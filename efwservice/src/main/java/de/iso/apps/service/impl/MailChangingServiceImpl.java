package de.iso.apps.service.impl;

import de.iso.apps.contracts.Topicable;
import de.iso.apps.service.MailChangingService;
import de.iso.apps.service.dto.MailChangingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service public class MailChangingServiceImpl implements MailChangingService {
    
    private final Logger log = LoggerFactory.getLogger(MailChangingServiceImpl.class);
    
    public MailChangingServiceImpl(Topicable<MailChangingDTO> topicable) {
        this.topicable = topicable;
    }
    
    
    private final Topicable<MailChangingDTO> topicable;
    
    
    @Override
    public void propagate(MailChangingDTO userDTO) {
        try {
            if (userDTO.hasChange()) {
                topicable.getKafkaTemplate().send(topicable.getTopic(), userDTO);
            }
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }
    
}
