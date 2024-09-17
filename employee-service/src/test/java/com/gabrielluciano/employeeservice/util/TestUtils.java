package com.gabrielluciano.employeeservice.util;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtils {

    public static int executeConcurrently(int times, Callable callable) {
        AtomicInteger numberOfExecutions = new AtomicInteger();
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            CountDownLatch latch = new CountDownLatch(times);
            for (int i = 0; i < times; i++) {
                executorService.submit(() -> {
                    try {
                        callable.call();
                        numberOfExecutions.getAndIncrement();
                        latch.countDown();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            latch.await();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        return numberOfExecutions.get();
    }
}
