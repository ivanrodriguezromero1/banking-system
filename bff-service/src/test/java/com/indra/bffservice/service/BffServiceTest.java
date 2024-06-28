package com.indra.bffservice.service;

import com.indra.bffservice.model.CustomerProductDTO;
import com.indra.bffservice.model.CustomerDTO;
import com.indra.bffservice.model.ProductDTO;
import com.indra.bffservice.model.mapper.CustomerProductMapper;
import com.indra.bankingstarter.util.CustomException;
import com.indra.bankingstarter.encryption.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class BffServiceTest {

    @Mock
    private CustomerServiceClient customerServiceClient;

    @Mock
    private ProductServiceClient productServiceClient;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private BffService bffService;

    private final CustomerProductMapper customerProductMapper = CustomerProductMapper.INSTANCE;

    private CustomerDTO customerDTO;
    private ProductDTO productDTO;
    private CustomerProductDTO customerProductDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerDTO = new CustomerDTO();
        customerDTO.setId("1");
        customerDTO.setFirstName("John");
        customerDTO.setLastName("Doe");
        customerDTO.setDocumentType("ID");
        customerDTO.setDocumentNumber("12345");

        productDTO = new ProductDTO();
        productDTO.setId("1");
        productDTO.setName("Savings Account");
        productDTO.setType("Account");
        productDTO.setBalance(1000.0);

        List<ProductDTO> products = Arrays.asList(productDTO);

        customerProductDTO = new CustomerProductDTO();
        customerProductDTO.setCustomerId(customerDTO.getId());
        customerProductDTO.setFirstName(customerDTO.getFirstName());
        customerProductDTO.setLastName(customerDTO.getLastName());
        customerProductDTO.setDocumentType(customerDTO.getDocumentType());
        customerProductDTO.setDocumentNumber(customerDTO.getDocumentNumber());
        customerProductDTO.setProducts(products);
    }

    @Test
    void getCustomerProduct_shouldReturnCustomerProduct() {
        given(encryptionService.decrypt(anyString())).willReturn("decryptedCode");
        given(customerServiceClient.getCustomerByUniqueCode(anyString())).willReturn(Mono.just(customerDTO));
        given(productServiceClient.getProductsByCustomerId(anyString())).willReturn(Mono.just(productDTO).flux().collectList());

        Mono<CustomerProductDTO> result = bffService.getCustomerProduct("encryptedCode");

        StepVerifier.create(result)
                .expectNext(customerProductDTO)
                .verifyComplete();
    }

    @Test
    void getCustomerProduct_shouldThrowCustomException() {
        given(encryptionService.decrypt(anyString())).willThrow(new CustomException("DECRYPTION_ERROR", "Error decrypting unique code"));

        Mono<CustomerProductDTO> result = bffService.getCustomerProduct("encryptedCode");

        StepVerifier.create(result)
                .expectError(CustomException.class)
                .verify();
    }
}
