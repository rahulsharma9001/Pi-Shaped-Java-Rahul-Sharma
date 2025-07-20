# Day - 4 : Spring Boot Resilience4j Payment Service

This project demonstrates how to build a **fault-tolerant payment API** using **Spring Boot** with **Resilience4j** features like `@Retry` and `@TimeLimiter`.

---

## 📌 What this project does

When building distributed microservices, remote calls (e.g., payment gateways, external APIs) can fail or hang indefinitely due to network issues or provider problems.  
This project shows how to handle:

- **Temporary failures** → with **automatic retries**
- **Slow responses** → with **timeouts and fallbacks**
- **Future improvements** → with a **Circuit Breaker** to stop hammering an unhealthy service

---

## ⚡ Technologies used

- Spring Boot
- Resilience4j (`@Retry`, `@TimeLimiter`)
- CompletableFuture for async calls
- Java 17+

---

## ✅ Why I built this (real-world example)

From my own experience building payment integrations, I know how often third-party payment providers (like Stripe, Razorpay, or PayPal) can sometimes be slow or temporarily unavailable.  
For example:

> - A customer clicks **“Pay Now”** on an e-commerce site.  
> - The backend calls the payment provider.  
> - But what if the provider is down **just for a moment**? Or stuck **forever waiting**?  
> - Without proper resilience, this means failed payments and frustrated customers.

So, in this project:
- I **simulate random failures** to see how `@Retry` automatically gives them another chance.
- I **simulate random delays** to see how `@TimeLimiter` cancels calls that hang too long.
- I use a **fallback** to make sure the user always gets a friendly message instead of a crash.

---

## 🔄 Why add a **Circuit Breaker**

While `@Retry` and `@TimeLimiter` handle **temporary blips**, what if an external service is **down for a long time**?

- **Retries alone can make it worse** — your service keeps retrying, making the failing service even slower.
- This can overwhelm your system and waste resources.

A **Circuit Breaker** solves this by:
- **Monitoring failures** and **opening the circuit** if failures cross a threshold.
- While open, it **blocks calls instantly**, returning a fallback right away.
- After some time, it **tests the service again** to see if it’s healthy.

This protects the system from cascading failures and keeps it **resilient at scale**.

---

## 🗂️ Project structure

```aiignore
/Microservices_WorkShop-RahulSharma/Day-4/
│
├── src/main/java/com/example/payment/
│ ├── PaymentApplication.java # Main Spring Boot app
│ ├── controller/PaymentController.java 
│ ├── service/PaymentService.java 
│
├── src/main/resources/
│ ├── application.yml
│
├── pom.xml
└── README.md
```

---

## 🚀 How to run

1. Clone the repo:
   ```bash
   git clone https://github.com/rahulsharma9001/Pi-Shaped-Java-Rahul-Sharma.git
   ```
   ```
   cd /Microservices_WorkShop-RahulSharma/Day-4/
   ```
   
2. Run it:
```shell
   ./mvnw spring-boot:run
```

# 🔗 Endpoints to test

| Method | URL                                         | Description                                                      |
| ------ | ------------------------------------------- | ---------------------------------------------------------------- |
| `GET`  | `http://localhost:8080/api/payment/process` | Triggers the payment flow with random success, failure, or delay |
| `GET`  | `http://localhost:8080/api/payment/health`  | (Optional) Health check                                          |

When you hit **/api/payment/process** multiple times:

- Sometimes it fails → you’ll see retry attempts in logs.

- Sometimes it hangs → you’ll see the timeout fallback kick in.

- Sometimes it succeeds → all good!

Check your console to see the logs for retries, timeouts, and fallbacks.



# 📌 Core Concept Questions & Answers

---

## What problem does each of the following solve: **Retry, TimeLimiter, Bulkhead**?
- Retry: Handles transient failures (e.g., temporary network glitches or timeouts) by automatically re-invoking a failed operation a configurable number of times before giving up.

- TimeLimiter: Prevents calls from hanging indefinitely by enforcing an upper time limit for execution. If the limit is exceeded, a timeout exception is thrown and fallback logic can be triggered.

- Bulkhead: Isolates resources by limiting concurrent calls to a service or method, preventing failures in one part of the system from exhausting shared resources and impacting others.

---
## What happens when @Retry and @TimeLimiter are combined?
When used together, @Retry retries the failed call according to its policy each time a timeout occurs due to @TimeLimiter. So, if a call exceeds the time limit, it can be retried until the retry limit is reached or succeeds within the time limit.

## How does @Bulkhead protect your system under heavy load?
@Bulkhead limits the number of concurrent calls to a service, isolating failures and controlling resource usage. If the limit is reached, additional calls are rejected immediately. This keeps critical system resources like threads or connections from being exhausted by one overloaded part of your system.

## Why is @TimeLimiter only applicable on async return types?
@TimeLimiter works by running the protected call in a separate thread and enforcing a timeout. This requires the method to return an asynchronous type like CompletableFuture. It can’t enforce timeouts on blocking synchronous methods because there’s no non-blocking way to interrupt the thread that’s executing them.

## How can actuator metrics help visualize failures and retries in production?
Spring Boot Actuator exposes metrics like retry attempts, failures, bulkhead states, and circuit breaker states. By connecting these metrics to a dashboard (e.g., Prometheus + Grafana), you can monitor:

- How often retries happen

- How often fallbacks are triggered

- Bulkhead queue usage and rejections

This helps us to identify unstable dependencies and tune your resilience configuration.

## How do you test the fallback path effectively?
- Use unit tests to simulate failures or timeouts by mocking dependencies to throw exceptions.

- Verify that the fallback logic is executed by asserting the fallback response.

- Use tools like @SpringBootTest with mocks or Mockito to simulate different failure scenarios.

## What would you adjust if the service is retrying too aggressively?
- Increase the wait duration or add backoff (exponential backoff) between retries.

- Reduce the maximum number of retry attempts.

- Revisit the exception types configured for retry — make sure you’re not retrying non-transient errors.

## How would you simulate latency or failure in a unit test?
- Use mocks to delay responses (e.g., Thread.sleep() in a mocked method).

- Throw exceptions from the mocked method to simulate errors.

- Use CompletableFuture with a delayed completion to test @TimeLimiter.

## What’s the risk of retrying on exceptions like IllegalArgumentException?
**IllegalArgumentException** typically indicates a programming error or bad input — not a transient issue. Retrying won’t fix it and can waste resources, introduce latency, or cause side effects. Retry should only target transient, recoverable failures.

## How can you visualize open/closed states using actuator endpoints?
Actuator exposes endpoints like **/actuator/health** or **/actuator/circuitbreakers** which show the current state of resilience patterns (e.g., circuit open/closed, bulkhead stats). Integrating this with dashboards makes it easy to monitor system health in real-time.