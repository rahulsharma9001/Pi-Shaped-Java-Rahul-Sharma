package com.evolve.UserServiceApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final MeterRegistry meterRegistry;

    public UserController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable String id) throws InterruptedException {
        long start = System.currentTimeMillis();

        long delay = 100 + (long) (Math.random() * 400);
        Thread.sleep(delay);

        long duration = System.currentTimeMillis() - start;

        // record custom metric
        meterRegistry.timer("user.api.latency").record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);

        return "User with ID: " + id + " (delay: " + delay + " ms)";
    }
}