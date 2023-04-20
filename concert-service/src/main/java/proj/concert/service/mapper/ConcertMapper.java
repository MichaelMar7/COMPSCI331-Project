package proj.concert.service.mapper;
import proj.concert.service.domain.Concert;

public class ConcertMapper {

    public static Concert toDomainModel(proj.concert.common.dto.ConcertDTO dtoConcert) {
        Concert domainConcert = new Concert(
                dtoConcert.getId(),
                dtoConcert.getTitle(),
                dtoConcert.getImageName(),
                dtoConcert.getBlurb()
        );
        return domainConcert;
    }

    public static proj.concert.common.dto.ConcertDTO toDto(Concert concert) {
        proj.concert.common.dto.ConcertDTO dtoConcert = new proj.concert.common.dto.ConcertDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName(),
                concert.getBlrb()
        );
        return dtoConcert;
    }

}
