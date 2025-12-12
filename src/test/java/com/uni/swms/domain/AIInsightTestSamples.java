package com.uni.swms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AIInsightTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AIInsight getAIInsightSample1() {
        return new AIInsight().id(1L).type("type1").message("message1");
    }

    public static AIInsight getAIInsightSample2() {
        return new AIInsight().id(2L).type("type2").message("message2");
    }

    public static AIInsight getAIInsightRandomSampleGenerator() {
        return new AIInsight().id(longCount.incrementAndGet()).type(UUID.randomUUID().toString()).message(UUID.randomUUID().toString());
    }
}
