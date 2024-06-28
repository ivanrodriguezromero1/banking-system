package com.indra.productservice.model.mapper;

import com.indra.productservice.model.Product;
import com.indra.productservice.model.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDTO productToProductDTO(Product product);

    Product productDTOToProduct(ProductDTO productDTO);
}
