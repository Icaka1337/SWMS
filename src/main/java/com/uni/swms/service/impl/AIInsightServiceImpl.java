package com.uni.swms.service.impl;

import com.uni.swms.domain.AIInsight;
import com.uni.swms.repository.AIInsightRepository;
import com.uni.swms.service.AIInsightService;
import com.uni.swms.service.dto.AIInsightDTO;
import com.uni.swms.service.mapper.AIInsightMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.uni.swms.domain.AIInsight}.
 */
@Service
@Transactional
public class AIInsightServiceImpl implements AIInsightService {

    private static final Logger LOG = LoggerFactory.getLogger(AIInsightServiceImpl.class);

    private final AIInsightRepository aIInsightRepository;

    private final AIInsightMapper aIInsightMapper;

    public AIInsightServiceImpl(AIInsightRepository aIInsightRepository, AIInsightMapper aIInsightMapper) {
        this.aIInsightRepository = aIInsightRepository;
        this.aIInsightMapper = aIInsightMapper;
    }

    @Override
    public AIInsightDTO save(AIInsightDTO aIInsightDTO) {
        LOG.debug("Request to save AIInsight : {}", aIInsightDTO);
        AIInsight aIInsight = aIInsightMapper.toEntity(aIInsightDTO);
        aIInsight = aIInsightRepository.save(aIInsight);
        return aIInsightMapper.toDto(aIInsight);
    }

    @Override
    public AIInsightDTO update(AIInsightDTO aIInsightDTO) {
        LOG.debug("Request to update AIInsight : {}", aIInsightDTO);
        AIInsight aIInsight = aIInsightMapper.toEntity(aIInsightDTO);
        aIInsight = aIInsightRepository.save(aIInsight);
        return aIInsightMapper.toDto(aIInsight);
    }

    @Override
    public Optional<AIInsightDTO> partialUpdate(AIInsightDTO aIInsightDTO) {
        LOG.debug("Request to partially update AIInsight : {}", aIInsightDTO);

        return aIInsightRepository
            .findById(aIInsightDTO.getId())
            .map(existingAIInsight -> {
                aIInsightMapper.partialUpdate(existingAIInsight, aIInsightDTO);

                return existingAIInsight;
            })
            .map(aIInsightRepository::save)
            .map(aIInsightMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AIInsightDTO> findOne(Long id) {
        LOG.debug("Request to get AIInsight : {}", id);
        return aIInsightRepository.findById(id).map(aIInsightMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AIInsight : {}", id);
        aIInsightRepository.deleteById(id);
    }
}
