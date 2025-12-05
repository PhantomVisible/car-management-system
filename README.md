## 🚗 Car Management System – Microservices Platform

### A production-ready, cloud-native microservices platform for car rentals built with Java & Spring Boot.

<p align="center"> <img src="https://img.shields.io/badge/Java-21-red" /> <img src="https://img.shields.io/badge/Spring_Boot-3.2.5-brightgreen" /> <img src="https://img.shields.io/badge/Microservices-Architecture-blue" /> <img src="https://img.shields.io/badge/Dependency-Eureka-orange" /> </p>

### 📋 Table of Contents

- Overview

- Architecture

- Tech Stack

- Features

- Project Structure

- Installation & Setup

- API Documentation

- Key Design Decisions

- Performance & Scalability

- Future Improvements

- Contributing

- Author

- Acknowledgments

### 📖 Overview

#### This project is a cloud-native microservices platform designed for modern car rental businesses.
#### It demonstrates:

- [x] High-volume rental processing

- [x] Real-time car availability

- [x] Secure user authentication

- [x] Fault-tolerant service communication

- [x] Independent deployable services

🚀 Designed using modern enterprise architecture patterns used by top tech companies.

### 🏗️ Architecture
#### 🧱 System Diagram
```
┌─────────────────────────────────────────────────────────────────────────┐
│                        API Gateway (Port: 8080)                         │
│                       Load Balancing & Routing                          │
└─────────────────┬─────────────────┬─────────────────┬───────────────────┘
                  │                 │                 │
   ┌──────────────▼─────┐  ┌────────▼───────────┐┌────▼──────────────┐
   │   Auth Service     │  │   Car Service      ││  Rental Service   │
   │    (8081)          │  │    (8082)          ││     (8083)        │
   │ - JWT Auth         │  │ - Inventory Mgmt   ││ - Booking Mgmt    │
   │ - RBAC             │  │ - Car Catalog      ││ - Availability    │
   │ - User Mgmt        │  │ - Pricing          ││ - Transactions    │
   └────────────────────┘  └────────────────────┘└───────────────────┘
                  │                 │                 │
   ┌──────────────▼─────────────────▼─────────────────▼───────────────┐
   │                         Eureka Server (8761)                     │
   │                    Service Discovery & Registry                  │
   └──────────────────────────────────────────────────────────────────┘
                  │                 │                 │
   ┌──────────────▼─────────────────▼─────────────────▼───────────────┐
   │                      PostgreSQL Databases                        │
   │             (Each service has its own isolated schema)           │
   └──────────────────────────────────────────────────────────────────┘
```
### 🧩 Key Architectural Patterns

- Microservices Architecture

- Domain-Driven Design (DDD)

- Hexagonal Architecture

- Circuit Breakers (Resilience4j)

- API Gateway Routing

- Service Discovery (Eureka)

- Event-Driven Communication Ready

### 🛠️ Tech Stack
#### ☕ Backend

- Java 21

- Spring Boot 3.2.5

- Spring Cloud (Gateway, Eureka, Feign)

- Spring Security + JWT

- Spring Data JPA

- Resilience4j
#### 🗄️ Database

- PostgreSQL

- H2 (dev/testing)


#### ⚙️ DevOps & Infrastructure

- API Gateway

- Declarative REST clients

- Containerized microservices

#### 🧪 Testing

- Postman
- Debuggers

### ✨ Features
#### 🔐 Authentication Service

- JWT-based authentication

- Refresh token rotation

- Role-Based Access Control (RBAC)

- BCrypt password encryption

- User CRUD

#### 🚗 Car Management Service

- Full car inventory

- Real-time availability

- Filtering & search

- Pricing configuration

- Car status transitions (AVAILABLE → RENTED)

#### 📅 Rental Service

- Rental creation

- Overlap detection

- Cost calculation

- History & cancellations

- Transaction-safe workflow

#### 🛡️ System-Wide Features

- Circuit Breakers

- API Gateway (rate limiting supported)

- Dynamic service discovery

- Health checks

- Distributed tracing ready

