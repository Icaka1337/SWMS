package com.uni.swms.service.mapper;

import com.uni.swms.domain.SystemLog;
import com.uni.swms.service.dto.SystemLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemLog} and its DTO {@link SystemLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemLogMapper extends EntityMapper<SystemLogDTO, SystemLog> {}
