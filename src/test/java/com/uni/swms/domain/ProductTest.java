package com.uni.swms.domain;

import static com.uni.swms.domain.ProductTestSamples.*;
import static com.uni.swms.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void supplierTest() {
        Product product = getProductRandomSampleGenerator();
        Supplier supplierBack = getSupplierRandomSampleGenerator();

        product.setSupplier(supplierBack);
        assertThat(product.getSupplier()).isEqualTo(supplierBack);

        product.supplier(null);
        assertThat(product.getSupplier()).isNull();
    }
}