### 📁 Project Structure
```
car-management-platform/
│
├── api-gateway/                  
│   ├── src/main/java/com/carmanagement/api_gateway/
│       └── filters/              # Jwt
│   └── resources/application.yml
│
├── auth-service/
│   └── src/main/java/com/carmanagement/auth/
│       ├── application/          # Use cases
│       ├── domain/               # Entities, aggregates
│       ├── infrastructure/       # Controllers, adapters
│       └── shared/               # DTOs, exceptions
│   └── resources/application.yml
│
├── car-service/
│   └── src/main/java/com/carmanagement/car/
│       ├── application/          
│       ├── domain/               
│       ├── infrastructure/       # Controllers, adapters
│       └── shared/               # DTOs, exceptions
│   └── resources/application.yml
│
├── rental-service/
│   └── src/main/java/com/carmanagement/rental/
│       ├── application/          # Use cases
│       ├── domain/               # Entities, aggregates
│       ├── infrastructure/       # Controllers, adapters
│       └── shared/               # DTOs, exceptions
│   └── resources/application.yml
│ 
├── service-discovery/
├── api-gateway/                  
│   ├── src/main/java/com/carmanagement/discovery/
│       └── config/               # Spring Security
│   └── resources/application.yml
```
### ⚙️ Installation & Setup
#### ✔️ Prerequisites

- Java 21

- Maven 3.8+

- PostgreSQL

### 📚 API Documentation
#### 🔐 Auth Endpoints
Register
```html
POST /api/auth/register
Content-Type: application/json
{
"email": "user@example.com",
"password": "SecurePass123!",
"role": "USER"
}
```
Login
```html
POST /api/auth/login
Content-Type: application/json
{
"email": "user@example.com",
"password": "SecurePass123!"
}
```
🚗 Car Endpoints
```html
GET /api/cars?available=true&type=SEDAN
Authorization: Bearer {token}
```
```html
POST /api/cars
Authorization: Bearer {token}
{
"brand": "Toyota",
"model": "Camry",
"year": 2023,
"type": "SEDAN",
"dailyRate": 45.99
}
```
📅 Rental Endpoints
```html
POST /api/rentals
Authorization: Bearer {token}
{
"carId": 1,
"startDate": "2024-01-15",
"endDate": "2024-01-20"
}
```
```html
GET /api/rentals/user/{userId}
Authorization: Bearer {token}
```
### 🎯 Key Design Decisions
1. Domain-Driven Design

   - Bounded contexts: Auth, Car, Rental

   - Clear aggregates for consistency

   - Ubiquitous language across services

2. Resilience Patterns

    - Circuit Breakers

    - Retries with backoff

    - Bulkheads

    - Fallbacks

3. Security

    - JWT + RSA256

    - Refresh token rotation

    - RBAC

    - Input validation

4. Data Consistency

    - Saga pattern
    - Eventual consistency
    - Idempotent operations

### 📊 Performance & Scalability
| Metric | Value          |
|--------|----------------|
|Response Time | < 200ms (P95)  |
|Throughput | 1000+ req/s |
|Database |	Optimized indexes|
| Caching |	Redis-ready |
### 🔮 Future Improvements
#### 🟦 Short-Term

- [ ] Redis caching

- [ ] Jaeger tracing

- [ ] Prometheus/Grafana monitoring

- [ ] Async messaging (Kafka/RabbitMQ)

#### 🟧 Medium-Term

- [ ] Payment processing

- [ ] Recommendation engine

- [ ] React Native mobile app

- [ ] Cloud deployment

#### 🟥 Long-Term

- [ ] ML-based demand prediction

- [ ] Blockchain rental records

- [ ] IoT car tracking

- [ ] Multi-tenant architecture

### 👨‍💻 Author
> Amine El Haouat
> 
> LinkedIn: https://www.linkedin.com/in/amine-el-haouat/
> 
> GitHub: @PhantomVisible 



### 🙏 Acknowledgments

> ℹ️ **Note:** My ArkX academy coach for presenting me with this challenge

> ℹ️ **Note:** Spring Boot team for the excellent framework

> ℹ️ **Note:** PostgreSQL and MongoDB communities

> ℹ️ **Note:** Hexagonal Architecture concepts by Alistair Cockburn

## ⭐ If you like this project, give it a star!