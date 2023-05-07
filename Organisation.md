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

## Changelog (WIP - PLEASE UPDATE)

All discussions made as a team was primarily conducted on a private Discord server. Please refer to the Teams Discussion post under Issues on GitHub to view the chat logs.

- Michael Mar as Dev 1 (mma667, MichaelMar7)
    - Participated in the domain model discussion.
    - Implemented methods for GET, POST, PUT, and DELETE.
    - Contributed to resolving issues and code reviews.
    - Started on subscription and updated subscriptions methods
    - Fixed booking methods
    - re-commented subscription tests
    - Implemented cookie authentication in subscription.
    - Fixed authentication for subscription.
    - Removed notification and subscription classes and mappers.
    - Fixed subscription method
    - Completed GET subscription method
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
    - Rewrote 'Domain Model Structure' section
    - Continued debugging GET methods for Bookings (testGetOwnBookingById).
    - Tweaked URI for getAllBookingsForUser, added getSingleBookingForUser.
    - Tweaked getBookingById
    - Add bookings field, getBookings, setBookings, and addBooking methods to User.java.
    - Fixed user cookie auth not persisting.
    - Began implementation of getBooking(), added findUserByUuid() helper method.
    - Refactored login() and getSeatsForDate(), fixed makeBooking().
    - Added uuids field, getUuids, and addUuids in User.java.
    - Add userId field and getter/setter for Booking.java.
    - Add uuid field for cookie auth in Use
- Alexandre Szelag Dev 3 (asze997, Clavides)
    - Participated in the domain model discussion.
    - Implemented features such as Booking, UserMapper 
    - Created UserMapper for User.java and UserDTO conversion
    - Updates to ConcertResource in order to implement login method.
    - Contributed to resolving issues and code reviews.
    - Created Organisation.md and made the initial draft 
    - Updated Organisation.md 
    - Updated makeBooking to handle auth cookie check.
    - Finished implementing getSingleBookingForUser.
    - Updated the changelog


## Concurrency Error Minimization Strategy

placeholder


