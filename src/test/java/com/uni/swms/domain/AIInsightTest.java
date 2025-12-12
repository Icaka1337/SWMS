package com.uni.swms.domain;

import static com.uni.swms.domain.AIInsightTestSamples.*;
import static com.uni.swms.domain.ProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AIInsightTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AIInsight.class);
        AIInsight aIInsight1 = getAIInsightSample1();
        AIInsight aIInsight2 = new AIInsight();
        assertThat(aIInsight1).isNotEqualTo(aIInsight2);

        aIInsight2.setId(aIInsight1.getId());
        assertThat(aIInsight1).isEqualTo(aIInsight2);

        aIInsight2 = getAIInsightSample2();
        assertThat(aIInsight1).isNotEqualTo(aIInsight2);
    }

    @Test
    void productTest() {
        AIInsight aIInsight = getAIInsightRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        aIInsight.setProduct(productBack);
        assertThat(aIInsight.getProduct()).isEqualTo(productBack);

        aIInsight.product(null);
        assertThat(aIInsight.getProduct()).isNull();
    }
}
