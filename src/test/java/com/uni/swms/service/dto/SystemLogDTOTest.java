package com.uni.swms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemLogDTO.class);
        SystemLogDTO systemLogDTO1 = new SystemLogDTO();
        systemLogDTO1.setId(1L);
        SystemLogDTO systemLogDTO2 = new SystemLogDTO();
        assertThat(systemLogDTO1).isNotEqualTo(systemLogDTO2);
        systemLogDTO2.setId(systemLogDTO1.getId());
        assertThat(systemLogDTO1).isEqualTo(systemLogDTO2);
        systemLogDTO2.setId(2L);
        assertThat(systemLogDTO1).isNotEqualTo(systemLogDTO2);
        systemLogDTO1.setId(null);
        assertThat(systemLogDTO1).isNotEqualTo(systemLogDTO2);
    }
}
