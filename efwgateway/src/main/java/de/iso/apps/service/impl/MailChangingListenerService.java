package de.iso.apps.service.impl;

import de.iso.apps.service.UserService;
import de.iso.apps.service.dto.MailChangingDTO;
import de.iso.apps.service.dto.UserDTO;
import lombok.var;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.stream.StreamSupport;

@Service public class MailChangingListenerService {
    private final Logger log = LoggerFactory.getLogger(MailChangingListenerService.class);
    private final UserService userService;
    
    
    public MailChangingListenerService(UserService service) {
        userService = service;
    }

    
    @Async
    @KafkaListener(topics = "${employee.topic-name}",
                   clientIdPrefix = "json", containerFactory = "employeeListenerContainerFactory")
    public void listenAsObject(ConsumerRecord<String, MailChangingDTO> cr, @Payload MailChangingDTO payload) {
        log.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}",
                 cr.key(),
                 typeIdHeader(cr.headers()),
                 payload,
                 cr.toString());
        try {
            if (wasMailChanged(payload)) {
                changeEmployee(payload);
            } else if (wasMailDeleted(payload)) {
                deleteEmployee(payload);
            } else if (wasMailCreated(payload)) {
                createEmployee(payload);
            }
            
        } catch (Exception e) {
            log.error("Error in Listener", e);
        }
        
    }
    
    private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                            .filter(header -> header.key().equals("__TypeId__"))
                            .findFirst()
                            .map(header -> new String(header.value()))
                            .orElse("N/A");
    }
    
    private boolean wasMailChanged(MailChangingDTO payload) {
        return StringUtils.hasText(payload.getNewMail()) && StringUtils.hasText(payload.getOldMail());
    }
    
    private void changeEmployee(MailChangingDTO payload) {
        var optionalUser = userService.getUserWithAuthoritiesByLogin(payload.getOldMail());
        if (optionalUser.isPresent() && !Objects.equals(optionalUser.get().getEmail(), payload.getNewMail())) {
            var user = optionalUser.get();
            user.setEmail(payload.getNewMail());
            userService.updateUser(user);
            log.info("User was changed by Listener");
        }
    }
    
    private boolean wasMailDeleted(MailChangingDTO payload) {
        return StringUtils.hasText(payload.getOldMail()) && !StringUtils.hasText(payload.getNewMail());
    }
    
    private void deleteEmployee(MailChangingDTO payload) {
        var employee = userService.getUserWithAuthoritiesByEmail(payload.getOldMail());
        employee.ifPresent(item -> {
            userService.deleteUser(item.getLogin());
            log.info("User was deleted by Listener");
        });
    }
    
    private boolean wasMailCreated(MailChangingDTO payload) {
        return StringUtils.hasText(payload.getNewMail()) && !StringUtils.hasText(payload.getOldMail());
    }
    
    private void createEmployee(MailChangingDTO payload) {
        var repemployee = userService.getUserWithAuthoritiesByEmail(payload.getNewMail());
        if (!repemployee.isPresent()) {
            var employee = new UserDTO();
            employee.setEmail(payload.getNewMail());
            employee.setLogin(payload.getNewMail());
            userService.createUser(employee);
            log.info("User was created by Listener");
        }
    }
    
}
    
