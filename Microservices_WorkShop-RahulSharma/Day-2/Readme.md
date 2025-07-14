# Core Concept Questions

---

## 1️⃣ What is the purpose of `bootstrap.yml` in a config client?

`bootstrap.yml` is loaded **before** `application.yml`. 

Its main purpose is to configure properties needed to **bootstrap** the application context,  
such as the **Config Server URL**, discovery service info, and encryption keys.  
This ensures that your app can **fetch external configuration** before it starts.

---

## 2️⃣ How does `@RefreshScope` work and when should it be used?

`@RefreshScope` tells Spring to **recreate** a bean when a `refresh` event happens.  
Use it on beans whose properties can change dynamically (e.g., config properties from Config Server).  
Combined with Spring Cloud Bus, you can refresh configurations **without restarting** the service.

---

## 3️⃣ What’s the difference between static and dynamic route configuration in Gateway?

- **Static routes**: Defined in `application.yml` or `bootstrap.yml`. Loaded at startup.
- **Dynamic routes**: Can be updated at runtime, usually from Config Server or a DB.  
  Dynamic routing lets you **change routes without redeploying** the Gateway.

---

## 4️⃣ How does rate limiting in Spring Cloud Gateway work internally?

Rate limiting uses **filters** and a **token bucket algorithm** (or Redis).  
When a request arrives, the filter checks if the request exceeds allowed limits.  
State is often stored in Redis for distributed rate limiting across multiple Gateway instances.

---

## 5️⃣ How do you test a configuration change without restarting services?

- Update the config in **Config Server** (e.g., Git).
- Send a `POST` to `/actuator/refresh` (with `@RefreshScope` in use).
- Or use **Spring Cloud Bus** to broadcast a refresh event to all instances automatically.

---

## 6️⃣ What’s the difference between global and per-route filters in Gateway?

- **Global filters**: Applied to **all routes** automatically (e.g., logging, security).
- **Per-route filters**: Applied to **specific routes only**.  
  You can chain multiple filters for custom behavior per route.

---

## 7️⃣ How does Spring Cloud Bus enhance dynamic config refresh?

Spring Cloud Bus uses a **message broker** (like Kafka or RabbitMQ) to broadcast config changes.  
When a config is updated, Bus sends a `RefreshRemoteApplicationEvent` to all instances,  
so you don’t need to hit `/actuator/refresh` on each service manually.

---

## 8️⃣ How is JWT verified at the Gateway level?

The Gateway uses a filter (like `AuthenticationFilter`) to:
- Extract the JWT token from headers.
- Verify the signature with the public key.
- Validate claims (expiry, audience, etc.).
- Forward the request with valid authentication context.

---

## 9️⃣ What’s the role of service discovery when routing via Gateway?

Service discovery (e.g., Eureka, Consul) lets the Gateway find available **service instances dynamically**.  
Instead of hardcoding backend URLs, the Gateway queries the registry to route requests to healthy instances.

---

## 🔟 Why prefer centralized config and not embedded `.yml`?

Centralized config:
- Ensures **consistency** across environments and services.
- Enables **dynamic updates** without redeploying services.
- Reduces risk of config drift.
- Easier to manage secrets (via vault/encryption).

Embedded `.yml` is static, requires rebuilding/redeploying for every change.

---

