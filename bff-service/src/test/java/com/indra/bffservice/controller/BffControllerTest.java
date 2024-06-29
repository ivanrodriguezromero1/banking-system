package com.indra.bffservice.controller;

import com.indra.bffservice.model.CustomerProductDTO;
import com.indra.bffservice.service.BffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToController;

class BffControllerTest {

    @Mock
    private BffService bffService;

    @InjectMocks
    private BffController bffController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = bindToController(bffController).build();
    }

    @Test
    void getCustomerProduct_shouldReturnCustomerProduct() {
        CustomerProductDTO mockCustomerProduct = new CustomerProductDTO();

        given(bffService.getCustomerProduct(anyString())).willReturn(Mono.just(mockCustomerProduct));

        webTestClient.get()
                .uri("/bff/encryptedCode")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerProductDTO.class)
                .isEqualTo(mockCustomerProduct);
    }

    @Test
    void getCustomerProduct_shouldReturnNotFoundWhenCustomerProductDoesNotExist() {
        given(bffService.getCustomerProduct(anyString())).willReturn(Mono.empty());

        webTestClient.get()
                .uri("/bff/encryptedCode")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getCustomerProduct_shouldHandleError() {
        given(bffService.getCustomerProduct(anyString())).willReturn(Mono.error(new RuntimeException("Error occurred")));

        webTestClient.get()
                .uri("/bff/encryptedCode")
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
