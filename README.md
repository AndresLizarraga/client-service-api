# Client Service API

A secure and event-driven RESTful API built to manage customer data, supporting full CRUD operations and asynchronous messaging using RabbitMQ. Designed with clean architecture principles and modern development best practices.

## 🛠️ Tech Stack

- Java 21
- Spring Boot
- Spring Security with JWT
- PostgreSQL 15
- RabbitMQ
- Docker

## ⚙️ Design & Architecture

- **RESTful architecture**: Clean and resource-oriented HTTP endpoints.
- **Event-driven communication**: RabbitMQ integration ensures loose coupling and high scalability.
- **Design patterns and principles**:
    - *Builder Pattern* for structured DTO creation.
    - *ControllerAdvice* for centralized exception handling.
    - *SOLID principles* to ensure clean, testable, and maintainable code.

## 🚀 Running Locally

### Requirements

- Java 21
- Docker
- Maven

### Steps

1. Once you cloned the repository, build the project:

./mvnw clean install

2. Start PostgreSQL and RabbitMQ containers:

docker-compose up -d

3. Run the Spring Boot application:

/mvnw spring-boot:run

The API will be available at:

http://localhost:8080


## 🔐 Authentication

To consume the protected endpoints, you must first authenticate via:

POST /auth/login

You can use one of the pre-registered users to test role-based access control:

- **Admin user**
    - `username`: `admin`
    - `password`: `password`

- **Regular user**
    - `username`: `user`
    - `password`: `password`

Once authenticated, you'll receive a JWT token that must be included in the `Authorization` header as follows:

Authorization: Bearer <your_token_here>

## 📘 API Documentation

Swagger UI is available to explore and test the API interactively:

http://localhost:8080/swagger-ui.html
