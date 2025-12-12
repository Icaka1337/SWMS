package com.uni.swms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SystemLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SystemLog getSystemLogSample1() {
        return new SystemLog().id(1L).username("username1").action("action1").entityName("entityName1").details("details1");
    }

    public static SystemLog getSystemLogSample2() {
        return new SystemLog().id(2L).username("username2").action("action2").entityName("entityName2").details("details2");
    }

    public static SystemLog getSystemLogRandomSampleGenerator() {
        return new SystemLog()
            .id(longCount.incrementAndGet())
            .username(UUID.randomUUID().toString())
            .action(UUID.randomUUID().toString())
            .entityName(UUID.randomUUID().toString())
            .details(UUID.randomUUID().toString());
    }
}
