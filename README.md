# Auth-API-Gateway

A modern reactive Spring Boot API Gateway that provides JWT-based authentication and authorization for microservices. Built with Spring Cloud Gateway and WebFlux for high-performance, non-blocking operations.

## Key Features

- **JWT Authentication** - Stateless authentication with access and refresh tokens
- **User Management** - Registration, login, and profile management
- **Reactive Architecture** - Built on Spring WebFlux for non-blocking I/O
- **Global Exception Handling** - Structured error responses across all endpoints
- **Secure Password Storage** - BCrypt hashing with 12 rounds
- **Token Refresh Mechanism** - 7-day refresh tokens for seamless user experience

## Tech Stack

- **Java 21** - LTS version
- **Spring Boot 3.5.7** - Framework
- **Spring Cloud Gateway** - Reactive gateway
- **Spring Security** - Authentication & authorization
- **PostgreSQL + R2DBC** - Reactive database access
- **JJWT 0.12.5** - JWT token handling
- **Maven** - Build tool

## Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/matthewhou19/auth-api-gateway.git
   cd auth-api-gateway
   ```

2. **Start PostgreSQL database**

   **Option A: Using Docker (Recommended)**
   ```bash
   docker compose up -d
   ```

   **Option B: Use existing PostgreSQL**

   Make sure PostgreSQL is running on `localhost:5432` with:
   - Database: `auth_gateway_db`
   - Username: `postgres`
   - Password: `password`

3. **Run the application**
   ```bash
   cd apigateway
   ./mvnw spring-boot:run
   ```

   **Note:** Database tables (`users`, `refresh_tokens`) are created automatically on startup from `schema.sql`. No manual database setup required!

4. **Access the API**
   - Base URL: `http://localhost:8080`
   - All endpoints are prefixed with `/auth`

## API Endpoints

### Public Endpoints (No authentication required)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login with email/username and password |
| GET | `/auth/refresh/{token}` | Refresh access token using refresh token |

### Protected Endpoints (Requires JWT)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/auth/me` | Get authenticated user details |
| DELETE | `/auth/logout` | Logout and invalidate refresh token |

### Example: Register a User

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"email\": \"user@example.com\", \"username\": \"johndoe\", \"name\": \"John Doe\", \"password\": \"SecurePass123@\"}"
```

**Note**: Using double quotes and escaped JSON for better shell compatibility. Password uses `@` instead of `!` to avoid shell history expansion issues in some terminals.

### Example: Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"emailUsername\": \"user@example.com\", \"password\": \"SecurePass123@\"}"
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "base64-encoded-token"
}
```

### Example: Access Protected Endpoint

```bash
curl -X GET http://localhost:8080/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Configuration

### Default Settings

The application uses the following default database settings (matching the `docker-compose.yml`):

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/auth_gateway_db
    username: postgres
    password: password
```

### Custom Configuration

For production or custom setups, modify `apigateway/src/main/resources/application.yaml`:

**Custom Database:**
```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://your-host:5432/your-database
    username: your-username
    password: your-password
```

**JWT Secret (CRITICAL for Production!):**
```yaml
jwt:
  secret: your-secure-secret-key-minimum-32-characters-long
```

⚠️ **Security Warning**: The default JWT secret is for development only. MUST be changed for production!

## Database Schema

The application automatically creates the required database tables on startup:

**Tables:**
- `users` - Stores user account information (id, name, username, email, password)
- `refresh_tokens` - Manages refresh token lifecycle (id, user_id, token, expiry_date, created_at)

**Schema Definition:**
- Table definitions are in `apigateway/src/main/resources/schema.sql`
- Tables are created automatically via Spring's SQL initialization feature
- Uses `CREATE TABLE IF NOT EXISTS` to prevent errors on restart
- Includes indexes on `refresh_tokens` for optimal performance

**Configuration:**
```yaml
spring:
  sql:
    init:
      mode: never        # Use 'always' only in dev/test profiles; handle prod schema via migrations
      platform: postgresql
```

**Note:** For production environments, use proper database migration tools like Flyway or Liquibase instead of SQL initialization mode.

## Password Requirements

- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit
- At least one special character

## Project Structure

```
apigateway/
├── src/main/java/vaultweb/apigateway/
│   ├── config/          # Security & JWT configuration
│   ├── controller/      # REST API endpoints
│   ├── service/         # Business logic
│   ├── model/           # Database entities
│   ├── repositories/    # Data access layer
│   ├── dto/             # Request/Response objects
│   ├── exceptions/      # Exception handling
│   └── util/            # Utility classes
└── src/main/resources/
    ├── application.yaml # Application configuration
    └── schema.sql       # Database schema (auto-applied on startup)
```

## License

[Add your license here]
