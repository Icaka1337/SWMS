package com.uni.swms.service.mapper;

import static com.uni.swms.domain.AIInsightAsserts.*;
import static com.uni.swms.domain.AIInsightTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AIInsightMapperTest {

    private AIInsightMapper aIInsightMapper;

    @BeforeEach
    void setUp() {
        aIInsightMapper = new AIInsightMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAIInsightSample1();
        var actual = aIInsightMapper.toEntity(aIInsightMapper.toDto(expected));
        assertAIInsightAllPropertiesEquals(expected, actual);
    }
}
