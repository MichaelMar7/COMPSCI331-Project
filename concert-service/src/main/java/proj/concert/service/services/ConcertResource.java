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
import java.util.stream.Collectors;

@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    EntityManager em = PersistenceManager.instance().createEntityManager();
    EntityTransaction tx = em.getTransaction();
    ResponseBuilder builder;
    private final static Map<ConcertInfoSubscriptionDTO, AsyncResponse> subs = new HashMap<>(); // subs used when specific concerts is getting near concert cap.

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
    public Response getPerformers(@PathParam("id") long id) {

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
        EntityManager em = PersistenceManager.instance().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        ResponseBuilder builder;

        try {
            tx.begin();
            TypedQuery<User> userQuery = em.createQuery("select u from User u where u.username = :username", User.class);
            userQuery.setParameter("username", userDTO.getUsername());
            List<User> users = userQuery.getResultList();
            tx.commit();

            if (users.isEmpty()) {
                builder = Response.status(Response.Status.UNAUTHORIZED);
            } else {
                User user = users.get(0);
                if (user.getPassword().equals(userDTO.getPassword())) {
                    UserDTO loggedInUser = UserMapper.toDto(user);
                    NewCookie authCookie = new NewCookie("auth", user.getId().toString(), "/", "", "Authentication Cookie", NewCookie.DEFAULT_MAX_AGE, false);
                    builder = Response.ok(loggedInUser).cookie(authCookie);
                } else {
                    builder = Response.status(Response.Status.UNAUTHORIZED);
                }
            }

        } finally {
            em.close();
        }
        return builder.build();
    }

    @GET
    @Path("/seats/{date}")
    public Response getSeatsForDate(@PathParam("date") LocalDateTimeParam date, @QueryParam("status") BookingStatus status) {

        try {
            tx.begin();
            TypedQuery<Seat> seatQuery = em
                    .createQuery("select s from Seat s where s.date = :date", Seat.class)
                    .setParameter("date", date.getLocalDateTime());
            //TypedQuery<Seat> seatQuery = em
            //        .createQuery("select s from Seat s", Seat.class);
            List<Seat> seats = seatQuery.getResultList();
            tx.commit();
            List<SeatDTO> seatDTOs = new ArrayList<>();
            for (Seat s: seats) {
                if ((status == BookingStatus.Booked && s.getBookingStatus() == BookingStatus.Booked) ||
                        (status == BookingStatus.Unbooked && (s.getBookingStatus() == BookingStatus.Unbooked || s.getBookingStatus() == BookingStatus.Any)) ||
                        status == BookingStatus.Any) {
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
    public Response makeBookingRequest(BookingRequestDTO bookingRequestDTO) {

        try {
            BookingRequest request = BookingRequestMapper.toDomainModel(bookingRequestDTO);

            for (String label: request.getSeatLabels()) {
                tx.begin();
                TypedQuery<Seat> seatQuery = em
                        .createQuery("select s from Seat s where s.label = :label and s.date = :date", Seat.class)
                        .setParameter("label", label)
                        .setParameter("date", request.getDate());
                List<Seat> seats = seatQuery.getResultList();
                for (Seat s : seats) {
                    s.setBookingStatus(BookingStatus.Booked);
                    s.setDate(request.getDate());
                }
                tx.commit();

            }
            builder = Response.created(URI.create("/seats/" + request.getDate() + "?status=Booked"));
        } finally {
            em.close();
        }

        return builder.build();
    }

    @GET
    @Path("/subscribe/concertInfo")
    public void subscribe(ConcertInfoSubscription concertInfoSubscription, @Suspended AsyncResponse sub) {
        subs.put(ConcertInfoSubscriptionMapper.toDto(concertInfoSubscription), sub);
    }

    @POST
    @Path("/subscribe/concertInfo")
    public Response postSubscription(ConcertInfoSubscription concertInfoSubscription) {
        synchronized (subs) {
            for (Map.Entry<ConcertInfoSubscriptionDTO, AsyncResponse> sub : subs.entrySet()) {
                sub.getValue().resume(concertInfoSubscription);
            }
            subs.clear();
        }
        return Response.ok().build();
    }
}
