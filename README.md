# Inventory Management API

A robust, production-ready backend module for inventory management built with Java, Spring Boot, and PostgreSQL. 
This project is fully containerized using Docker and Docker Compose, allowing seamless setup and deployment across different environments. 
It includes global exception handling, data validations, and a comprehensive suite of unit tests.

---

## Tech Stack & Tools

* **Backend Framework:** Java 17, Spring Boot 3
* **Database:** PostgreSQL
* **Database Tooling:** Hibernate ORM, pgAdmin
* **Testing Suite:** JUnit 5, Mockito
* **Containerization:** Docker, Docker Compose
* **Dependency Management:** Maven

---

## Environment Variables Setup

To secure sensitive credentials, the real `.env` file is excluded from version control. 
If you want to run this project locally, you must create a `.env` file in the root directory based on the following template:

### .env.example

```
# Database Credentials
POSTGRES_DB=inventory_db
POSTGRES_USER=your_postgres_user
POSTGRES_PASSWORD=your_secure_password
POSTGRES_PORT=5432

# Spring Boot Configuration
SPRING_LOCAL_PORT=8080
DOCKER_DB_HOST=postgres-db
```

**Important:** Replace `your_postgres_user` and `your_secure_password` with your own configuration before starting the application.

---

## Deployment with Docker Compose

This architecture is completely containerized. You do not need Java, Maven, or PostgreSQL installed on your machine—only Docker.

1. **Spin up the containers**
  From the project root directory, run the following command to build the image and start the services (API and Database):

  ```bash
  docker compose up --build
  ```
2. **Stop the application**
  ```bash
  docker compose down
  ```
**Note:** A named volume (`postgres_data`) is used to ensure your database records persist on your local drive even when containers are destroyed.

---

## Running Unit Tests

The backend includes a comprehensive suite of unit and service-layer tests implemented with JUnit 5 and Mockito. 
You can execute these tests within an isolated Docker environment without installing local dependencies.

### Run tests on-demand
While the architecture is active or stopped, run this command to trigger the testing suite inside a temporary container:

```bash
docker compose run --rm api-service mvn test
```

The `--rm` flag ensures that the testing container is safely removed after printing the test results to your terminal.

---

## How to Test the Inventory API (API Guide)

Once the application is running via Docker (`http://localhost:8080`), you can test the REST endpoints using Postman, Insomnia, or `curl`.

1. **Check for Existing Products (GET)**
   * **Endpoint:** `GET http://localhost:8080/api/v1/products`
   * **Expected Response (First run):** `[]` (An empty array, confirming a clean, successful database connection).

2. **Create a Product (POST)**
   * **Endpoint:** `POST http://localhost:8080/api/v1/products`
   * **Headers:** `Content-Type: application/json`
   * **Body Example:**
     ```json
     {
      "uniqueCode": "PROD-001",
      "name": "Gaming Monitor 27'",
      "description": "144Hz IPS display",
      "actualStock": 15,
      "minimumStock": 3,
      "price": 299.99
     }
     ```
   * **Expected Response:** `201 Created` along with the persisted object containing its database ID and automatic timestamps.

3. **Verify Persistence (GET)**
   * **Endpoint:** `GET http://localhost:8080/api/v1/products`
   * **Expected Response:** You will now receive an array containing the product you just created.

4. **Validation & Global Error Handling**
   The API includes a global exception handler. If you attempt to send an invalid payload (e.g., negative stock, missing product code, or empty name),
   the API will reject the request cleanly with a custom `400 Bad Request `structure detailing the validation errors.
