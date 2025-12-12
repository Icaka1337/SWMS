package com.uni.swms.domain;

import static com.uni.swms.domain.SystemLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemLog.class);
        SystemLog systemLog1 = getSystemLogSample1();
        SystemLog systemLog2 = new SystemLog();
        assertThat(systemLog1).isNotEqualTo(systemLog2);

        systemLog2.setId(systemLog1.getId());
        assertThat(systemLog1).isEqualTo(systemLog2);

        systemLog2 = getSystemLogSample2();
        assertThat(systemLog1).isNotEqualTo(systemLog2);
    }
}
