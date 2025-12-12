package com.uni.swms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AIInsightDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AIInsightDTO.class);
        AIInsightDTO aIInsightDTO1 = new AIInsightDTO();
        aIInsightDTO1.setId(1L);
        AIInsightDTO aIInsightDTO2 = new AIInsightDTO();
        assertThat(aIInsightDTO1).isNotEqualTo(aIInsightDTO2);
        aIInsightDTO2.setId(aIInsightDTO1.getId());
        assertThat(aIInsightDTO1).isEqualTo(aIInsightDTO2);
        aIInsightDTO2.setId(2L);
        assertThat(aIInsightDTO1).isNotEqualTo(aIInsightDTO2);
        aIInsightDTO1.setId(null);
        assertThat(aIInsightDTO1).isNotEqualTo(aIInsightDTO2);
    }
}
