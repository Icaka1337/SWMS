package com.uni.swms.service;

import com.uni.swms.service.dto.AIInsightDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.uni.swms.domain.AIInsight}.
 */
public interface AIInsightService {
    /**
     * Save a aIInsight.
     *
     * @param aIInsightDTO the entity to save.
     * @return the persisted entity.
     */
    AIInsightDTO save(AIInsightDTO aIInsightDTO);

    /**
     * Updates a aIInsight.
     *
     * @param aIInsightDTO the entity to update.
     * @return the persisted entity.
     */
    AIInsightDTO update(AIInsightDTO aIInsightDTO);

    /**
     * Partially updates a aIInsight.
     *
     * @param aIInsightDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AIInsightDTO> partialUpdate(AIInsightDTO aIInsightDTO);

    /**
     * Get the "id" aIInsight.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AIInsightDTO> findOne(Long id);

    /**
     * Delete the "id" aIInsight.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
