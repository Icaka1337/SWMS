package com.uni.swms.service;

import com.uni.swms.service.dto.InventoryItemDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.uni.swms.domain.InventoryItem}.
 */
public interface InventoryItemService {
    /**
     * Save a inventoryItem.
     *
     * @param inventoryItemDTO the entity to save.
     * @return the persisted entity.
     */
    InventoryItemDTO save(InventoryItemDTO inventoryItemDTO);

    /**
     * Updates a inventoryItem.
     *
     * @param inventoryItemDTO the entity to update.
     * @return the persisted entity.
     */
    InventoryItemDTO update(InventoryItemDTO inventoryItemDTO);

    /**
     * Partially updates a inventoryItem.
     *
     * @param inventoryItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InventoryItemDTO> partialUpdate(InventoryItemDTO inventoryItemDTO);

    /**
     * Get the "id" inventoryItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InventoryItemDTO> findOne(Long id);

    /**
     * Delete the "id" inventoryItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
