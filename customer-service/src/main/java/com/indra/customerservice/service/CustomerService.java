package com.indra.customerservice.service;

import com.indra.bankingstarter.encryption.EncryptionService;
import com.indra.customerservice.model.CustomerDTO;
import com.indra.customerservice.model.mapper.CustomerMapper;
import com.indra.customerservice.repository.CustomerRepository;
import com.indra.customerservice.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper = CustomerMapper.INSTANCE;
    private final EncryptionService encryptionService;

    public Mono<CustomerDTO> getCustomerByUniqueCode(String uniqueCode) {
        String decryptedUniqueCode;
        try {
            decryptedUniqueCode = encryptionService.decrypt(uniqueCode);
        } catch (Exception e) {
            throw new CustomException("DECRYPTION_ERROR", "Error decrypting unique code", e);
        }
        return customerRepository.findByUniqueCode(decryptedUniqueCode)
                .map(customerMapper::customerToCustomerDTO)
                .switchIfEmpty(Mono.error(new CustomException("NOT_FOUND", "Customer not found")));
    }
}
