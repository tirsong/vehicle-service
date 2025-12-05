# Vehicle Service API

A Spring Boot REST API for managing vehicle records. This service provides full CRUD capabilities, input validation, exception handling, and persistence using an H2 in-memory database.

## Table of Contents
* [Overview](#overview)
* [Architecture](#architecture)
* [Technologies](#technologies)
* [Getting Started](#getting-started)
* [API Endpoints](#api-endpoints)
* [Error Handling](#error-handling)
* [Testing](#testing)

## Overview
The **Vehicle Service** is designed to track vehicle inventory. It allows users to:
* Create new vehicle records with unique VINs.
* Retrieve lists of all vehicles or specific vehicles by VIN.
* Update existing vehicle details (price, description, etc.).
* Delete vehicles from the inventory.
* Enforce strict data validation (e.g., non-negative prices, required fields).

## Architecture
The application follows a standard **Layered Architecture**:
1.  **Controller Layer (`VehicleController`):** Handles incoming HTTP requests and responses.
2.  **Service Layer (`VehicleService`):** Contains business logic (e.g., checking for duplicate VINs).
3.  **Repository Layer (`VehicleRepository`):** Manages data access using Spring Data JPA.
4.  **Domain Layer (`Vehicle`):** Defines the data entity and validation rules.

## Technologies
* **Java 21**
* **Spring Boot 4.0.0** (Web, Data JPA, Validation)
* **H2 Database** (In-memory persistence)
* **Lombok** (Boilerplate reduction)
* **Maven** (Dependency management)
* **SpringDoc OpenAPI** (Swagger UI Documentation)

## Getting Started

### Prerequisites
* JDK 21 or higher installed.
* Maven installed (or use the included `mvnw` wrapper).

### Running the Application
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/tirsong/vehicle-service.git](https://github.com/tirsong/vehicle-service.git)
    cd vehicle-service
    ```
2.  **Build the project:**
    ```bash
    ./mvnw clean package
    ```
3.  **Run the server:**
    ```bash
    ./mvnw spring-boot:run
    ```
4.  **Access the API:** The server will start on `http://localhost:8080`.

## API Documentation (Swagger)

This application includes built-in API documentation using Swagger UI.

Once the application is running, you can access the interactive documentation at:
`http://localhost:8080/swagger-ui/index.html`

This interface allows you to:
* Visualize all available endpoints.
* See expected request bodies and response schemas.

## Developer Documentation (Javadoc)
To generate the full technical documentation (HTML) for this project, run the following command:

```bash
./mvnw javadoc:javadoc
```

## Database Access (H2 Console)

This application uses an in-memory H2 database. You can access the database console directly to verify data or run SQL queries.
* **URL:** `http://localhost:8080/h2-console`
* **Driver Class:** `org.h2.Driver`
* **JDBC URL:** `jdbc:h2:mem:vehicledb`
* **Username:** `sa`
* **Password:** `password`  

*Note: Since this is an in-memory database, all data will be lost when the application stops.*

## API Endpoints
Base URL: `/vehicle` (Note: Singular path based on current implementation).

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| **GET** | `/vehicle` | Retrieve all vehicles | 200 OK |
| **GET** | `/vehicle/{vin}` | Retrieve a specific vehicle | 200 OK, 404 Not Found |
| **POST** | `/vehicle` | Create a new vehicle | 201 Created, 409 Conflict, 422 Unprocessable |
| **PUT** | `/vehicle/{vin}` | Update an existing vehicle | 200 OK, 404 Not Found, 422 Unprocessable |
| **DELETE** | `/vehicle/{vin}` | Delete a vehicle | 204 No Content |

## Error Handling
The API includes a `GlobalExceptionHandler` to return consistent JSON errors:

* **400 Bad Request:** Malformed JSON syntax.
* **409 Conflict:** Attempting to create a vehicle with a VIN that already exists.
* **422 Unprocessable Entity:** Validation failures (e.g., negative price, missing VIN).

**Example Error Response:**
```json
{
    "horsePower": "Horsepower must be greater than 0",
    "purchasePrice": "Price cannot be negative"
}
```

## Testing
You can verify the API using cURL, Postman, or the built-in Swagger UI.
For more comprehensive testing, refer to TESTING.md.

Quick Test (Create Vehicle):
```bash
curl -v -X POST http://localhost:8080/vehicle \
-H "Content-Type: application/json" \
-d '{
"vin": "INVALID-DATA-VIN",
"manufacturerName": "Unknown",
"modelName": "FailMobile",
"description": "This should fail validation",
"horsePower": -100,
"purchasePrice": -5000.00,
"fuelType": "Gasoline"
}'
```