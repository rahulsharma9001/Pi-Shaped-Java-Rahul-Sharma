# 🚀 Spring Cloud Microservices: Eureka + Gateway + Load Balancing

This project demonstrates:
- A **Spring Cloud Eureka Server** for service discovery
- **User Service** & **Order Service** registered with Eureka
- A centralized **API Gateway** using `lb://` URIs for load-balanced routing
- Health checks with Spring Boot Actuator

---

## 📂 Project Structure

```
Day-3/
└── spring-cloud-microservices/
├── README.md
├── eureka-server/
│   ├── src/main/java/com/example/eurekaserver/EurekaServerApplication.java
│   ├── src/main/resources/application.yml
│   ├── pom.xml
│
├── user-service/
│   ├── src/main/java/com/example/userservice/UserServiceApplication.java
│   ├── src/main/java/com/example/userservice/controller/UserController.java
│   ├── src/main/resources/application.yml
│   ├── pom.xml
│
├── order-service/
│   ├── src/main/java/com/example/orderservice/OrderServiceApplication.java
│   ├── src/main/java/com/example/orderservice/controller/OrderController.java
│   ├── src/main/resources/application.yml
│   ├── pom.xml
│
├── api-gateway/
│   ├── src/main/java/com/example/apigateway/ApiGatewayApplication.java
│   ├── src/main/resources/application.yml
│   ├── pom.xml
│
└── pom.xml   (optional parent POM if multi-module)
```


---

## ⚙️ Prerequisites

```bash
   java -version    //Check Java
````

```bash
   mvn -v  //Check Maven
```
# 1. Start Eureka Server
```aiignore
cd eureka-server

# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Eureka Dashboard → http://localhost:8761/eureka
```

# 2️. Start User Service
```aiignore
cd ../user-service

# Build
./mvnw clean package

# Run first instance (default port 8081)
./mvnw spring-boot:run

# OPTIONAL: Run a second instance on port 8083 for load balancing test
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8083"
```

# 3. Start Order Service
```aiignore
cd ../order-service

# Build
./mvnw clean package

# Run (port 8082)
./mvnw spring-boot:run

```

# 4.  Start API Gateway
```aiignore
cd ../api-gateway

# Build
./mvnw clean package

# Run (port 8080)
./mvnw spring-boot:run

```

# 5. Verify Service Discovery
```aiignore
# Open Eureka Dashboard in your browser:
# http://localhost:8761/eureka

# You should see:
# - API-GATEWAY
# - USER-SERVICE (1 or more instances)
# - ORDER-SERVICE

```

# 6. Test Routing and Load Balancing
```aiignore
# Call User Service via Gateway
curl http://localhost:8080/users

# Call Order Service via Gateway
curl http://localhost:8080/orders

# Run multiple requests to check round-robin:
curl http://localhost:8080/users
curl http://localhost:8080/users
curl http://localhost:8080/users

# You should see different ports in the response if multiple instances are running.

```

# 7. Test Health Endpoints
```aiignore
# User Service health
curl http://localhost:8081/actuator/health

# Second User Service instance
curl http://localhost:8083/actuator/health

# Order Service health
curl http://localhost:8082/actuator/health

```
# Core Concept Questions

# Q1: What’s the difference between client-side and server-side service discovery?

Client-side discovery:
- The client fetches the list of available service instances from the registry (e.g., Eureka).
- It performs load balancing and picks one instance to call.
- Used in Spring Cloud Netflix Eureka with Spring LoadBalancer or Ribbon.

Server-side discovery:
- The client sends a request to a load balancer (like an API Gateway).
- The gateway queries the service registry and forwards the request to the appropriate instance.
- Example: Kubernetes services with an ingress controller.

# Q2: How does Eureka detect dead service instances?

 - Each service instance sends a heartbeat (default every 30 seconds) to Eureka.
 - If Eureka does not receive a heartbeat within a configured time (default 90 seconds), it marks the instance as DOWN and eventually removes it.

# Q3: Why is the /actuator/health endpoint important for service discovery?

 - Eureka and other registries use this endpoint to check if a service instance is healthy.
 - Only healthy instances are returned to clients.
 - You can customize the `/actuator/health` to include DB, Redis, etc., for deeper health checks.

# Q4: How does lb:// routing in Gateway work with DiscoveryClient?

 - When you write routes like `lb://user-service`, Spring Cloud Gateway uses `DiscoveryClient` to resolve the logical service name to actual instances.
 - Then, Spring LoadBalancer selects an instance to forward the request to.
 - It allows dynamic routing to services without hardcoding URLs.

# Q5: What happens when a registered service goes down?

 - The service stops sending heartbeats.
 - Eureka waits for the eviction timeout (default 90s), then marks it as unreachable.
 - Clients or gateways will stop receiving that instance in their discovery responses.

# Q6: Compare Eureka, Consul, and Kubernetes DNS-based discovery.

## Eureka:
 - Client-side discovery
 - Good for Spring Cloud ecosystem
 - Lightweight, Java-focused

## Consul:
 - Supports health checks, key-value store
 - Supports both client and server-side discovery
 - Works across platforms

## Kubernetes DNS:
 - DNS-based server-side discovery
 - Pods/services are auto-registered via kube-dns or CoreDNS
 - Tight integration with container orchestration

# Q7: How does Spring LoadBalancer choose which instance to call?

 - Uses load-balancing algorithms (default: round-robin)
 - Custom rules can be plugged in (e.g., random, weighted, zone-aware)
 - Retrieves instance list from `DiscoveryClient`, applies filters and chooses one.



