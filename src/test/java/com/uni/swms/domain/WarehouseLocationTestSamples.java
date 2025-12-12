package com.uni.swms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class WarehouseLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static WarehouseLocation getWarehouseLocationSample1() {
        return new WarehouseLocation().id(1L).name("name1").section("section1").capacity(1).description("description1");
    }

    public static WarehouseLocation getWarehouseLocationSample2() {
        return new WarehouseLocation().id(2L).name("name2").section("section2").capacity(2).description("description2");
    }

    public static WarehouseLocation getWarehouseLocationRandomSampleGenerator() {
        return new WarehouseLocation()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .section(UUID.randomUUID().toString())
            .capacity(intCount.incrementAndGet())
            .description(UUID.randomUUID().toString());
    }
}
