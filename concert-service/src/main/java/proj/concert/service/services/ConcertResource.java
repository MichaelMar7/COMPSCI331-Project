package proj.concert.service.services;

import proj.concert.common.dto.*;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.*;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.*;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.container.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.*;
import java.net.URI;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    EntityManager em = PersistenceManager.instance().createEntityManager();
    EntityTransaction tx = em.getTransaction();
    ResponseBuilder builder;

    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);
    private final static Map<ConcertInfoSubscriptionDTO, AsyncResponse> subs = new HashMap<>(); // subs used when specific concerts is getting near concert cap.
    ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * Retrieves a single concert using a given ID from the web service.
     */
    @GET
    @Path("/concerts/{id}")
    public Response getConcert(@PathParam("id") long id) {

        try {
            tx.begin();
            Concert concert = em.find(Concert.class, id);
            tx.commit();

            if (concert == null) {
                builder = Response.status(404);
            } else {
                ConcertDTO concertDTO = ConcertMapper.toDto(concert);
                builder = Response.ok(concertDTO);
            }

        } finally {
            em.close();
        }
        return builder.build();
    }

    /*
    Retrieves all concerts from the web service
     */
    @GET
    @Path("/concerts")
    public Response getAllConcerts() {
        List<ConcertDTO> concertDTOs = new ArrayList<>();
        try {
            tx.begin();
            TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c",Concert.class);
            List<Concert> concerts = concertQuery.getResultList();
            concertDTOs = concerts.stream()
                    .map(concert -> ConcertMapper.toDto(concert))
                    .collect(Collectors.toList());
            tx.commit();
        } finally {
            em.close();
        }
        return Response.ok(concertDTOs).build();
    }

    /*
    Retrieves a summary of all concerts from the web service
     */
    @GET
    @Path("/concerts/summaries")
    public Response getAllConcertSummaries() {

        List<ConcertSummaryDTO> summaryDTOs = new ArrayList<>();
        try {
            tx.begin();
            TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c",Concert.class);
            List<Concert> concerts = concertQuery.getResultList();
            for (Concert c: concerts) {
                ConcertSummary summary = new ConcertSummary(
                        c.getId(),
                        c.getTitle(),
                        c.getImageName()
                );
                summaryDTOs.add(ConcertSummaryMapper.toDto(summary));
            }
        } finally {
            em.close();
        }
        return Response.ok(summaryDTOs).build();
    }

    @GET
    @Path("/performers/{id}")
    public Response getPerformer(@PathParam("id") long id) {

        try {
            tx.begin();
            Performer performer = em.find(Performer.class, id);
            tx.commit();

            if (performer == null) {
                builder = Response.status(404);
            } else {
                PerformerDTO performerDTO = PerformerMapper.toDto(performer);
                builder = Response.ok(performerDTO);
            }

        } finally {
            em.close();
        }
        return builder.build();
    }

    @GET
    @Path("/performers")
    public Response getAllPerformers() {
        List<PerformerDTO> performerDTOS = new ArrayList<>();
        try {
            tx.begin();
            TypedQuery<Performer> performerQuery = em.createQuery("select p from Performer p",Performer.class);
            List<Performer> performers = performerQuery.getResultList();
            performerDTOS = performers.stream()
                    .map(performer -> PerformerMapper.toDto(performer))
                    .collect(Collectors.toList());
            tx.commit();
        } finally {
            em.close();
        }
        return Response.ok(performerDTOS).build();
    }

    @POST
    public Response createConcert(ConcertDTO concertDTO) {
        Concert concert = ConcertMapper.toDomainModel(concertDTO);
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(concert);
            tx.commit();
        } finally {
            em.close();
        }

        return Response.created(URI.create("/concerts/" + concert.getId())).entity(ConcertMapper.toDto(concert)).build();
    }

    @PUT
    @Path("/concerts/{id}")
    public Response updateConcert(@PathParam("id") long id, ConcertDTO concertDTO) {

        EntityTransaction tx = em.getTransaction();
        Concert updatedConcert = null;
        try {
            tx.begin();
            Concert concert = em.find(Concert.class, id);
            if (concert == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            updatedConcert = ConcertMapper.updateFromDto(concertDTO, concert);
            em.merge(updatedConcert);
            tx.commit();
        } finally {
            em.close();
        }

        if (updatedConcert == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        ConcertDTO updatedConcertDTO = ConcertMapper.toDto(updatedConcert);
        return Response.ok(updatedConcertDTO).build();
    }

    @DELETE
    @Path("/concerts/{id}")
    public Response deleteConcert(@PathParam("id") long id) {
        EntityTransaction tx = em.getTransaction();
        ResponseBuilder builder;
        try {
            tx.begin();
            Concert concert = em.find(Concert.class, id);
            if (concert == null) {
                builder = Response.status(Response.Status.NOT_FOUND);
            } else {
                em.remove(concert);
                builder = Response.noContent();
            }
            tx.commit();

        } finally {
            em.close();
        }

        return builder.build();
    }

    @DELETE
    public Response deleteAllConcerts() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("DELETE FROM Concert");
            int numDeleted = query.executeUpdate();
            tx.commit();
        } finally {
            em.close();
        }

        return Response
                .noContent()
                .build();
    }

    @POST
    @Path("/login")
    public Response login(UserDTO userDTO) {

        try {
            tx.begin();
            TypedQuery<User> userQuery = em
                    .createQuery("select u from User u where u.username = :username and u.password = :password", User.class)
                    .setParameter("username", userDTO.getUsername())
                    .setParameter("password", userDTO.getPassword());
            List<User> userList = userQuery.getResultList();
            tx.commit();

            if (userList.isEmpty()) {
                builder = Response.status(Response.Status.UNAUTHORIZED);
            } else {
                User user = userList.get(0);
                UserDTO loggedInUser = UserMapper.toDto(user);
                NewCookie cookie;

                if (user.getUuid() == null) {
                    cookie = makeCookie(null);
                    user.setUuid(cookie.getValue());
                    user.addUuids(cookie.getValue());
                    em.merge(user);
                    LOGGER.debug("UUID for user " + user.getUsername() + ": " + user.getUuid() );
                } else {
                    cookie = NewCookie.valueOf(user.getUuid());
                }

                builder = Response.ok(loggedInUser).cookie(cookie);
            }

        } catch (NoResultException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        finally {
            em.close();
        }

        return builder.build();
    }

    @GET
    @Path("/seats/{date}")
    public Response getSeatsForDate(@PathParam("date") LocalDateTimeParam date, @QueryParam("status") BookingStatus status, @CookieParam("auth") Cookie auth) {

        try {
            tx.begin();
            TypedQuery<Seat> seatQuery = em
                    .createQuery("select s from Seat s where s.date = :date", Seat.class)
                    .setParameter("date", date.getLocalDateTime());
            List<Seat> seats = seatQuery.getResultList();
            tx.commit();
            List<SeatDTO> seatDTOs = new ArrayList<>();

            for (Seat s: seats) {
                if (status == BookingStatus.Booked && s.isBooked()) {
                    seatDTOs.add(SeatMapper.toDto(s));
                } else if (status == BookingStatus.Unbooked && !s.isBooked()) {
                    seatDTOs.add(SeatMapper.toDto(s));
                } else if (status == BookingStatus.Any) {
                    seatDTOs.add(SeatMapper.toDto(s));
                }
            }

            builder = Response.ok(seatDTOs);
        } finally {
            em.close();
        }

        return builder.build();
    }

    @POST
    @Path("/bookings")
    public Response makeBooking(BookingRequestDTO bookingRequestDTO, @CookieParam("auth") Cookie auth) {

        try {
            tx.begin();
            BookingRequest request = BookingRequestMapper.toDomainModel(bookingRequestDTO);
            if (auth == null) {
                LOGGER.debug("No cookie >:(");
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            LOGGER.debug("Found cookie! UUID string: " + auth.getValue());
            TypedQuery<Concert> concertQuery = em
                    .createQuery("select c from Concert c where c.id = :id ", Concert.class)
                    .setParameter("id", request.getConcertId());
            Concert c = concertQuery.getSingleResult();

//            TypedQuery<User> userQuery = em
//                    .createQuery("select u from User u where u.uuid = :uuid", User.class)
//                    .setParameter("uuid", auth.getValue());
//            User user = userQuery.getSingleResult();
//            tx.commit();
            TypedQuery<User> userQuery = em
                    .createQuery("select u from User u", User.class);
            List<User> users = userQuery.getResultList();

            for (User u: users) {
                LOGGER.debug("UUID for user " + u.getUsername() + ": " + u.getUuid() + ". List of UUIDs: " + u.getUuids());
            }

            Set<Seat> seatsToBook = new HashSet<>();
            for (String label: request.getSeatLabels()) {
                TypedQuery<Seat> seatQuery = em
                        .createQuery("select s from Seat s where s.label = :label and s.date = :date", Seat.class)
                        .setParameter("label", label)
                        .setParameter("date", request.getDate());
                List<Seat> seats = seatQuery.getResultList();

                for (Seat s : seats) {
                    if (s.getLabel().equals(label)) {
                        if (!s.isBooked()) {
                            s.setBooked(true);
                            s.setBookingStatus(BookingStatus.Booked);
                            seatsToBook.add(s);
                            em.merge(s);
                        } else {
                            return Response.status(Response.Status.FORBIDDEN).build();
                        }
                    }
                }
            }

            tx.commit();
            Booking booking = new Booking(
                    request.getConcertId(),
                    request.getDate(),
                    seatsToBook
            );
            builder = Response
                    .created(URI.create("/bookings/" + booking.getConcertId()))
                    .entity(BookingMapper.toDto(booking));
        }
        catch (NoResultException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        finally {
            em.close();
        }

        return builder.build();
    }

//    @GET
//    @Path("/subscribe/concertInfo")
//    public void subscribe(ConcertInfoSubscription concertInfoSubscription, @Suspended AsyncResponse sub) {
//        subs.put(ConcertInfoSubscriptionMapper.toDto(concertInfoSubscription), sub);
//    }

    @POST
    @Path("/subscribe/concertInfo")
    public Response subscription(ConcertInfoSubscriptionDTO concertInfoSubscription, AsyncResponse sub, @CookieParam("ClientId") Cookie cookie) {
    //public Response postSubscription(ConcertInfoSubscription concertInfoSubscription) {
        // authentication
        // check concert date if exist
        try {
            tx.begin();
            Concert concert = em.find(Concert.class, concertInfoSubscription.getConcertId());
            if (concert == null || !concert.getDates().contains(concertInfoSubscription.getDate())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            tx.commit();
        } finally {
            em.close();
        }

        synchronized (subs) {
            subs.put(concertInfoSubscription, sub);
        }
        return Response.ok().build();
    }

    /*
    Helper function that creates a NewCookie instance whenever a user successfully logs in.
     */
    private static NewCookie makeCookie(Cookie auth) {

        NewCookie cookie = null;
        if (auth == null) {
            cookie = new NewCookie("auth", UUID.randomUUID().toString());
            LOGGER.info("Generated cookie: " + cookie.getValue());
        }

        return cookie;
    }

}
