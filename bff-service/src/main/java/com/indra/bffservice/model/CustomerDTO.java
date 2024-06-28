package com.indra.bffservice.model;

import lombok.Data;

@Data
public class CustomerDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private String uniqueCode;
}
