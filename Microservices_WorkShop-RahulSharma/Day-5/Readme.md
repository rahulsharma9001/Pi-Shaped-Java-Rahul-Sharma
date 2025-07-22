# 📌 Core Concepts: Observability and Monitoring

---

## ✅ Questions & Detailed Answers

### 1️⃣ What are the differences between metrics, logs, and traces?

- **Metrics:** Quantitative measurements collected over time, like CPU usage, memory consumption, request counts, error rates. Good for dashboards and alerts.
- **Logs:** Immutable, timestamped text records of discrete events (like startup info, warnings, stack traces). Useful for debugging what happened at a specific point.
- **Traces:** End-to-end records showing how a single request flows through multiple services. They break down time spent in each span, revealing bottlenecks and dependencies.

---

### 2️⃣ What’s the role of Micrometer and how does it enable backend-agnostic metrics?

- **Micrometer** is a metrics instrumentation library for JVM applications.
- It provides a simple API to collect and register metrics like timers, counters, and gauges.
- Micrometer does not care where the metrics go — you can switch from Prometheus to Datadog or New Relic without changing the instrumentation code. This makes it backend-agnostic.

---

### 3️⃣ How do \`traceId\` and \`spanId\` travel across microservice boundaries?

- Distributed tracing tools like Sleuth or Zipkin use HTTP headers to carry trace context.
- Common headers: `X-B3-TraceId` (identifies the whole trace) and `X-B3-SpanId` (identifies the current operation).
- When a request leaves one service and enters another, these headers are attached to the HTTP call so the downstream service can continue the trace.

---

### 4️⃣ How does Sleuth propagate context in WebClient/RestTemplate?

- **Spring Cloud Sleuth** automatically hooks into popular HTTP clients.
- For **RestTemplate**, Sleuth decorates the bean to inject trace headers in outgoing requests.
- For **WebClient**, Sleuth adds filters to ensure `traceId` and `spanId` flow with the request.
- This keeps the trace context consistent without manual effort.

---

### 5️⃣ What is the difference between **Timer**, **Gauge**, and **Counter** in Micrometer?

- **Counter:** A single monotonically increasing value. Example: number of requests served.
- **Gauge:** Tracks a value that can rise and fall. Example: active threads, DB connection pool usage.
- **Timer:** Measures both how many times an event happened and how long it took. Example: HTTP request duration.

---

### 6️⃣ What is MDC and how does it help with log correlation?

- **MDC** (Mapped Diagnostic Context) is a logging feature that stores contextual information for the current thread.
- Example: When a request starts, Sleuth adds `traceId` and `spanId` to MDC.
- Every log entry automatically includes this info, so you can correlate logs with traces and debug end-to-end flows easily.

---

### 7️⃣ What is the difference between structured vs unstructured logging?

- **Structured Logging:** Logs in a structured, parseable format (e.g., JSON). Makes it easy for log aggregators like Elasticsearch to index and query.
- Example: `{ "level": "ERROR", "traceId": "1234", "message": "Payment failed" }`
- **Unstructured Logging:** Free-form text logs. Good for humans but harder for machines to parse reliably.
- Structured logging is preferred for large-scale distributed systems.

---

### 8️⃣ How do you use Kibana to search for logs tied to a specific \`traceId\`?

- Open **Kibana Discover**.
- Use the search bar to filter logs by `traceId`:

  ```plaintext
  traceId:123456789abcdef
  ```
  
### 9️ How can you monitor memory, DB pool, and request error rates in Grafana?
- Use Micrometer to expose metrics for JVM memory, database pool (HikariCP), and HTTP errors.

- Visualize them with Grafana panels and set alerts:

   - JVM Memory → jvm.memory.used
    
   - DB Pool → hikaricp.connections.active
    
   - Errors → http.server.requests with status >= 5xx.
  
### 🔟 How does sampling affect Zipkin trace accuracy and performance?
- Sampling reduces the number of traces collected to save resources.

- Improves performance but may miss some traces.

- Trade-off between visibility and overhead.