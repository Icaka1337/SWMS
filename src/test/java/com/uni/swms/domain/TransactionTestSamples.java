package com.uni.swms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Transaction getTransactionSample1() {
        return new Transaction().id(1L).type("type1").quantity(1).notes("notes1");
    }

    public static Transaction getTransactionSample2() {
        return new Transaction().id(2L).type("type2").quantity(2).notes("notes2");
    }

    public static Transaction getTransactionRandomSampleGenerator() {
        return new Transaction()
            .id(longCount.incrementAndGet())
            .type(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .notes(UUID.randomUUID().toString());
    }
}
