package com.uni.swms.service.mapper;

import static com.uni.swms.domain.WarehouseLocationAsserts.*;
import static com.uni.swms.domain.WarehouseLocationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WarehouseLocationMapperTest {

    private WarehouseLocationMapper warehouseLocationMapper;

    @BeforeEach
    void setUp() {
        warehouseLocationMapper = new WarehouseLocationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWarehouseLocationSample1();
        var actual = warehouseLocationMapper.toEntity(warehouseLocationMapper.toDto(expected));
        assertWarehouseLocationAllPropertiesEquals(expected, actual);
    }
}
