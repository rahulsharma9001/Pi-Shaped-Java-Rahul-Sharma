package com.evolve.Payment_Application;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @GetMapping("/payment/process")
    @Retry(name = "paymentService", fallbackMethod = "retryFallback")
    @TimeLimiter(name = "paymentService", fallbackMethod = "timeoutFallback")
    @Bulkhead(name = "paymentService", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "bulkheadFallback")
    public CompletableFuture<String> processPayment() {
        logger.info("Processing payment...");
        return CompletableFuture.supplyAsync(this::processWithRetry)
                .exceptionally(ex -> {
                    logger.warn("Exceptionally caught: {}", ex.toString());
                    return "Payment failed inside exceptionally block.";
                });
    }

    private String processWithRetry() {
        double rand = Math.random();
        logger.info("Generated random number: {}", rand);
        if (rand < 0.5) {
            logger.info("Simulating delay to trigger timeout...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else {
            logger.info("Simulating failure to trigger retry...");
            throw new IllegalStateException("Simulated failure");
        }
        return "Payment processed successfully!";
    }

    public CompletableFuture<String> retryFallback(Throwable t) {
        logger.warn("Retry fallback triggered due to: {}", t.toString());
        return CompletableFuture.completedFuture("Payment failed after retries. Please try again later.");
    }

    public CompletableFuture<String> timeoutFallback(Throwable t) {
        logger.warn("Timeout fallback triggered due to: {}", t.toString());
        return CompletableFuture.completedFuture("Payment timed out. Please try again later.");
    }

    public CompletableFuture<String> bulkheadFallback(Throwable t) {
        logger.warn("Bulkhead fallback triggered due to: {}", t.toString());
        return CompletableFuture.completedFuture("Too many requests. Please try again later.");
    }


}
