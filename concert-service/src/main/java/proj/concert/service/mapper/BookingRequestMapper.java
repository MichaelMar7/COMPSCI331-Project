package proj.concert.service.mapper;

import proj.concert.common.dto.BookingRequestDTO;
import proj.concert.service.domain.BookingRequest;

public class BookingRequestMapper {

    public static BookingRequest toDomainModel(BookingRequestDTO bookingRequestDTO) {
        BookingRequest domainBookingRequest = new BookingRequest(
                bookingRequestDTO.getConcertId(),
                bookingRequestDTO.getDate(),
                bookingRequestDTO.getSeatLabels()
        );
        return domainBookingRequest;
    }

    public static BookingRequestDTO toDto(BookingRequest bookingRequest) {
        BookingRequestDTO dtoBookingRequest = new BookingRequestDTO(
                bookingRequest.getConcertId(),
                bookingRequest.getDate(),
                bookingRequest.getSeatLabels()
        );
        return dtoBookingRequest;
    }

}
