package de.iso.apps.service;

import de.iso.apps.config.Constants;
import de.iso.apps.contracts.Topicable;
import de.iso.apps.domain.User;
import de.iso.apps.repository.UserRepository;
import de.iso.apps.repository.search.UserSearchRepository;
import de.iso.apps.service.dto.MailChangingDTO;
import de.iso.apps.service.mapper.UserMapper;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

@Service public class MailChangingService {
    private final Logger log = LoggerFactory.getLogger(MailChangingService.class);
    
    private final Topicable<MailChangingDTO> topicable;
    private final UserRepository userRepository;
    private final UserSearchRepository userSearchRepository;
    private final UserMapper userMapper;
    private CountDownLatch latch;
    
    public MailChangingService(Topicable<MailChangingDTO> topicable,
                               UserRepository userRepository,
                               UserSearchRepository userSearchRepository,
                               UserMapper userMapper) {
        this.topicable = topicable;
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.userMapper = userMapper;
    }
    
    
    public void propagate(MailChangingDTO userDTO) {
        try {
            latch = new CountDownLatch(topicable.getRequestsPerMessage());
            for (int i = topicable.getRequestsPerMessage(); i >= 0; i--) {
                topicable.getKafkaTemplate().send(topicable.getTopic(), String.valueOf(i), userDTO);
            }
            if (latch.await(60, TimeUnit.SECONDS)) {
                log.info("If await is true");
            }
            log.info("All messages received");
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }
    
    @KafkaListener(topics = Constants.TOPIC_MAIL_CHANGING,
                   clientIdPrefix = "json",
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
        var employee = userRepository.findOneByEmailIgnoreCase(payload.getOldMail());
        if (employee.isPresent() && employee.get().getEmail() != payload.getNewMail()) {
            employee.get().setEmail(payload.getNewMail());
            userRepository.save(employee.get());
            userSearchRepository.save(employee.get());
            log.info("User was changed by Listener");
        }
    }
    
    private void deleteEmployee(@Payload MailChangingDTO payload) {
        var employee = userRepository.findOneByEmailIgnoreCase(payload.getOldMail());
        employee.ifPresent(item -> {
            userRepository.deleteById(item.getId());
            userSearchRepository.deleteById(item.getId());
            log.info("User was deleted by Listener");
        });
    }
    
    private void createEmployee(MailChangingDTO payload) {
        var repemployee = userRepository.findOneByEmailIgnoreCase(payload.getNewMail());
        if (!repemployee.isPresent()) {
            var employee = new User();
            employee.setEmail(payload.getNewMail());
            employee.setLogin(payload.getNewMail());
            employee.setPassword(payload.getNewMail());
            userRepository.save(employee);
            userSearchRepository.save(employee);
            log.info("User was created by Listener");
        }
    }
    
}
    
