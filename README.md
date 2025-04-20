# client-service-api
RESTful API built with Spring Boot and Java 21 for managing customer data. Features include CRUD operations, validation, exception handling, PostgreSQL integration, and unit testing with JUnit 5 and Mockito. Designed following clean architecture principles.

### 📩 Asynchronous Messaging with RabbitMQ

An asynchronous messaging system was implemented using **RabbitMQ** (via **CloudAMQP**) to audit customer creation events.

When a customer is successfully created, a JSON message is published to a queue (`client.created.queue`) through an exchange (`client.exchange`) using a routing key (`client.created`). 

A dedicated listener component consumes this message and logs the event for auditing purposes. This setup helps decouple the main business logic from secondary operations, following a clean and scalable event-driven architecture.