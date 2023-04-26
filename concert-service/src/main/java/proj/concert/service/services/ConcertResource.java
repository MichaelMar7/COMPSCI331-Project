package proj.concert.service.services;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.common.dto.ConcertInfoSubscriptionDTO;
import proj.concert.common.dto.ConcertSummaryDTO;
import proj.concert.service.domain.*;
import proj.concert.service.mapper.ConcertMapper;
import proj.concert.service.mapper.ConcertSummaryMapper;

import javax.persistence.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.container.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.*;
import java.net.URI;
import java.util.stream.Collectors;

@Path("/concert-service")
public class ConcertResource {
    EntityManager em = PersistenceManager.instance().createEntityManager();
    EntityTransaction tx = em.getTransaction();
    ResponseBuilder builder;
    private final static Map<ConcertInfoSubscriptionDTO, AsyncResponse> subs = new HashMap<>(); // subs used when specific concerts is getting near concert cap.

    /*
    Retrieves a single concert using a given ID from the web service.
     */
    @GET
    @Path("/concerts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
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

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateConcert(@PathParam("id") long id, ConcertDTO concertDTO) {

        EntityTransaction tx = em.getTransaction();
        //ResponseBuilder builder;
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
    @Produces(MediaType.APPLICATION_JSON)
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

}
