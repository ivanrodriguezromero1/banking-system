package com.indra.bffservice.controller;

import com.indra.bffservice.model.CustomerProductDTO;
import com.indra.bffservice.service.BffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bff")
@RequiredArgsConstructor
public class BffController {

    private final BffService bffService;

    @GetMapping("/{uniqueCode}")
    public Mono<ResponseEntity<CustomerProductDTO>> getCustomerProduct(@PathVariable String uniqueCode) {
        return bffService.getCustomerProduct(uniqueCode)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
