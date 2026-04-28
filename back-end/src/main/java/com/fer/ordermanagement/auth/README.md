# Auth Domain

Handles user authentication, registration, and JWT-based security for the entire application.

## Responsibilities

- User registration with role assignment
- Login with credential validation
- JWT token generation and verification
- Spring Security integration (filter chain, user details)

## Structure

```
auth/
├── controller/
│   ├── api/AuthApi.java          # API interface with OpenAPI annotations
│   └── AuthController.java       # REST controller implementation
├── dto/
│   ├── LoginRequest.java         # Login credentials (username, password)
│   ├── RegisterRequest.java      # Registration data (username, email, password)
│   └── AuthResponse.java         # JWT token + user info response
├── entity/
│   ├── User.java                 # User entity (username, email, password, status)
│   └── Role.java                 # Role entity (role name)
├── enums/
│   ├── RoleName.java             # STAFF, ADMIN
│   └── UserStatus.java           # Account status values
├── repository/
│   ├── UserRepository.java
│   └── RoleRepository.java
├── security/
│   ├── SecurityConfig.java       # Spring Security filter chain configuration
│   ├── JwtUtil.java              # JWT creation, parsing, and validation
│   ├── JwtAuthFilter.java        # OncePerRequestFilter for JWT authentication
│   ├── UserDetailsImpl.java      # Spring Security UserDetails implementation
│   └── UserDetailsServiceImpl.java
└── service/
    └── AuthService.java          # Login & registration business logic
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/login` | Authenticate user, returns JWT token |
| `POST` | `/auth/register` | Register new account |

## Key Behaviors

- Passwords are hashed with BCrypt before storage
- JWT tokens are issued upon successful login/registration
- `JwtAuthFilter` intercepts every request to validate the token
- Roles (`STAFF`, `ADMIN`) are used for endpoint-level access control via `@PreAuthorize`

## Dependencies

- **Outbound**: None (foundational module)
- **Consumed by**: All other secured modules rely on Auth for authentication context
