package proj.concert.service.mapper;

import proj.concert.service.domain.Seat;
import proj.concert.common.dto.SeatDTO;

public class SeatMapper {

    public static Seat toDomainModel(SeatDTO SeatDTO) {
        Seat domainSeat = new Seat(
                SeatDTO.getLabel(),
                SeatDTO.getPrice()
        );
        return domainSeat;
    }

    public static SeatDTO toDto(Seat Seat) {
        SeatDTO dtoSeat = new SeatDTO(
                Seat.getLabel(),
                Seat.getPrice()
        );
        return dtoSeat;
    }

}
