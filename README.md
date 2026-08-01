# Smart Parking Management System (SPMS)

A microservice-based platform for real-time parking space management, built with Spring Boot and Spring Cloud, following cloud-native architectural patterns (service discovery, centralized configuration, API gateway routing). Developed as the 
ITS 1018 - Software Architectures & Design Patterns II final examination assignment.

## Overview

SPMS solves urban parking inefficiency by giving drivers real-time visibility into available parking spaces, letting parking owners manage their spaces dynamically, and handling digital payments end-to-end — all through a decoupled, independently deployable microservice architecture.

## Architecture

| Component | Responsibility | Port |
|---|---|---|
| Eureka Server | Service registry & discovery | 8761 |
| Config Server | Centralized configuration management | 8888 |
| API Gateway | Single entry point, routing, JWT authentication | 8080 |
| Parking Space Service | Space CRUD, availability, dynamic pricing, reservations | 8081 |
| Vehicle Service | Vehicle registration, entry/exit tracking | 8082 |
| User Service | Registration, authentication, JWT issuance, email notifications | 8083 |
| Payment Service | Mock payments, PayHere gateway integration, receipts | 8084 |

All business services register with Eureka on startup and are routed exclusively through the API Gateway.

## Tech Stack

- **Spring Boot 3.3.4** — core framework for all microservices
- **Spring Cloud 2023.0.3** — Eureka, Gateway, LoadBalancer
- **Spring Data JPA + MySQL** — persistence layer
- **Spring Security + BCrypt** — password hashing
- **JJWT** — JWT token generation and validation
- **Resilience4j** — circuit breaker
- **Spring Mail** — email notifications
- **Springdoc OpenAPI (Swagger)** — API documentation
- **PayHere** — Sri Lankan payment gateway (sandbox)
- **ngrok** — public tunnel for local webhook testing
- **Postman** — API testing

## Unique / Advanced Features

Beyond the core assignment requirements, this project implements:

- **JWT-based authentication** at the API Gateway level — all protected routes require a valid Bearer token issued at login
- **Optimistic locking** on parking space reservations (`@Version` field) to prevent race conditions when multiple users attempt to reserve the same space simultaneously
- **Resilience4j circuit breaker** — Vehicle Service calls User Service via load-balanced WebClient with a graceful fallback if User Service is unavailable
- **Dynamic pricing** — parking rates automatically surge during peak hours (7–9 AM, 5–7 PM)
- **BCrypt password hashing** with a DTO response layer that ensures password data never leaves the User Service, even hashed
- **Email notifications** — a welcome email is sent automatically on user registration
- **PayHere payment gateway integration** — real sandbox checkout flow with MD5 hash generation/verification, tested end-to-end via an ngrok public tunnel
- **Swagger/OpenAPI documentation** on all business services

## Getting Started

### Prerequisites
- Java 21
- Maven
- MySQL running locally
- (Optional) ngrok, for testing the PayHere webhook flow

### Running the System

Start services in this order:

1. `eureka-server` → verify at `http://localhost:8761`
2. `config-server` → verify at `http://localhost:8888`
3. `api-gateway`
4. `parking-space-service`
5. `vehicle-service`
6. `user-service`
7. `payment-service`

Each service registers with Eureka automatically on startup.

### API Documentation (Swagger)

Once the corresponding service is running, its interactive API docs are available at:

- Parking Space Service — [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- Vehicle Service — [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
- User Service — [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)
- Payment Service — [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html)

### Authentication

1. Register: `POST /api/user/register`
2. Login: `POST /api/user/login` → returns a JWT
3. Include the token as `Authorization: Bearer <token>` on all subsequent requests through the Gateway

## Resources

- [Postman Collection](./postman_collection.json)
- ![Eureka Dashboard](<img width="1917" height="1078" alt="Screenshot 2026-08-01 135033" src="https://github.com/user-attachments/assets/db430696-c8f6-41fe-a10f-62c1180cf0f4" />
 )

## Testing

All endpoints were tested via Postman through the API Gateway (`http://localhost:8080`), covering:
- Standard CRUD flows for all four business services
- JWT-protected route access (401 on missing/invalid token)
- Optimistic locking conflict handling (409 on concurrent reservation)
- Circuit breaker fallback behavior (User Service stopped mid-test)
- Dynamic pricing calculation across peak/off-peak hours
- PayHere sandbox checkout, verified through an ngrok tunnel

## Project Structure

```
Smart Parking Management System/
├── eureka-server/
├── config-server/
├── api-gateway/
├── parking-space-service/
├── vehicle-service/
├── user-service/
├── payment-service/
├── postman_collection.json
├── docs/
│   └── screenshots/
│       └── eureka_dashboard.png
└── README.md
```

## Author

Saumya Divyanjalee — IJSE, Graduate Diploma in Software Engineering
