package com.indra.customerservice.controller;

import com.indra.customerservice.model.CustomerDTO;
import com.indra.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{uniqueCode}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomerByUniqueCode(@PathVariable String uniqueCode) {
        return customerService.getCustomerByUniqueCode(uniqueCode)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
