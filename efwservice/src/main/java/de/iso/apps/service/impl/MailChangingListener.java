package de.iso.apps.service.impl;

import de.iso.apps.config.Constants;
import de.iso.apps.domain.Employee;
import de.iso.apps.repository.EmployeeRepository;
import de.iso.apps.repository.search.EmployeeSearchRepository;
import de.iso.apps.service.dto.MailChangingDTO;
import lombok.var;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.CountDownLatch;
import java.util.stream.StreamSupport;

@Service public class MailChangingListener {
    
    private final Logger log = LoggerFactory.getLogger(MailChangingListener.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeSearchRepository employeeSearchRepository;
    private CountDownLatch latch = new CountDownLatch(1);
    
    public MailChangingListener(EmployeeRepository employeeRepository,
                                EmployeeSearchRepository employeeSearchRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeSearchRepository = employeeSearchRepository;
    }
    
    @KafkaListener(topics = Constants.TOPIC_MAIL_CHANGING, clientIdPrefix = "json",
                   containerFactory = "kafkaListenerContainerFactory")
    public void listenAsObject(ConsumerRecord<String, MailChangingDTO> cr, @Payload MailChangingDTO payload) {
        log.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}",
                 cr.key(),
                 typeIdHeader(cr.headers()),
                 payload,
                 cr.toString());
        try {
            if (StringUtils.hasText(payload.getNewMail()) && StringUtils.hasText(payload.getOldMail())) {
                changeEmployee(payload);
            } else if (StringUtils.hasText(payload.getOldMail()) && !StringUtils.hasText(payload.getNewMail())) {
                deleteEmployee(payload);
            } else if (StringUtils.hasText(payload.getNewMail()) && !StringUtils.hasText(payload.getOldMail())) {
                createEmployee(payload);
            }
            
        } catch (Exception e) {
            log.error("Error in Listener", e);
        }
        
        latch.countDown();
        
    }
    
    private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                            .filter(header -> header.key().equals("__TypeId__"))
                            .findFirst()
                            .map(header -> new String(header.value()))
                            .orElse("N/A");
    }
    
    private void changeEmployee(@Payload MailChangingDTO payload) {
        var employee = employeeSearchRepository.findOneByEmailIgnoreCase(payload.getOldMail());
        if (employee.isPresent() && employee.get().getEmail() != payload.getNewMail()) {
            employee.get().setEmail(payload.getNewMail());
            employeeRepository.save(employee.get());
            employeeSearchRepository.save(employee.get());
            log.info("Employee was changed by Listener");
        }
    }
    
    private void deleteEmployee(@Payload MailChangingDTO payload) {
        var employee = employeeSearchRepository.findOneByEmailIgnoreCase(payload.getOldMail());
        employee.ifPresent(item -> {
            employeeRepository.deleteById(item.getId());
            employeeSearchRepository.deleteById(item.getId());
            log.info("Employee was deleted by Listener");
        });
    }
    
  /*  @KafkaListener(topics = Constants.TOPIC_MAIL_CHANGING, clientIdPrefix = "string",
                   containerFactory = "kafkaListenerStringContainerFactory")
    public void listenasString(ConsumerRecord<String, String> cr,
                               @Payload String payload) {
        log.info("Logger 2 [String] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                 typeIdHeader(cr.headers()), payload, cr.toString());
        latch.countDown();
    }*/
    /*
    @KafkaListener(topics = "advice-topic", clientIdPrefix = "bytearray",
                   containerFactory = "kafkaListenerByteArrayContainerFactory")
    public void listenAsByteArray(ConsumerRecord<String, byte[]> cr,
                                  @Payload byte[] payload) {
        log.info("Logger 3 [ByteArray] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
                 typeIdHeader(cr.headers()), payload, cr.toString());
        latch.countDown();
    }*/
    
    private void createEmployee(@Payload MailChangingDTO payload) {
        var repemployee = employeeSearchRepository.findOneByEmailIgnoreCase(payload.getNewMail());
        if (!repemployee.isPresent()) {
            var employee = new Employee();
            employee.setEmail(payload.getNewMail());
            employeeRepository.save(employee);
            employeeSearchRepository.save(employee);
            log.info("Employee was created by Listener");
        }
    }
}
