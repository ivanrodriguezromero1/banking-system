package com.indra.bffservice.model;

import lombok.Data;

import java.util.List;

@Data
public class CustomerProduct {

    private String customerId;
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private String uniqueCode;
    private List<ProductDTO> products;
}
