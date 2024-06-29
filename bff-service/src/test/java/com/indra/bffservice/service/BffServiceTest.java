package com.indra.bffservice.service;

import com.indra.bffservice.model.CustomerDTO;
import com.indra.bffservice.model.CustomerProductDTO;
import com.indra.bffservice.model.ProductDTO;
import com.indra.bankingstarter.encryption.EncryptionService;
import com.indra.bankingstarter.util.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class BffServiceTest {

    @Mock
    private CustomerServiceClientContext customerServiceClientContext;

    @Mock
    private ProductServiceClientContext productServiceClientContext;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private BffService bffService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCustomerProduct_shouldReturnCustomerProduct() {
        CustomerDTO mockCustomer = new CustomerDTO("1", "Ivan", "Rodriguez", "DNI", "46794348", "traceId");
        ProductDTO mockProduct = new ProductDTO("1", "Product A", "Type A", 100.0, "traceId");
        List<ProductDTO> mockProductList = Collections.singletonList(mockProduct);

        given(encryptionService.decrypt(anyString())).willReturn("decryptedCode");
        given(customerServiceClientContext.getCustomerByUniqueCode("decryptedCode")).willReturn(Mono.just(mockCustomer));
        given(productServiceClientContext.getProductByUniqueCode("1")).willReturn(Mono.just(mockProduct));

        Mono<CustomerProductDTO> result = bffService.getCustomerProduct("encryptedCode");

        StepVerifier.create(result)
                .assertNext(customerProductDTO -> {
                    assertThat(customerProductDTO.getCustomerId()).isEqualTo("1");
                    assertThat(customerProductDTO.getFirstName()).isEqualTo("Ivan");
                    assertThat(customerProductDTO.getLastName()).isEqualTo("Rodriguez");
                    assertThat(customerProductDTO.getDocumentType()).isEqualTo("DNI");
                    assertThat(customerProductDTO.getDocumentNumber()).isEqualTo("46794348");
                    assertThat(customerProductDTO.getProducts()).isEqualTo(mockProductList);
                })
                .verifyComplete();
    }

    @Test
    void getCustomerProduct_shouldThrowCustomExceptionWhenDecryptionFails() {
        given(encryptionService.decrypt(anyString())).willThrow(new CustomException("DECRYPTION_ERROR", "Error decrypting unique code"));

        Mono<CustomerProductDTO> result = bffService.getCustomerProduct("encryptedCode");

        StepVerifier.create(result)
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    void getCustomerProduct_shouldReturnErrorWhenCustomerNotFound() {
        given(encryptionService.decrypt(anyString())).willReturn("decryptedCode");
        given(customerServiceClientContext.getCustomerByUniqueCode("decryptedCode")).willReturn(Mono.empty());

        Mono<CustomerProductDTO> result = bffService.getCustomerProduct("encryptedCode");

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();
    }
}
