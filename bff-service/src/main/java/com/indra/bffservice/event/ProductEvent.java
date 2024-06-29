package com.indra.bffservice.event;

import com.indra.bffservice.model.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductEvent {
    private String traceId;
    private ProductDTO product;
}
