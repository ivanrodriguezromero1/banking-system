package com.indra.productservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("product")
public class Product {

    @Id
    private Long id;
    private String name;
    private String type;
    private Double balance;
}
