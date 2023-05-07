# Organisation

## Domain Model Structure

Our domain model consists of several classes that each represent an object in the Concert Booking Service. These classes are:
- `Booking` - represents a `Booking` successfully made by a `User` client logged into the Web Service for a specific `Concert`, after requesting for one. 
- `BookingRequest` - represents a request made by a `User` client logged into the Web Service to book one or more `Seat`s at a `Concert`
- `Concert` - represents a `Concert` that is available to be booked on the Web Service. 
- `ConcertInfoNotification` - represents a `Notification` a `User` may receive about the number of `Seat`s remain at a `concert` (should they choose to subscribe) 
- `ConcertInfoSubscription` - represents a `Subscription` made by a `User` to receive `Notification`s relating to any changes made to a particular `Concert`
- `ConcertSummary` - represents a brief `Summary` of a given `Concert`
- `Performer` - represents a `Performer` that will be performing at one or more `Concert`s
- `Seat` - represents a `Seat` at a `Concert` that `User`s can enquire and book
- `User` - represents a logged `User` in the Web Service 

Each class has their own respective Data Transfer Object (DTO) class that is used to transfer lightweight and minimal but crucial data between the client and server via Jackson JSON marshalling and unmarshalling. Furthermore, to ensure seamless conversion between a DTO object and a Domain object without losing any important information, each class also has their own Mapper class to ensure this is the case while the Web Service is being used.
Finally, to ensure the persistence of our data in the Web Service, we have annotated every domain class using JPA Hibernate. This also allows us to represent certain relationships two classes may have, such as one-to-many or many-to-many.

## Changelog

All discussions made as a team was primarily conducted on a private Discord server. Please refer to the Teams Discussion post under Issues on GitHub to view the chat logs.

- Michael Mar as Dev 1 (mma667, MichaelMar7)
    - Participated in the domain model discussion.
    - Implemented methods for GET, POST, PUT, and DELETE.
    - Contributed to resolving issues and code reviews.
    - Started on subscription and updated subscriptions methods
    - Fixed booking methods
    - re-commented subscription tests
- Dylan Choy as Dev 2 and Team Leader (dcho282, ghxstling)
    - Implemented ConcertApplication class and annotated Concert and Performer domain models appropriately.
    - Ran DomainModelTest, all tests passed.
    - Worked on ConcertResource class, attempted to implement GET and POST methods.
    - Successfully fixed retrieveConcert and createConcert methods, 2 tests passed.
    - Successfully fixed deleteConcert method, 1 test passed.
    - Successfully fixed deleteAllConcerts method, 1 test passed.
    - Successfully fixed updateConcert method, 1 test passed.
    - Implemented BookinqRequest.java, SeatMapper.java, BookingRequestMapper.java, BookingMapper.java
    - Implemented getSeatsForDate (1 test passed) & makebooking (need fixing)
    - Rewrote 'Domain Model Structure' section in Organisation.md
    - Updated 'Concurrency Error Minimization Strategy' section in Organisation.md 
    - Cleaned up code in User and ConcertResource
    - Pushed to submission branch :)
- Alexandre Szelag Dev 3 (asze997, Clavides)
    - Participated in the domain model discussion.
    - Implemented features such as Booking, UserMapper 
    - Updates to ConcertResource in order to implement login method.
    - Contributed to resolving issues and code reviews.
    - Created Organisation.md and made the initial draft 
    - Updated 'Changelog' section in Organisation.md 

## Concurrency Error Minimization Strategy

We utilised the Pessimistic Locking strategy to ensure only one user can access and update resources of the web service at any given time. An example of this strategy being utilised is in our GET `notification()` method:
```java
@GET
public void notification(long concertId, long remainingSeats) {
    synchronized (subs) {
        for (Map.Entry<ConcertInfoSubscriptionDTO, AsyncResponse> sub : subs.entrySet()) {
            if (sub.getKey().getConcertId() == concertId && remainingSeats < 60) {
                sub.getValue().resume(remainingSeats);
                subs.put(sub.getKey(), sub.getValue());
            }
        }
    }
}
```
Furthermore, we ensure that all transactions are commenced once and committed once after we have made all necessary queries on a database at any given time. This is to ensure that we do not run into conflicts when making multiple transactions at the same time.
For example, take a look at our POST `makeBooking()` method:
```java
@POST
@Path("/bookings")
public Response makeBooking(BookingRequestDTO bookingRequestDTO, @CookieParam("auth") Cookie auth) {

    ...

    try {
        tx.begin();
        
        ...
        TypedQuery<Concert> concertQuery = em
                .createQuery("select c from Concert c where c.id = :id", Concert.class) ...
        ...
        TypedQuery<User> userQuery = em
                .createQuery("select u from User u where u.uuid = :uuid", User.class) ...
        
        ...
        for (String label: request.getSeatLabels()) {
            TypedQuery<Seat> seatQuery = em
                    .createQuery("select s from Seat s where s.label = :label and s.date = :date", Seat.class) ...
            ...
        }

        Booking booking = new Booking( ... );
        booking.setUserId(user.getId());
        user.addBooking(booking);
        em.persist(booking);
        em.merge(user);

        List<Seat> remainingSeats = em.createQuery("select s from Seat s where s.date = :date and s.isBooked = :isBooked", Seat.class) ...
        
        ...
        tx.commit();
        ...
        
    finally {
        em.close();
    }
    return ...
}
```
