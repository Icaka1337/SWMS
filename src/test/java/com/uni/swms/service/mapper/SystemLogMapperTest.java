package com.uni.swms.service.mapper;

import static com.uni.swms.domain.SystemLogAsserts.*;
import static com.uni.swms.domain.SystemLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemLogMapperTest {

    private SystemLogMapper systemLogMapper;

    @BeforeEach
    void setUp() {
        systemLogMapper = new SystemLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSystemLogSample1();
        var actual = systemLogMapper.toEntity(systemLogMapper.toDto(expected));
        assertSystemLogAllPropertiesEquals(expected, actual);
    }
}
