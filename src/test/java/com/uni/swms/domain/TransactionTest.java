package com.uni.swms.domain;

import static com.uni.swms.domain.ProductTestSamples.*;
import static com.uni.swms.domain.TransactionTestSamples.*;
import static com.uni.swms.domain.WarehouseLocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.uni.swms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = getTransactionSample1();
        Transaction transaction2 = new Transaction();
        assertThat(transaction1).isNotEqualTo(transaction2);

        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);

        transaction2 = getTransactionSample2();
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void productTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        transaction.setProduct(productBack);
        assertThat(transaction.getProduct()).isEqualTo(productBack);

        transaction.product(null);
        assertThat(transaction.getProduct()).isNull();
    }

    @Test
    void sourceLocationTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        WarehouseLocation warehouseLocationBack = getWarehouseLocationRandomSampleGenerator();

        transaction.setSourceLocation(warehouseLocationBack);
        assertThat(transaction.getSourceLocation()).isEqualTo(warehouseLocationBack);

        transaction.sourceLocation(null);
        assertThat(transaction.getSourceLocation()).isNull();
    }

    @Test
    void targetLocationTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        WarehouseLocation warehouseLocationBack = getWarehouseLocationRandomSampleGenerator();

        transaction.setTargetLocation(warehouseLocationBack);
        assertThat(transaction.getTargetLocation()).isEqualTo(warehouseLocationBack);

        transaction.targetLocation(null);
        assertThat(transaction.getTargetLocation()).isNull();
    }
}
