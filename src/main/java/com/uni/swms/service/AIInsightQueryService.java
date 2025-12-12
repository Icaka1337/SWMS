package com.uni.swms.service;

import com.uni.swms.domain.*; // for static metamodels
import com.uni.swms.domain.AIInsight;
import com.uni.swms.repository.AIInsightRepository;
import com.uni.swms.service.criteria.AIInsightCriteria;
import com.uni.swms.service.dto.AIInsightDTO;
import com.uni.swms.service.mapper.AIInsightMapper;
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
 * Service for executing complex queries for {@link AIInsight} entities in the database.
 * The main input is a {@link AIInsightCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AIInsightDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AIInsightQueryService extends QueryService<AIInsight> {

    private static final Logger LOG = LoggerFactory.getLogger(AIInsightQueryService.class);

    private final AIInsightRepository aIInsightRepository;

    private final AIInsightMapper aIInsightMapper;

    public AIInsightQueryService(AIInsightRepository aIInsightRepository, AIInsightMapper aIInsightMapper) {
        this.aIInsightRepository = aIInsightRepository;
        this.aIInsightMapper = aIInsightMapper;
    }

    /**
     * Return a {@link Page} of {@link AIInsightDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AIInsightDTO> findByCriteria(AIInsightCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AIInsight> specification = createSpecification(criteria);
        return aIInsightRepository.findAll(specification, page).map(aIInsightMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AIInsightCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AIInsight> specification = createSpecification(criteria);
        return aIInsightRepository.count(specification);
    }

    /**
     * Function to convert {@link AIInsightCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AIInsight> createSpecification(AIInsightCriteria criteria) {
        Specification<AIInsight> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AIInsight_.id),
                buildStringSpecification(criteria.getType(), AIInsight_.type),
                buildStringSpecification(criteria.getMessage(), AIInsight_.message),
                buildRangeSpecification(criteria.getConfidence(), AIInsight_.confidence),
                buildRangeSpecification(criteria.getGeneratedAt(), AIInsight_.generatedAt),
                buildSpecification(criteria.getProductId(), root -> root.join(AIInsight_.product, JoinType.LEFT).get(Product_.id))
            );
        }
        return specification;
    }
}
