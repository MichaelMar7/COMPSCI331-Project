package proj.concert.service.mapper;

import proj.concert.service.domain.Concert;
import proj.concert.common.dto.ConcertDTO;
import proj.concert.service.domain.Performer;
import proj.concert.common.dto.PerformerDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConcertMapper {

    public static Concert toDomainModel(ConcertDTO dtoConcert) {
        Concert domainConcert = new Concert(
                dtoConcert.getId(),
                dtoConcert.getTitle(),
                dtoConcert.getImageName(),
                dtoConcert.getBlurb()
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
        List<PerformerDTO> performers = new ArrayList<>();
        for (Performer p: concert.getPerformers()) {
            performers.add(PerformerMapper.toDto(p));
        }
        dtoConcert.setPerformers(performers);
        List<LocalDateTime> dates = new ArrayList<>();
        for (LocalDateTime d: concert.getDates()) {
            dates.add(d);
        }
        dtoConcert.setDates(dates);
        return dtoConcert;
    }

}
