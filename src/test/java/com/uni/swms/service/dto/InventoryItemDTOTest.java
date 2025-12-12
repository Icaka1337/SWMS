package com.uni.swms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InventoryItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InventoryItemDTO.class);
        InventoryItemDTO inventoryItemDTO1 = new InventoryItemDTO();
        inventoryItemDTO1.setId(1L);
        InventoryItemDTO inventoryItemDTO2 = new InventoryItemDTO();
        assertThat(inventoryItemDTO1).isNotEqualTo(inventoryItemDTO2);
        inventoryItemDTO2.setId(inventoryItemDTO1.getId());
        assertThat(inventoryItemDTO1).isEqualTo(inventoryItemDTO2);
        inventoryItemDTO2.setId(2L);
        assertThat(inventoryItemDTO1).isNotEqualTo(inventoryItemDTO2);
        inventoryItemDTO1.setId(null);
        assertThat(inventoryItemDTO1).isNotEqualTo(inventoryItemDTO2);
    }
}
