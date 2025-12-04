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

## Getting Started

### Prerequisites
* JDK 21 or higher installed.
* Maven installed (or use the included `mvnw` wrapper).

### Running the Application
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/vehicle-service.git](https://github.com/tirsong/vehicle-service.git)
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