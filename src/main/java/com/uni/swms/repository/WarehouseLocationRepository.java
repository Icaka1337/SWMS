package com.uni.swms.repository;

import com.uni.swms.domain.WarehouseLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WarehouseLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long>, JpaSpecificationExecutor<WarehouseLocation> {}
