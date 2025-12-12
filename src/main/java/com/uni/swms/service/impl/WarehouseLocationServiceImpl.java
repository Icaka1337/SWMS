package com.uni.swms.service.impl;

import com.uni.swms.domain.WarehouseLocation;
import com.uni.swms.repository.WarehouseLocationRepository;
import com.uni.swms.service.WarehouseLocationService;
import com.uni.swms.service.dto.WarehouseLocationDTO;
import com.uni.swms.service.mapper.WarehouseLocationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.uni.swms.domain.WarehouseLocation}.
 */
@Service
@Transactional
public class WarehouseLocationServiceImpl implements WarehouseLocationService {

    private static final Logger LOG = LoggerFactory.getLogger(WarehouseLocationServiceImpl.class);

    private final WarehouseLocationRepository warehouseLocationRepository;

    private final WarehouseLocationMapper warehouseLocationMapper;

    public WarehouseLocationServiceImpl(
        WarehouseLocationRepository warehouseLocationRepository,
        WarehouseLocationMapper warehouseLocationMapper
    ) {
        this.warehouseLocationRepository = warehouseLocationRepository;
        this.warehouseLocationMapper = warehouseLocationMapper;
    }

    @Override
    public WarehouseLocationDTO save(WarehouseLocationDTO warehouseLocationDTO) {
        LOG.debug("Request to save WarehouseLocation : {}", warehouseLocationDTO);
        WarehouseLocation warehouseLocation = warehouseLocationMapper.toEntity(warehouseLocationDTO);
        warehouseLocation = warehouseLocationRepository.save(warehouseLocation);
        return warehouseLocationMapper.toDto(warehouseLocation);
    }

    @Override
    public WarehouseLocationDTO update(WarehouseLocationDTO warehouseLocationDTO) {
        LOG.debug("Request to update WarehouseLocation : {}", warehouseLocationDTO);
        WarehouseLocation warehouseLocation = warehouseLocationMapper.toEntity(warehouseLocationDTO);
        warehouseLocation = warehouseLocationRepository.save(warehouseLocation);
        return warehouseLocationMapper.toDto(warehouseLocation);
    }

    @Override
    public Optional<WarehouseLocationDTO> partialUpdate(WarehouseLocationDTO warehouseLocationDTO) {
        LOG.debug("Request to partially update WarehouseLocation : {}", warehouseLocationDTO);

        return warehouseLocationRepository
            .findById(warehouseLocationDTO.getId())
            .map(existingWarehouseLocation -> {
                warehouseLocationMapper.partialUpdate(existingWarehouseLocation, warehouseLocationDTO);

                return existingWarehouseLocation;
            })
            .map(warehouseLocationRepository::save)
            .map(warehouseLocationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WarehouseLocationDTO> findOne(Long id) {
        LOG.debug("Request to get WarehouseLocation : {}", id);
        return warehouseLocationRepository.findById(id).map(warehouseLocationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete WarehouseLocation : {}", id);
        warehouseLocationRepository.deleteById(id);
    }
}
