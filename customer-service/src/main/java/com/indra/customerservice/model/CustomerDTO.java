package com.indra.customerservice.model;

import lombok.Data;

@Data
public class CustomerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private String uniqueCode;
}
