package proj.concert.service.domain;

public class ConcertRemainingSeats {
    private long concertId;
    private long remainingSeats;
    private static final long MAX_SEAT = 60;

    public ConcertRemainingSeats() {}

    public ConcertRemainingSeats(long concertId, int remainingSeats) {
        this.concertId = concertId;
        this.remainingSeats = remainingSeats;
    }

    public long getConcertId() { return this.concertId; }

    public long getRemainingSeats() { return this.remainingSeats; }
}
