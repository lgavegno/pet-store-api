# 🐾 Pet Store API

A comprehensive Spring Boot application for managing pets and their owners with JWT-based authentication and authorization.

## 🚀 Features

- **RESTful API** with Spring Boot
- **JWT Authentication** with Spring Security
- **Docker** support with MySQL
- **OpenAPI 3.0** Documentation
- **Actuator** for monitoring and metrics
- **Unit & Integration Tests** with JUnit 5 and Mockito
- **Logging** with SLF4J and Logback
- **CORS** enabled
- **H2** in-memory database for testing
- **MySQL** for production

## �️ Technologies

- **Java 17**
- **Spring Boot 3.x**
- **Spring Security** with JWT
- **Spring Data JPA** with Hibernate
- **Lombok**
- **MapStruct**
- **SpringDoc OpenAPI**
- **JUnit 5** & **Mockito**
- **Docker** & **Docker Compose**
- **MySQL 8.0**
- **H2 Database** (for testing)

## 📦 Prerequisites

- JDK 17 or later
- Maven 3.6+
- Docker & Docker Compose (for containerized deployment)
- MySQL 8.0 (for local development without Docker)

## 🚀 Getting Started

### Local Development (Without Docker)

1. **Clone the repository**
   ```bash
   git clone https://github.com/lgavegno/pet-store-api.git
   cd pet-store-api
   ```

2. **Configure the database**
   - Create a MySQL database named `petstore`
   - Update `application.properties` with your database credentials

3. **Build and run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Actuator: http://localhost:8080/actuator

### Docker Deployment

1. **Build and start containers**
   ```bash
   # Make the script executable (Linux/Mac)
   chmod +x docker-build-and-run.sh
   
   # Start the application
   ./docker-build-and-run.sh start
   ```

2. **Stop the application**
   ```bash
   ./docker-build-and-run.sh stop
   ```

3. **View logs**
   ```bash
   ./docker-build-and-run.sh logs
   ```

## 🔒 Authentication

The API uses JWT for authentication. To access protected endpoints:

1. Register a new user:
   ```http
   POST /api/v1/auth/register
   Content-Type: application/json
   
   {
     "firstname": "John",
     "lastname": "Doe",
     "email": "john.doe@example.com",
     "password": "password123"
   }
   ```

2. Login to get a token:
   ```http
   POST /api/v1/auth/authenticate
   Content-Type: application/json
   
   {
     "email": "john.doe@example.com",
     "password": "password123"
   }
   ```

3. Use the token in subsequent requests:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

## 📚 API Documentation

API documentation is available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify -Pintegration-test
```

### Run All Tests with Coverage
```bash
mvn clean verify jacoco:report
```

## � Monitoring

Spring Boot Actuator endpoints are available at `/actuator`:
- Health: `/actuator/health`
- Metrics: `/actuator/metrics`
- Info: `/actuator/info`

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/mycompany/petstore/
│   │   ├── config/           # Configuration classes
│   │   ├── controller/       # REST controllers
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── exception/        # Exception handling
│   │   ├── model/            # JPA entities
│   │   ├── repository/       # JPA repositories
│   │   ├── security/         # Security configuration
│   │   ├── service/          # Business logic
│   │   └── PetStoreApplication.java
│   └── resources/
│       ├── application.properties
│       └── application-docker.yml
└── test/                     # Test classes
```

## 📝 Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | Database URL | `jdbc:mysql://localhost:3306/petstore` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `petuser` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `petpassword` |
| `JWT_SECRET` | Secret key for JWT | Random UUID |
| `JWT_EXPIRATION` | JWT expiration time in ms | `86400000` (24h) |

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot Team
- All Contributors
- Original project by TODOCODE
