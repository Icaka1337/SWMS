package com.uni.swms.repository;

import com.uni.swms.domain.AIInsight;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AIInsight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AIInsightRepository extends JpaRepository<AIInsight, Long>, JpaSpecificationExecutor<AIInsight> {}
