package proj.concert.service.mapper;

import proj.concert.common.dto.ConcertInfoNotificationDTO;
import proj.concert.service.domain.ConcertInfoNotification;

public class ConcertInfoNotificationMapper {
    public static ConcertInfoNotification toDomainModel(ConcertInfoNotificationDTO concertInfoNotificationDTO) {
        return new ConcertInfoNotification(concertInfoNotificationDTO.getNumSeatsRemaining());
    }

    public static ConcertInfoNotificationDTO toDto(ConcertInfoNotification concertInfoNotification) {
        return new ConcertInfoNotificationDTO(concertInfoNotification.getNumSeatsRemaining());
    }
}
