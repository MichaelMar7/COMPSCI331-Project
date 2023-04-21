package proj.concert.service.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import proj.concert.common.jackson.LocalDateTimeDeserializer;
import proj.concert.common.jackson.LocalDateTimeSerializer;
import proj.concert.common.types.BookingStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Seat{

	@Id
	@GeneratedValue
	private Long id;
	private String label;
	private boolean isBooked;
	private BookingStatus status = BookingStatus.Any;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime date;
	private BigDecimal cost;

	public Seat() {}
	public Seat(String label,
				boolean isBooked,
				LocalDateTime date,
				BigDecimal cost) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.cost = cost;
	}

	public Long getId() { return id; }
	public String getLabel() { return label; }
	public void setLabel(String label) { this.label = label; }
	public boolean isBooked() { return isBooked; }
	public void setBooked(boolean booked) {isBooked = booked; }
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	public LocalDateTime getDate() { return date; }
	public BookingStatus setBookingStatus() { return status; }
	public void setBookingStatus(BookingStatus status) { this.status = status; }

}
