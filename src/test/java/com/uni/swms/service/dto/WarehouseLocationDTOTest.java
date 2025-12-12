package com.uni.swms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WarehouseLocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarehouseLocationDTO.class);
        WarehouseLocationDTO warehouseLocationDTO1 = new WarehouseLocationDTO();
        warehouseLocationDTO1.setId(1L);
        WarehouseLocationDTO warehouseLocationDTO2 = new WarehouseLocationDTO();
        assertThat(warehouseLocationDTO1).isNotEqualTo(warehouseLocationDTO2);
        warehouseLocationDTO2.setId(warehouseLocationDTO1.getId());
        assertThat(warehouseLocationDTO1).isEqualTo(warehouseLocationDTO2);
        warehouseLocationDTO2.setId(2L);
        assertThat(warehouseLocationDTO1).isNotEqualTo(warehouseLocationDTO2);
        warehouseLocationDTO1.setId(null);
        assertThat(warehouseLocationDTO1).isNotEqualTo(warehouseLocationDTO2);
    }
}
