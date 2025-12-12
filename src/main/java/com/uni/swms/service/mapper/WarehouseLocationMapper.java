package com.uni.swms.service.mapper;

import com.uni.swms.domain.WarehouseLocation;
import com.uni.swms.service.dto.WarehouseLocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WarehouseLocation} and its DTO {@link WarehouseLocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface WarehouseLocationMapper extends EntityMapper<WarehouseLocationDTO, WarehouseLocation> {}
