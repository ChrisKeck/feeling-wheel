package de.iso.apps.service.impl;

import de.iso.apps.contracts.MailChangingEventArgs;
import de.iso.apps.contracts.TopicDistributor;
import de.iso.apps.service.MailChangingService;
import de.iso.apps.service.dto.UserDTO;
import de.iso.apps.service.mapper.MailChangingMapper;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service public class MailChangingServiceImpl implements MailChangingService {
    private final Logger log = LoggerFactory.getLogger(MailChangingServiceImpl.class);
    private final TopicDistributor<MailChangingEventArgs> topicDistributor;
    private final MailChangingMapper mailChangingMapper;
    
    
    public MailChangingServiceImpl(
            @Qualifier("userProducer")
                    TopicDistributor<MailChangingEventArgs> topicable,
            MailChangingMapper mailChangingMapper) {
        this.topicDistributor = topicable;
        this.mailChangingMapper = mailChangingMapper;
    }
    
    
    @Override
    @Async
    public void propagate(UserDTO newUser, UserDTO oldUser) {
        try {
            var mail = mailChangingMapper.toMailChangingDTO(newUser, oldUser);
            if (mail.hasChange()) {
                topicDistributor.send(mail);
            }
            log.info("All messages send");
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }
    
    
}
    
