package proj.concert.service.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "SEATS")
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "label", nullable = false)
	private String label;
	@Column(name = "price", nullable = false)
	private BigDecimal price;
	@Column(name = "isBooked", nullable = false)
	private boolean isBooked;
	@Column(name = "date", nullable = false)
	private LocalDateTime date;
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "concert_id")
	private Concert concert;
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "performer_id")
	private Performer performer;

	public Seat() {
	}

	public Seat(String label, BigDecimal price, boolean isBooked, LocalDateTime date, Concert concert, Performer performer) {
		this.label = label;
		this.price = price;
		this.isBooked = isBooked;
		this.date = date;
		this.concert = concert;
		this.performer = performer;
	}

	public Seat(String seatLabel, boolean b, LocalDateTime date, BigDecimal price) {
		this.label = seatLabel;
		this.isBooked = b;
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

	public Concert getConcert() {
		return concert;
	}

	public void setConcert(Concert concert) {
		this.concert = concert;
	}

	public Performer getPerformer() {
		return performer;
	}

	public void setPerformer(Performer performer) {
		this.performer = performer;
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