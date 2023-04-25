package proj.concert.service.mapper;

import proj.concert.common.dto.ConcertSummaryDTO;
import proj.concert.service.domain.ConcertSummary;

public class ConcertSummaryMapper {

    public static ConcertSummary toDomainModel(ConcertSummaryDTO concertSummaryDTO) {
        ConcertSummary domainConcertSummary = new ConcertSummary(
                concertSummaryDTO.getId(),
                concertSummaryDTO.getTitle(),
                concertSummaryDTO.getImageName()
        );
        return domainConcertSummary;
    }

    public static ConcertSummaryDTO toDto(ConcertSummary concertSummary) {
        ConcertSummaryDTO dtoConcertSummary = new ConcertSummaryDTO(
                concertSummary.getId(),
                concertSummary.getTitle(),
                concertSummary.getImageName()
        );
        return dtoConcertSummary;
    }

}
