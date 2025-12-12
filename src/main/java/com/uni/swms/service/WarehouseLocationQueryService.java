package com.uni.swms.service;

import com.uni.swms.domain.*; // for static metamodels
import com.uni.swms.domain.WarehouseLocation;
import com.uni.swms.repository.WarehouseLocationRepository;
import com.uni.swms.service.criteria.WarehouseLocationCriteria;
import com.uni.swms.service.dto.WarehouseLocationDTO;
import com.uni.swms.service.mapper.WarehouseLocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link WarehouseLocation} entities in the database.
 * The main input is a {@link WarehouseLocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WarehouseLocationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WarehouseLocationQueryService extends QueryService<WarehouseLocation> {

    private static final Logger LOG = LoggerFactory.getLogger(WarehouseLocationQueryService.class);

    private final WarehouseLocationRepository warehouseLocationRepository;

    private final WarehouseLocationMapper warehouseLocationMapper;

    public WarehouseLocationQueryService(
        WarehouseLocationRepository warehouseLocationRepository,
        WarehouseLocationMapper warehouseLocationMapper
    ) {
        this.warehouseLocationRepository = warehouseLocationRepository;
        this.warehouseLocationMapper = warehouseLocationMapper;
    }

    /**
     * Return a {@link Page} of {@link WarehouseLocationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WarehouseLocationDTO> findByCriteria(WarehouseLocationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WarehouseLocation> specification = createSpecification(criteria);
        return warehouseLocationRepository.findAll(specification, page).map(warehouseLocationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WarehouseLocationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<WarehouseLocation> specification = createSpecification(criteria);
        return warehouseLocationRepository.count(specification);
    }

    /**
     * Function to convert {@link WarehouseLocationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WarehouseLocation> createSpecification(WarehouseLocationCriteria criteria) {
        Specification<WarehouseLocation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), WarehouseLocation_.id),
                buildStringSpecification(criteria.getName(), WarehouseLocation_.name),
                buildStringSpecification(criteria.getSection(), WarehouseLocation_.section),
                buildRangeSpecification(criteria.getCapacity(), WarehouseLocation_.capacity),
                buildStringSpecification(criteria.getDescription(), WarehouseLocation_.description)
            );
        }
        return specification;
    }
}
