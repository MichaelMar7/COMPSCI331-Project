package proj.concert.service.mapper;

import proj.concert.common.dto.ConcertInfoSubscriptionDTO;
import proj.concert.service.domain.ConcertInfoSubscription;

public class ConcertInfoSubscriptionMapper {
    public static ConcertInfoSubscription toDomainModel(ConcertInfoSubscriptionDTO concertInfoSubscriptionDTO) {

        return new ConcertInfoSubscription(
                concertInfoSubscriptionDTO.getConcertId(),
                concertInfoSubscriptionDTO.getDate(),
                concertInfoSubscriptionDTO.getPercentageBooked()
        );
    }

    public static ConcertInfoSubscriptionDTO toDto(ConcertInfoSubscription concertInfoSubscription) {
        return new ConcertInfoSubscriptionDTO(
                concertInfoSubscription.getConcertId(),
                concertInfoSubscription.getDate(),
                concertInfoSubscription.getPercentageBooked()
        );
    }
}
