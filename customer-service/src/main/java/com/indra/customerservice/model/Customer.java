package com.indra.customerservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("customer")
public class Customer {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String documentType;
    private String documentNumber;
    private String uniqueCode;
}
