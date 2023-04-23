package proj.concert.service.services;

import proj.concert.common.dto.ConcertDTO;
import proj.concert.service.domain.*;
import proj.concert.service.mapper.ConcertMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@Path("/concerts")
public class ConcertResource {

    EntityManager em = PersistenceManager.instance().createEntityManager();

    /*
    Retrieves a single concert using a given ID from the web service.
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConcert(@PathParam("id") long id) {

        EntityTransaction tx = em.getTransaction();
        ResponseBuilder builder;

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
//    public Response getAllConcerts() { return Response.noContent().build(); }
//
//    /*
//    Retrieves a summary of all concerts from the web service
//     */
//    public Response getAllConcertSummaries() { return Response.noContent().build(); }

}
