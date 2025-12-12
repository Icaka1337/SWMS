package com.uni.swms.service.mapper;

import com.uni.swms.domain.Product;
import com.uni.swms.domain.Transaction;
import com.uni.swms.domain.WarehouseLocation;
import com.uni.swms.service.dto.ProductDTO;
import com.uni.swms.service.dto.TransactionDTO;
import com.uni.swms.service.dto.WarehouseLocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "sourceLocation", source = "sourceLocation", qualifiedByName = "warehouseLocationId")
    @Mapping(target = "targetLocation", source = "targetLocation", qualifiedByName = "warehouseLocationId")
    TransactionDTO toDto(Transaction s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("warehouseLocationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WarehouseLocationDTO toDtoWarehouseLocationId(WarehouseLocation warehouseLocation);
}
