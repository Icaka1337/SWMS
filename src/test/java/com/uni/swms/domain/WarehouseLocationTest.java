package com.uni.swms.domain;

import static com.uni.swms.domain.WarehouseLocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WarehouseLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarehouseLocation.class);
        WarehouseLocation warehouseLocation1 = getWarehouseLocationSample1();
        WarehouseLocation warehouseLocation2 = new WarehouseLocation();
        assertThat(warehouseLocation1).isNotEqualTo(warehouseLocation2);

        warehouseLocation2.setId(warehouseLocation1.getId());
        assertThat(warehouseLocation1).isEqualTo(warehouseLocation2);

        warehouseLocation2 = getWarehouseLocationSample2();
        assertThat(warehouseLocation1).isNotEqualTo(warehouseLocation2);
    }
}
