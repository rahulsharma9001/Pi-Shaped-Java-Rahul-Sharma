## Core Concept Questions

---
1️⃣ What are the differences between Mono and Flux, and where did you use each?
- Mono: Emits zero or one result. Think of it like Optional or a Promise.
  Use Mono when you fetch or return a single value (e.g., find by ID, save one record).
- Flux: Emits zero to many results. Think of it like a stream or a list.
  Use Flux for multiple results (e.g., get all users, get multiple items, stream updates).
- In this project: Mono for single DB operations, Flux for lists and Server-Sent Events (SSE).

2️⃣ How does R2DBC differ from traditional JDBC?
- JDBC is blocking: each call waits for the DB to respond, blocking the thread.
- R2DBC is non-blocking and reactive: supports async I/O and backpressure.
- This means you can handle more concurrent requests with the same resources.
- R2DBC works well with Spring WebFlux for fully reactive pipelines.

3️⃣ How does Spring WebFlux routing differ from @RestController?
- @RestController: Uses annotations like @GetMapping and @PostMapping.
  Easy and declarative, similar to Spring MVC.
- Routing (RouterFunction + HandlerFunction): Uses a functional style.
  You define routes and handler logic separately. More flexible for advanced reactive flows.
- Use RouterFunction when you want clear separation and more functional composition.

4️⃣ What are the advantages of using @Transactional with R2DBC (or why not)?
- R2DBC supports reactive transactions, but the support depends on your driver.
- @Transactional can work, but works differently than JDBC transactions.
- Often it’s clearer to manage transactions manually with DatabaseClient.
- Important: blocking transaction managers break reactive flow, so stick to fully reactive DB ops.

5️⃣ Explain the difference between optimistic vs pessimistic locking, and where would you use either?
- Optimistic locking: Doesn’t lock rows in the DB. Uses a version field.
  When you update, it checks the version. If someone changed it, your update fails.
  Good when conflicts are rare (e.g., product catalog updates).
- Pessimistic locking: Locks rows up front to prevent others from updating.
  Used when conflicts are likely or critical data must not change midway (e.g., banking).
- Choose based on how much contention you expect.

6️⃣ Describe how session management works using Redis in reactive apps.
- Store session data in Redis instead of in server memory.
- Spring Session integrates with Redis to make the app stateless.
- This means multiple app instances share the same session info.
- Using a reactive Redis client ensures session operations don’t block the event loop.

7️⃣ How did you expose and verify SSE updates?
- SSE (Server-Sent Events) is used to push data from server to client.
- Exposed SSE endpoint as a Flux stream with content-type text/event-stream.
- Client subscribes to /events endpoint to receive real-time updates.
- Verified using WebTestClient to connect to the stream and assert received data.

8️⃣ What is the role of Swagger/OpenAPI in CI/CD and API consumer tooling?
- Generates live, interactive API documentation.
- Helps frontend and other teams understand and test your endpoints easily.
- In CI/CD, OpenAPI definitions can be validated to catch breaking changes.
- Tools can auto-generate client SDKs from OpenAPI, saving time and reducing bugs.

9️⃣ How does WebTestClient differ from MockMvc in testing?
- MockMvc: Used for Spring MVC apps (blocking). Doesn’t handle reactive streams well.
- WebTestClient: Designed for Spring WebFlux (non-blocking). Can test Mono, Flux, SSE.
- Lets you write end-to-end style tests for your functional reactive routes.

🔟 Compare WebSocket, SSE, and RSocket for real-time data needs.
- WebSocket: Full duplex. Client and server can send messages anytime.
  Great for chat apps, games, live dashboards.
- SSE: One-way stream from server to client. Simpler than WebSocket.
  Good for live updates, notifications, or dashboards.
- RSocket: Modern protocol for bi-directional, reactive communication.
  Supports backpressure and multiple transports (TCP, WebSocket).
  Good for advanced real-time microservices communication.
