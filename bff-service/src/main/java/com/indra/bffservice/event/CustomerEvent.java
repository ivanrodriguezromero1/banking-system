package com.indra.bffservice.event;

import com.indra.bffservice.model.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerEvent {
    private String traceId;
    private CustomerDTO customer;
}
