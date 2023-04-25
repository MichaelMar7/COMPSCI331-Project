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

    public static Concert updateFromDto(ConcertDTO concertDTO, Concert concert) {
        concert.setId(concertDTO.getId());
        concert.setTitle(concertDTO.getTitle());
        concert.setBlrb(concertDTO.getBlurb());
        concert.setImageName(concertDTO.getImageName());

        return concert;
    }

}
