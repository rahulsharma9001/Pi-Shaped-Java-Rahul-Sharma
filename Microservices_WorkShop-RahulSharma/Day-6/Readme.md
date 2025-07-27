# 🔐 Spring Boot Security with Form Login, Role-based Access, JWT, and Log Tracing

This project demonstrates a full-stack security setup using Spring Boot, with:

- Form login and in-memory users
- Role-based access control
- Stateless authentication using JWT
- Security filters and `SecurityContextHolder`
- Global CORS configuration and CSRF control
- MDC logging with traceId and username

---

## 🚀 How to Run the Project

### ✅ Prerequisites

- Java 17 or higher
- Maven
- Postman or cURL for testing

### 🧱 Steps to Run

```bash
# Clone the repo
  git clone https://github.com/rahulsharma9001/Pi-Shaped-Java-Rahul-Sharma.git
  cd SpringSecurity
```

```bash
# Build the project
  mvn clean install
```
```bash
# Run the project
  mvn spring-boot:run
```
## 🔐 In-Memory Users

| Username | Password   | Role         |
| -------- | ---------- | ------------ |
| `admin`  | `admin123` | `ROLE_ADMIN` |
| `user`   | `user123`  | `ROLE_USER`  |

## ✅ How to Test the Application
1️⃣ Login and Get JWT for User 
```bash
curl -X POST http://localhost:8080/auth/login \
  -d "username=admin" \
  -d "password=admin123"
```

➡️ Copy the JWT token from the response.

## 2️⃣ Call Authenticated Endpoints
🔒 Access /api/user (any authenticated user)
```bash
  curl -H "Authorization: Bearer <paste_token_here>" \
    http://localhost:8080/api/user
```

🔒 Access /api/admin (only with admin token)
```bash
curl -H "Authorization: Bearer <paste_admin_token>" \
  http://localhost:8080/api/admin
```

# 📘 Core Concept Questions & Answers
## What is the difference between authentication and authorization?
- Authentication verifies who the user is.

- Authorization determines what the user is allowed to do.

## How does Spring Security handle the authentication flow for form login?
- User submits username/password via /login.

- Spring Security verifies credentials using a UserDetailsService.

- On success, it stores user details in the SecurityContext.

## How is a stateless JWT flow different from session-based auth?
- Session-based: server stores session in memory.

- JWT-based (stateless): token contains all user info and is verified on each request. Server doesn't store sessions.

## How do filters in Spring Security get executed and ordered?
- Spring Security uses a filter chain.

- Filters like UsernamePasswordAuthenticationFilter or custom JwtAuthFilter are executed in a predefined order.

- You can inject your custom filters using addFilterBefore() or addFilterAfter().
 
## What role does the SecurityContextHolder play?
It holds the currently authenticated user's Authentication object and makes it accessible throughout the app.

## Why should CSRF be disabled for JWT-based APIs?
- CSRF protection is needed for stateful browser sessions.

- JWT-based APIs are stateless and don’t rely on cookies — hence CSRF should be disabled.

## What is MDC and how does it help with audit/log tracing?
- MDC (Mapped Diagnostic Context) allows storing metadata like traceId and username per request/thread.

- This helps in correlating logs and debugging.

## What are the security risks of not validating a JWT token signature?
- An attacker can forge tokens or tamper with payloads.

- If signature validation is skipped, anyone can gain unauthorized access.

## How can you revoke or expire JWTs?
- Set expiration (exp) during token generation.

To revoke before expiry:

- Use a blacklist/token revocation store (DB or cache).

- Change secret key (forces re-login for all tokens).

## What is the purpose of enabling or customizing CORS?
CORS (Cross-Origin Resource Sharing) controls which domains can access your APIs. It is important when your frontend (e.g., React) runs on a different port/domain than your backend.



