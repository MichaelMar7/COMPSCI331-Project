package proj.concert.service.mapper;

import proj.concert.service.domain.Concert;
import proj.concert.common.dto.ConcertDTO;
import proj.concert.service.domain.Performer;
import proj.concert.common.dto.PerformerDTO;

public class PerformerMapper {

    public static Performer toDomainModel(PerformerDTO dtoPerformer) {
        Performer domainPerformer = new Performer(
                dtoPerformer.getId(),
                dtoPerformer.getName(),
                dtoPerformer.getImageName(),
                dtoPerformer.getGenre(),
                dtoPerformer.getBlurb()
        );
        return domainPerformer;
    }

    public static PerformerDTO toDto(Performer performer) {
        PerformerDTO dtoPerformer = new PerformerDTO(
                performer.getId(),
                performer.getName(),
                performer.getImageName(),
                performer.getGenre(),
                performer.getBlurb()
        );
        return dtoPerformer;
    }

}
