package com.uni.swms.service.impl;

import com.uni.swms.domain.SystemLog;
import com.uni.swms.repository.SystemLogRepository;
import com.uni.swms.service.SystemLogService;
import com.uni.swms.service.dto.SystemLogDTO;
import com.uni.swms.service.mapper.SystemLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.uni.swms.domain.SystemLog}.
 */
@Service
@Transactional
public class SystemLogServiceImpl implements SystemLogService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemLogServiceImpl.class);

    private final SystemLogRepository systemLogRepository;

    private final SystemLogMapper systemLogMapper;

    public SystemLogServiceImpl(SystemLogRepository systemLogRepository, SystemLogMapper systemLogMapper) {
        this.systemLogRepository = systemLogRepository;
        this.systemLogMapper = systemLogMapper;
    }

    @Override
    public SystemLogDTO save(SystemLogDTO systemLogDTO) {
        LOG.debug("Request to save SystemLog : {}", systemLogDTO);
        SystemLog systemLog = systemLogMapper.toEntity(systemLogDTO);
        systemLog = systemLogRepository.save(systemLog);
        return systemLogMapper.toDto(systemLog);
    }

    @Override
    public SystemLogDTO update(SystemLogDTO systemLogDTO) {
        LOG.debug("Request to update SystemLog : {}", systemLogDTO);
        SystemLog systemLog = systemLogMapper.toEntity(systemLogDTO);
        systemLog = systemLogRepository.save(systemLog);
        return systemLogMapper.toDto(systemLog);
    }

    @Override
    public Optional<SystemLogDTO> partialUpdate(SystemLogDTO systemLogDTO) {
        LOG.debug("Request to partially update SystemLog : {}", systemLogDTO);

        return systemLogRepository
            .findById(systemLogDTO.getId())
            .map(existingSystemLog -> {
                systemLogMapper.partialUpdate(existingSystemLog, systemLogDTO);

                return existingSystemLog;
            })
            .map(systemLogRepository::save)
            .map(systemLogMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemLogDTO> findOne(Long id) {
        LOG.debug("Request to get SystemLog : {}", id);
        return systemLogRepository.findById(id).map(systemLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete SystemLog : {}", id);
        systemLogRepository.deleteById(id);
    }
}
