# Proposal App

This is a microservices application for managing financial proposals with messaging support.

## Table of Contents

- [Proposal App](#proposal-app)
  - [Table of Contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Features](#features)
  - [Technologies](#technologies)
  - [Architecture](#architecture)
  - [Installation](#installation)
  - [Usage](#usage)
  - [API Documentation](#api-documentation)
  - [Contributing](#contributing)

## Introduction

The Proposal App is a robust microservices-based application for creating and managing financial proposals. It integrates with other services through RabbitMQ messaging and provides real-time updates via WebSockets.

## Features

- Create and manage financial proposals
- Credit analysis integration with priority queue system
- Real-time notifications via WebSockets
- RESTful API with detailed documentation
- Robust error handling and validation
- Automatic retry mechanism for failed message delivery

## Technologies

The following technologies are used in this project:

- Java 21
- Spring Boot 3.4
- Spring AMQP (RabbitMQ)
- Spring WebSocket
- Spring Data JPA
- PostgreSQL
- Docker and Docker Compose
- Swagger/OpenAPI for API documentation

## Architecture

The application follows a microservices architecture:

1. **Proposal App** (this service) - Manages proposals and acts as the entry point for users
2. **Credit Analysis App** - Processes proposals and approves/denies based on financial criteria  
3. **Notification App** - Sends notifications to users about proposal status changes

Communication between services is managed via RabbitMQ message exchanges and queues, with WebSockets providing real-time updates to clients.

## Installation

To run the Proposal App locally, follow these steps:

1. Clone the repository:

    ```shell
    git clone https://github.com/leonardomeirels55/proposal-app.git
    ```

2. Configure the application properties:

    Open the `application.properties` file located in `src/main/resources` and update the database connection details and RabbitMQ configuration according to your environment or env in `docker-compose`.

3. Run the application:

    ```shell
    docker compose up
    ```

    This will start all required services (PostgreSQL, RabbitMQ, and the microservices).

## Usage

Once the application is running, you can access the API endpoints using a tool like Postman or cURL. Here are some example requests:

- Create a new proposal:
    ```http
    POST /api/v1/proposals
    Content-Type: application/json
    ```
    ```json
    {
      "name": "Leonardo",
      "lastName": "Meireles",
      "telephone": "5599999999",
      "cpf": "111.111.111-11",
      "financialIncome": 5000.0,
      "proposalValue": 10000.0,
      "paymentTerm": 24
    }
    ```

- Get all proposals:
    ```http
    GET /api/v1/proposals
    ```

- Get a specific proposal:
    ```http
    GET /api/v1/proposals/{id}
    ```

## API Documentation

The API documentation is available via Swagger UI at:

```
http://localhost:8080/api/v1/swagger-ui.html
```

## Contributing

Contributions are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request.
