# QuickCart — Spring Boot E-Commerce Backend

## Overview
QuickCart is a RESTful backend application built using Java and Spring Boot. It demonstrates REST API development, database integration, request validation, and layered application architecture.

## Technology Stack
- Java 21
- Spring Boot 4.0.x
- Spring Data JPA
- PostgreSQL
- Maven
- Jakarta Bean Validation
- Git and GitHub

## Features
### User APIs
- Register a user
- Retrieve a user by ID
- User login demonstration

### Product APIs
- Create a product
- Retrieve all active products
- Retrieve a product by ID
- Filter products by category
- Update product details
- Soft-delete a product

## Architecture
The application follows a layered architecture:

- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains business logic.
- **Repository:** Performs database operations using Spring Data JPA.
- **Entity:** Represents database tables.
- **DTO:** Defines request and response structures.
- **Mapper:** Converts between entities and DTOs.

## Prerequisites
- JDK 21
- PostgreSQL
- Git

## Database Setup
Create a PostgreSQL database named `quickcart`.

Configure the following environment variables before running the application:

- `DB_USERNAME` — PostgreSQL username
- `DB_PASSWORD` — PostgreSQL password

The database configuration is managed through `src/main/resources/application.properties`.

## Run Locally
1. Clone the repository.
2. Configure the database and environment variables.
3. Open a terminal in the project directory.
4. Run the application:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

The application runs on `http://localhost:8081` by default.

## API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/users/register` | Register a user |
| GET | `/api/users/{id}` | Retrieve a user |
| POST | `/api/users/login` | Demonstration login |
| POST | `/api/products` | Create a product |
| GET | `/api/products` | List active products |
| GET | `/api/products/{id}` | Retrieve an active product |
| GET | `/api/products/category/{category}` | Filter by category |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Soft-delete a product |
