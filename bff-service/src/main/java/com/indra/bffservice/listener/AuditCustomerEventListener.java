package com.indra.bffservice.listener;

import com.indra.bffservice.event.CustomerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditCustomerEventListener {

    @EventListener
    public void handleCustomerEvent(CustomerEvent event) {
        try {
            log.info("Audit log - TraceId: {}, CustomerId: {}, FirstName: {}, LastName: {}, DocumentType: {}, DocumentNumber: {}",
                    event.getTraceId(),
                    event.getCustomer().getId(),
                    event.getCustomer().getFirstName(),
                    event.getCustomer().getLastName(),
                    event.getCustomer().getDocumentType(),
                    event.getCustomer().getDocumentNumber());
        } catch (Exception e) {
            log.error("Error while processing CustomerEvent for customerId: {}", event.getCustomer().getId(), e);
        }
    }
}
