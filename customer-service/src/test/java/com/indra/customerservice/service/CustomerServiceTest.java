package com.indra.customerservice.service;

import com.indra.customerservice.model.Customer;
import com.indra.customerservice.model.CustomerDTO;
import com.indra.customerservice.model.mapper.CustomerMapper;
import com.indra.customerservice.repository.CustomerRepository;
import com.indra.bankingstarter.util.CustomException;
import com.indra.bankingstarter.encryption.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private CustomerService customerService;

    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId("1");
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setDocumentType("ID");
        customer.setDocumentNumber("12345");

        customerDTO = customerMapper.customerToCustomerDTO(customer);
    }

    @Test
    void getCustomerByUniqueCode_shouldReturnCustomer() {
        given(encryptionService.decrypt(anyString())).willReturn("decryptedCode");
        given(customerRepository.findByUniqueCode(anyString())).willReturn(Mono.just(customer));

        Mono<CustomerDTO> result = customerService.getCustomerByUniqueCode("encryptedCode");

        StepVerifier.create(result)
                .expectNext(customerDTO)
                .verifyComplete();
    }

    @Test
    void getCustomerByUniqueCode_shouldThrowCustomException() {
        given(encryptionService.decrypt(anyString())).willThrow(new CustomException("DECRYPTION_ERROR", "Error decrypting unique code"));

        Mono<CustomerDTO> result = customerService.getCustomerByUniqueCode("encryptedCode");

        StepVerifier.create(result)
                .expectError(CustomException.class)
                .verify();
    }
}
