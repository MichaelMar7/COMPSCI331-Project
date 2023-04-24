package proj.concert.service.mapper;
import proj.concert.common.dto.ConcertDTO;
import proj.concert.service.domain.Concert;


public class ConcertMapper {

    public static Concert toDomainModel(ConcertDTO concertDTO) {
        Concert domainConcert = new Concert(
                concertDTO.getId(),
                concertDTO.getTitle(),
                concertDTO.getImageName(),
                concertDTO.getBlurb()
        );
        return domainConcert;
    }

    public static ConcertDTO toDto(Concert concert) {
        ConcertDTO dtoConcert = new ConcertDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName(),
                concert.getBlrb()
        );
        return dtoConcert;
    }

    public static Concert updateFromDto(ConcertDTO concertDTO, Concert concert) {
        concert.setId(concertDTO.getId());
        concert.setTitle(concertDTO.getTitle());
        concert.setBlrb(concertDTO.getBlurb());
        concert.setImageName(concertDTO.getImageName());

        return concert;
    }

}
