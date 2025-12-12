package com.uni.swms.repository;

import com.uni.swms.domain.SystemLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long>, JpaSpecificationExecutor<SystemLog> {}
