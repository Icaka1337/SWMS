package com.uni.swms.service;

import com.uni.swms.domain.*; // for static metamodels
import com.uni.swms.domain.InventoryItem;
import com.uni.swms.repository.InventoryItemRepository;
import com.uni.swms.service.criteria.InventoryItemCriteria;
import com.uni.swms.service.dto.InventoryItemDTO;
import com.uni.swms.service.mapper.InventoryItemMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InventoryItem} entities in the database.
 * The main input is a {@link InventoryItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InventoryItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InventoryItemQueryService extends QueryService<InventoryItem> {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemQueryService.class);

    private final InventoryItemRepository inventoryItemRepository;

    private final InventoryItemMapper inventoryItemMapper;

    public InventoryItemQueryService(InventoryItemRepository inventoryItemRepository, InventoryItemMapper inventoryItemMapper) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryItemMapper = inventoryItemMapper;
    }

    /**
     * Return a {@link Page} of {@link InventoryItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InventoryItemDTO> findByCriteria(InventoryItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InventoryItem> specification = createSpecification(criteria);
        return inventoryItemRepository.findAll(specification, page).map(inventoryItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InventoryItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InventoryItem> specification = createSpecification(criteria);
        return inventoryItemRepository.count(specification);
    }

    /**
     * Function to convert {@link InventoryItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InventoryItem> createSpecification(InventoryItemCriteria criteria) {
        Specification<InventoryItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InventoryItem_.id),
                buildRangeSpecification(criteria.getQuantity(), InventoryItem_.quantity),
                buildRangeSpecification(criteria.getLastUpdated(), InventoryItem_.lastUpdated),
                buildSpecification(criteria.getProductId(), root -> root.join(InventoryItem_.product, JoinType.LEFT).get(Product_.id)),
                buildSpecification(criteria.getLocationId(), root ->
                    root.join(InventoryItem_.location, JoinType.LEFT).get(WarehouseLocation_.id)
                )
            );
        }
        return specification;
    }
}
