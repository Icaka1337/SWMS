package com.uni.swms.service;

import com.uni.swms.service.dto.SystemLogDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.uni.swms.domain.SystemLog}.
 */
public interface SystemLogService {
    /**
     * Save a systemLog.
     *
     * @param systemLogDTO the entity to save.
     * @return the persisted entity.
     */
    SystemLogDTO save(SystemLogDTO systemLogDTO);

    /**
     * Updates a systemLog.
     *
     * @param systemLogDTO the entity to update.
     * @return the persisted entity.
     */
    SystemLogDTO update(SystemLogDTO systemLogDTO);

    /**
     * Partially updates a systemLog.
     *
     * @param systemLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemLogDTO> partialUpdate(SystemLogDTO systemLogDTO);

    /**
     * Get the "id" systemLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemLogDTO> findOne(Long id);

    /**
     * Delete the "id" systemLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
