package com.uni.swms.service.impl;

import com.uni.swms.domain.InventoryItem;
import com.uni.swms.repository.InventoryItemRepository;
import com.uni.swms.service.InventoryItemService;
import com.uni.swms.service.dto.InventoryItemDTO;
import com.uni.swms.service.mapper.InventoryItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.uni.swms.domain.InventoryItem}.
 */
@Service
@Transactional
public class InventoryItemServiceImpl implements InventoryItemService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemServiceImpl.class);

    private final InventoryItemRepository inventoryItemRepository;

    private final InventoryItemMapper inventoryItemMapper;

    public InventoryItemServiceImpl(InventoryItemRepository inventoryItemRepository, InventoryItemMapper inventoryItemMapper) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryItemMapper = inventoryItemMapper;
    }

    @Override
    public InventoryItemDTO save(InventoryItemDTO inventoryItemDTO) {
        LOG.debug("Request to save InventoryItem : {}", inventoryItemDTO);
        InventoryItem inventoryItem = inventoryItemMapper.toEntity(inventoryItemDTO);
        inventoryItem = inventoryItemRepository.save(inventoryItem);
        return inventoryItemMapper.toDto(inventoryItem);
    }

    @Override
    public InventoryItemDTO update(InventoryItemDTO inventoryItemDTO) {
        LOG.debug("Request to update InventoryItem : {}", inventoryItemDTO);
        InventoryItem inventoryItem = inventoryItemMapper.toEntity(inventoryItemDTO);
        inventoryItem = inventoryItemRepository.save(inventoryItem);
        return inventoryItemMapper.toDto(inventoryItem);
    }

    @Override
    public Optional<InventoryItemDTO> partialUpdate(InventoryItemDTO inventoryItemDTO) {
        LOG.debug("Request to partially update InventoryItem : {}", inventoryItemDTO);

        return inventoryItemRepository
            .findById(inventoryItemDTO.getId())
            .map(existingInventoryItem -> {
                inventoryItemMapper.partialUpdate(existingInventoryItem, inventoryItemDTO);

                return existingInventoryItem;
            })
            .map(inventoryItemRepository::save)
            .map(inventoryItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InventoryItemDTO> findOne(Long id) {
        LOG.debug("Request to get InventoryItem : {}", id);
        return inventoryItemRepository.findById(id).map(inventoryItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete InventoryItem : {}", id);
        inventoryItemRepository.deleteById(id);
    }
}
