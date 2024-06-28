package com.indra.productservice.model;

import lombok.Data;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String type;
    private Double balance;
}
