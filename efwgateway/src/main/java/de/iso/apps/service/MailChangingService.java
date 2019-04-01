package de.iso.apps.service;

import de.iso.apps.contracts.Topicable;
import de.iso.apps.service.dto.MailChangingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service public class MailChangingService {
    private final Logger log = LoggerFactory.getLogger(MailChangingService.class);
    
    private final Topicable<MailChangingDTO> topicable;
    private CountDownLatch latch;
    
    @Autowired
    public MailChangingService(Topicable<MailChangingDTO> topicable) {
        this.topicable = topicable;
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
    
    
}
    
