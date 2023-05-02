# Organisation

## Authors

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
- Alexandre Szelag Dev 3 (asze997, Clavides)
    - Participated in the domain model discussion.
    - Implemented features such as Booking, UserMapper 
    - Updates to ConcertResource in order to implement login method.
    - Contributed to resolving issues and code reviews.
    - Created Organisation.md and made the initial draft 
    - Updated Organisation.md 


## Concurrency Error Minimization Strategy



## Domain Model Organisation

Our domain model has a hierarchical structure of classes. 
Each class represents a thing in the system and has its own data and logic. 
We use associations and object-oriented design rules to connect classes.
