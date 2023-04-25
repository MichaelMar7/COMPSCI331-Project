package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Seat {

	@Id
	@GeneratedValue
	private long id;
	private boolean isBooked;
	private LocalDateTime date;
	private String label;
	private BigDecimal price;

	public Seat() {}

	public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal price) {
		this.label = label;
		this.isBooked = isBooked;
		this.date = date;
		this.price = price;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean booked) {
		isBooked = booked;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Seat seat = (Seat) o;

		return new EqualsBuilder()
				.append(id, seat.id)
				.append(label, seat.label)
				.append(price, seat.price)
				.append(isBooked, seat.isBooked)
				.append(date, seat.date)
				.isEquals();
	}


	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(id)
				.append(label)
				.append(price)
				.append(isBooked)
				.append(date)
				.toHashCode();
	}
}