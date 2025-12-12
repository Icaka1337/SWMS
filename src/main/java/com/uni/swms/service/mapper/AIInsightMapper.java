package com.uni.swms.service.mapper;

import com.uni.swms.domain.AIInsight;
import com.uni.swms.domain.Product;
import com.uni.swms.service.dto.AIInsightDTO;
import com.uni.swms.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AIInsight} and its DTO {@link AIInsightDTO}.
 */
@Mapper(componentModel = "spring")
public interface AIInsightMapper extends EntityMapper<AIInsightDTO, AIInsight> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    AIInsightDTO toDto(AIInsight s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
