# Spring Boot-Based Payroll System Microservices Project

## Overview

This project is a microservices-based payroll system implemented in Java with Spring Boot. It is designed for scalability and maintainability, allowing each service to be deployed independently.

## Main Services

The project includes the following core services:

- Employee Service: Manages employee data such as name, CPF, base salary, and job position.
- Income Tax Service: Calculates income tax based on employee salaries.
- INSS Tax Service: Calculates INSS (Brazilian Social Security) tax contributions based on employee salaries.
- Work Attendance Publish Service: Publishes employee attendance records to the Payroll Service.
- Payroll Service: Processes payroll calculations based on attendance data.
- API Gateway: Routes requests to services and acts as an OAuth2 resource server for authorization.
- Authorization Server (Keycloak): Manages user authentication.
- Discovery Server: A Eureka server for service discovery, used in Docker Compose deployments and local development.

## Kafka Integration and Dead Letter Queue

The Payroll System uses Apache Kafka for asynchronous communication between services, enabling efficient, decoupled event processing. The Work Attendance Publish Service sends attendance records to Kafka, which the Payroll Service consumes for payroll calculation.

### Key Components

- Attendance Topic: Stores attendance data published by the Work Attendance Publish Service.
- Dead Letter Queue (DLQ): Captures messages that fail to process after multiple retry attempts for further investigation and reprocessing.

### Idempotent Payroll Processing and Retry Mechanism

The Payroll Service is an idempotent consumer, meaning it can process the same message more than once without creating duplicate payroll records. It achieves this by:

- Checking for existing payroll records before processing
- Retrying failed messages a set number of times
- Sending unprocessed messages to the DLQ if retries fail

This approach ensures fault tolerance, reliability, and data consistency, making the system robust against temporary failures.

## Features

Key features of the project include:

- Unit and integration testing
- Comprehensive error handling
- Data validation
- Clean code and SOLID principles
- Data mapping using JPA
- API documentation with Swagger
- OAuth2-based authorization
- Asynchronous communication via Apache Kafka
- Kubernetes deployment configuration
- Docker Compose for local deployments
- Monitoring and metrics collection
- Service discovery with Eureka
- PostgreSQL as the database
- Containerized testing with Testcontainers

## Key Spring Projects and Dependencies

The project leverages various Spring components and dependencies:

- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Actuator
- Spring Security
- Spring Cloud Netflix
- Spring Cloud Gateway
- Spring Cloud OpenFeign
- Spring Kafka

## Technologies Used

- **Languages**: Java, with Spring framework for all services
- **Containerization**: Docker for both development and production
- **Orchestration**: Docker Compose and Kubernetes for managing and deploying microservices
- **Monitoring**: Prometheus for metrics and alerts
- **Dashboards**: Grafana for visualizing metrics
- **Database**: PostgreSQL
- **Scripting**: Shell scripts

## Getting Started With Kubernetes

For details on deploying this project with Kubernetes, refer to [this documentation](./kubernetes/README.md) in the `kubernetes` directory.

## Getting Started with Docker Compose

Follow these steps to deploy the project locally with Docker Compose.

### Step 1: Build Docker Images

Run the script located in the scripts directory to build the Docker images.

```sh
# Build images with tag 1.0
./scripts/docker-build-compose.sh 1.0
```

### Step 2: Create a `.env` file

Generate a .env file from the provided template, and customize any necessary credentials.

```sh
cp .env.example .env
```

### Step 3: Deploy the Project with Docker Compose

Start the services using Docker Compose.

```sh
docker compose up -d
```

### Step 4: Configure Hostname Entries in `/etc/hosts` for Keycloak and API

To ensure that Keycloak works correctly, make sure both the client (e.g., Postman) and the resource server (API Gateway) access Keycloak using the same hostname. Since the API Gateway uses the Docker Compose service name `keycloak`, we’ll map this hostname to `127.0.0.1` (localhost) in the `/etc/hosts` file.

- **On Linux/Mac**: Edit `/etc/hosts`
- **On Windows**: Edit `C:\Windows\System32\drivers\etc\hosts`

Optionally, you can also map the api hostname for easier testing.

```sh
# /etc/hosts (Linux/Mac) or C:\Windows\System32\drivers\etc\hosts (Windows)
127.0.0.1 api
127.0.0.1 keycloak
```

### Step 5: Create a Keycloak realm

Once the project is running, access Keycloak at `https://keycloak:8443`. Note that the connection uses a self-signed certificate, so you may see a browser security warning. For production, use a certificate from a trusted authority.

In the Keycloak admin panel, log in with the credentials from the `.env` file and create a realm named `payroll-system`. Go to the realm settings and update the "Frontend URL" to `https://keycloak:8443`.

You can then create a client and test users for the application. Refer to the Keycloak documentation for more details.

