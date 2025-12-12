package com.uni.swms.service;

import com.uni.swms.service.dto.WarehouseLocationDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.uni.swms.domain.WarehouseLocation}.
 */
public interface WarehouseLocationService {
    /**
     * Save a warehouseLocation.
     *
     * @param warehouseLocationDTO the entity to save.
     * @return the persisted entity.
     */
    WarehouseLocationDTO save(WarehouseLocationDTO warehouseLocationDTO);

    /**
     * Updates a warehouseLocation.
     *
     * @param warehouseLocationDTO the entity to update.
     * @return the persisted entity.
     */
    WarehouseLocationDTO update(WarehouseLocationDTO warehouseLocationDTO);

    /**
     * Partially updates a warehouseLocation.
     *
     * @param warehouseLocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WarehouseLocationDTO> partialUpdate(WarehouseLocationDTO warehouseLocationDTO);

    /**
     * Get the "id" warehouseLocation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WarehouseLocationDTO> findOne(Long id);

    /**
     * Delete the "id" warehouseLocation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
