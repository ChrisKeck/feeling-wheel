package de.iso.apps.service.impl;

import de.iso.apps.service.EmployeeService;
import de.iso.apps.service.dto.EmployeeDTO;
import de.iso.apps.service.dto.MailChangingDTO;
import lombok.var;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.stream.StreamSupport;

@Service public class MailChangingListenerService {
    
    private final Logger log = LoggerFactory.getLogger(MailChangingListenerService.class);
    private final EmployeeService employeeService;
    
    public MailChangingListenerService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                            .filter(header -> header.key().equals("__TypeId__"))
                            .findFirst()
                            .map(header -> new String(header.value()))
                            .orElse("N/A");
    }
    
    @KafkaListener(topics = "${tpd.topic-name}",
                   clientIdPrefix = "json", containerFactory = "kafkaListenerContainerFactory", groupId = "efwservice")
    public void listenAsObject(ConsumerRecord<String, MailChangingDTO> cr, @Payload MailChangingDTO payload) {
        
        try {
            if (StringUtils.hasText(payload.getNewMail()) && StringUtils.hasText(payload.getOldMail())) {
                changeEmployee(payload);
            } else if (StringUtils.hasText(payload.getOldMail()) && !StringUtils.hasText(payload.getNewMail())) {
                deleteEmployee(payload);
            } else if (StringUtils.hasText(payload.getNewMail()) && !StringUtils.hasText(payload.getOldMail())) {
                createEmployee(payload);
            }
            
        } catch (Exception e) {
            log.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}",
                     cr.key(),
                     typeIdHeader(cr.headers()),
                     payload,
                     cr.toString());
            log.error("Error in Listener", e);
        }
    }
    
    private void changeEmployee(MailChangingDTO payload) {
        Optional<EmployeeDTO> employeeDTO = getEmployeeDTO(payload.getOldMail());
        employeeDTO.ifPresent(item -> {
            item.setEmail(payload.getNewMail());
            employeeService.save(item);
            log.info("Employee was changed by Listener");
        });
    }
    
    private Page<EmployeeDTO> getEmployeeDTOS(String payload) {
        return employeeService.search("email=" + payload, Pageable.unpaged());
    }
    
    private void deleteEmployee(MailChangingDTO payload) {
        Optional<EmployeeDTO> employee = getEmployeeDTO(payload.getOldMail());
        employee.ifPresent(item -> {
            employeeService.delete(item.getId());
            log.info("Employee was deleted by Listener");
        });
    }
    
    private Optional<EmployeeDTO> getEmployeeDTO(String payload) {
        Page<EmployeeDTO> employees = getEmployeeDTOS(payload);
        return employees.stream().findFirst();
    }
    
    private void createEmployee(MailChangingDTO payload) {
        
        var repemployee = getEmployeeDTO(payload.getNewMail());
        if (!repemployee.isPresent()) {
            var employee = new EmployeeDTO();
            employee.setEmail(payload.getNewMail());
            employeeService.save(employee);
            log.info("Employee was created by Listener");
        }
    }
}
