package de.iso.apps.service;

import de.iso.apps.contracts.Topicable;
import de.iso.apps.service.dto.MailChangingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service public class MailChangingService {
    private final Logger log = LoggerFactory.getLogger(MailChangingService.class);
    private static AtomicInteger counter = new AtomicInteger(1);
    private final Topicable<MailChangingDTO> topicable;
    
    
    public MailChangingService(Topicable<MailChangingDTO> topicable) {
        this.topicable = topicable;
    }
    
    
    @Async
    public void propagate(MailChangingDTO userDTO) {
        try {
            
            topicable.getKafkaTemplate().send(topicable.getTopic(), String.valueOf(counter.getAndAdd(1)), userDTO);
            
            
            log.info("All messages send");
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }
    
    
}
    
