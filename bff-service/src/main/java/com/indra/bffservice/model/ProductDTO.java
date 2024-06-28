package com.indra.bffservice.model;

import lombok.Data;

@Data
public class ProductDTO {

    private String id;
    private String name;
    private String type;
    private Double balance;
}