package com.indra.bffservice.model.mapper;

import com.indra.bffservice.model.CustomerProduct;
import com.indra.bffservice.model.CustomerProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerProductMapper {
    CustomerProductMapper INSTANCE = Mappers.getMapper(CustomerProductMapper.class);

    CustomerProductDTO customerProductToCustomerProductDTO(CustomerProduct customerProduct);
    CustomerProduct customerProductDTOToCustomerProduct(CustomerProductDTO customerProductDTO);
}
