# Java + Spring Boot Style Guide

## General Principles

- Use Spring Boot annotations and conventions to minimize boilerplate code
- Prefer Java 17+ features and syntax for modern, clean code
- Follow SOLID principles and clean architecture patterns
- Keep classes focused and single-purpose
- Use dependency injection rather than direct instantiation
- Favor composition over inheritance
- Use consistent naming and coding conventions

## Project Structure

- Follow standard Maven/Gradle project structure
- Organize code in packages by feature/domain rather than technical layers
- Use meaningful package names (`com.company.project.feature`)
- Separate business logic from infrastructure concerns

## Classes

- Use PascalCase for class names
- One class per file
- Follow standard naming conventions:
  - Service classes end with `Service`
  - Repository classes end with `Repository`
  - Controller classes end with `Controller`
  - Entity classes represent domain objects without suffixes

## Spring Components

- Use constructor injection instead of field injection
- Annotate classes appropriately: `@Service`, `@Repository`, `@Controller`, etc.
- Keep controllers thin - delegate business logic to services
- Use Spring's stereotypes consistently

## REST Controllers

- Use RESTful conventions for endpoint URLs
- Group related endpoints in the same controller
- Use appropriate HTTP methods (GET, POST, PUT, DELETE)
- Return proper HTTP status codes
- Validate request parameters and bodies
- Document APIs with OpenAPI/Swagger annotations

## Data Layer

- Use Spring Data repositories for database access
- Define clear entity relationships
- Use appropriate JPA annotations
- Create DTOs for transferring data between layers
- Avoid exposing entities directly to the web layer

## Exception Handling

- Create custom exceptions for business-specific errors
- Use `@ControllerAdvice` for global exception handling
- Return consistent error responses
- Log exceptions appropriately

## Configuration

- Use application.properties/application.yml for configuration
- Create separate config files for different environments
- Externalize sensitive information (credentials, API keys)
- Use Spring profiles for environment-specific configuration

## Testing

- Write unit tests for service and utility classes
- Write integration tests for repositories and controllers
- Use MockMvc for testing REST endpoints
- Use appropriate testing annotations (`@SpringBootTest`, `@WebMvcTest`, etc.)
- Organize test classes to mirror the structure of production code

## Lombok

- Use Lombok to reduce boilerplate code
- Prefer `@Getter`/`@Setter` over manually written methods
- Use `@RequiredArgsConstructor` for constructor injection
- Be consistent with Lombok usage throughout the codebase

## Code Quality

- Use static analysis tools (SonarQube, Checkstyle)
- Follow consistent code formatting
- Keep methods small and focused
- Use meaningful variable and method names

## Performance Considerations

- Use caching where appropriate
- Be aware of N+1 query problems
- Use pagination for large result sets
- Consider async processing for long-running operations

## Security

- Use Spring Security for authentication and authorization
- Never store sensitive information in plain text
- Validate and sanitize all user inputs
- Protect against common vulnerabilities (XSS, CSRF, SQL injection)

## Logging

- Use SLF4J for logging
- Define appropriate log levels
- Include relevant context in log messages
- Avoid logging sensitive information
