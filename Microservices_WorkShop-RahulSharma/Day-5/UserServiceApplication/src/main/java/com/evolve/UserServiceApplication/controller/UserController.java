package com.evolve.UserServiceApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.MeterRegistry;

@RestController
public class UserController {

    private final MeterRegistry meterRegistry;

    public UserController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("api/user/{id}")
    public ResponseEntity<String> getUser(@PathVariable String id) throws InterruptedException {
        int delay = new Random().nextInt(400) + 100;

        long start = System.currentTimeMillis();
        Thread.sleep(delay);
        long duration = System.currentTimeMillis() - start;

        // Custom metric
        meterRegistry.timer("user.api.latency").record(duration, TimeUnit.MILLISECONDS);

        return ResponseEntity.ok("User ID: " + id + ", Simulated delay: " + delay + " ms");
    }
}

