package proj.concert.service.mapper;

import proj.concert.common.dto.BookingDTO;
import proj.concert.common.dto.SeatDTO;
import proj.concert.common.types.BookingStatus;
import proj.concert.service.domain.Booking;
import proj.concert.service.domain.Seat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingMapper {

    public static Booking toDomainModel(BookingDTO bookingDTO) {

        Set<Seat> seats = new HashSet<>();
        for (SeatDTO seatDTO: bookingDTO.getSeats()) {
            Seat s = SeatMapper.toDomainModel(seatDTO);
            s.setBooked(true);
            s.setBookingStatus(BookingStatus.Booked);
            s.setDate(bookingDTO.getDate());
            seats.add(s);
        }

        Booking domainBooking = new Booking(
                bookingDTO.getConcertId(),
                bookingDTO.getDate(),
                seats
        );
        return domainBooking;
    }

    public static BookingDTO toDto(Booking booking) {

        List<SeatDTO> seats = new ArrayList<>();
        for (Seat s: booking.getSeats()) {
            seats.add(SeatMapper.toDto(s));
        }
        BookingDTO dtoBooking = new BookingDTO(
                booking.getConcertId(),
                booking.getDate(),
                seats
        );
        return dtoBooking;
    }

}
