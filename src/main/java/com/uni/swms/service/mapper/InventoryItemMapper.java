package com.uni.swms.service.mapper;

import com.uni.swms.domain.InventoryItem;
import com.uni.swms.domain.Product;
import com.uni.swms.domain.WarehouseLocation;
import com.uni.swms.service.dto.InventoryItemDTO;
import com.uni.swms.service.dto.ProductDTO;
import com.uni.swms.service.dto.WarehouseLocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InventoryItem} and its DTO {@link InventoryItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventoryItemMapper extends EntityMapper<InventoryItemDTO, InventoryItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "location", source = "location", qualifiedByName = "warehouseLocationId")
    InventoryItemDTO toDto(InventoryItem s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("warehouseLocationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WarehouseLocationDTO toDtoWarehouseLocationId(WarehouseLocation warehouseLocation);
}
