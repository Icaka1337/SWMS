package com.uni.swms.service;

import com.uni.swms.domain.*; // for static metamodels
import com.uni.swms.domain.SystemLog;
import com.uni.swms.repository.SystemLogRepository;
import com.uni.swms.service.criteria.SystemLogCriteria;
import com.uni.swms.service.dto.SystemLogDTO;
import com.uni.swms.service.mapper.SystemLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SystemLog} entities in the database.
 * The main input is a {@link SystemLogCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SystemLogDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemLogQueryService extends QueryService<SystemLog> {

    private static final Logger LOG = LoggerFactory.getLogger(SystemLogQueryService.class);

    private final SystemLogRepository systemLogRepository;

    private final SystemLogMapper systemLogMapper;

    public SystemLogQueryService(SystemLogRepository systemLogRepository, SystemLogMapper systemLogMapper) {
        this.systemLogRepository = systemLogRepository;
        this.systemLogMapper = systemLogMapper;
    }

    /**
     * Return a {@link Page} of {@link SystemLogDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemLogDTO> findByCriteria(SystemLogCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SystemLog> specification = createSpecification(criteria);
        return systemLogRepository.findAll(specification, page).map(systemLogMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemLogCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SystemLog> specification = createSpecification(criteria);
        return systemLogRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemLogCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SystemLog> createSpecification(SystemLogCriteria criteria) {
        Specification<SystemLog> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SystemLog_.id),
                buildStringSpecification(criteria.getUsername(), SystemLog_.username),
                buildStringSpecification(criteria.getAction(), SystemLog_.action),
                buildStringSpecification(criteria.getEntityName(), SystemLog_.entityName),
                buildStringSpecification(criteria.getDetails(), SystemLog_.details),
                buildRangeSpecification(criteria.getTimestamp(), SystemLog_.timestamp)
            );
        }
        return specification;
    }
}
