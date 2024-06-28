package com.indra.bffservice.service;

import com.indra.bffservice.model.CustomerProductDTO;
import com.indra.bffservice.model.mapper.CustomerProductMapper;
import com.indra.bffservice.model.CustomerDTO;
import com.indra.bffservice.model.ProductDTO;
import com.indra.bankingstarter.encryption.EncryptionService;
import com.indra.bankingstarter.util.CustomException;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BffService {

    private final CustomerServiceClient customerServiceClient;
    private final ProductServiceClient productServiceClient;
    private final CustomerProductMapper customerProductMapper = CustomerProductMapper.INSTANCE;
    private final EncryptionService encryptionService;

    public Mono<CustomerProductDTO> getCustomerProduct(String uniqueCode) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        String decryptedUniqueCode;
        try {
            decryptedUniqueCode = encryptionService.decrypt(uniqueCode);
        } catch (Exception e) {
            throw new CustomException("DECRYPTION_ERROR", "Error decrypting unique code", e);
        }

        Mono<CustomerDTO> customerMono = customerServiceClient.getCustomerByUniqueCode(decryptedUniqueCode)
                .contextWrite(ctx -> ctx.put("traceId", traceId));
        Mono<List<ProductDTO>> productsMono = customerMono
                .flatMapMany(customer -> productServiceClient.getProductsByCustomerId(customer.getId())
                        .contextWrite(ctx -> ctx.put("traceId", traceId)))
                .collectList();

        return Mono.zip(customerMono, productsMono)
                .map(tuple -> {
                    CustomerProductDTO customerProductDTO = new CustomerProductDTO();
                    CustomerDTO customer = tuple.getT1();
                    List<ProductDTO> products = tuple.getT2();

                    customerProductDTO.setCustomerId(customer.getId());
                    customerProductDTO.setFirstName(customer.getFirstName());
                    customerProductDTO.setLastName(customer.getLastName());
                    customerProductDTO.setDocumentType(customer.getDocumentType());
                    customerProductDTO.setDocumentNumber(customer.getDocumentNumber());
                    customerProductDTO.setUniqueCode(customer.getUniqueCode());
                    customerProductDTO.setProducts(products);

                    return customerProductDTO;
                })
                .doFinally(signalType -> MDC.remove("traceId"));
    }
}
