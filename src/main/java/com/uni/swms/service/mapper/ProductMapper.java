package com.uni.swms.service.mapper;

import com.uni.swms.domain.Product;
import com.uni.swms.domain.Supplier;
import com.uni.swms.service.dto.ProductDTO;
import com.uni.swms.service.dto.SupplierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "supplier", source = "supplier", qualifiedByName = "supplierId")
    ProductDTO toDto(Product s);

    @Named("supplierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SupplierDTO toDtoSupplierId(Supplier supplier);
}
