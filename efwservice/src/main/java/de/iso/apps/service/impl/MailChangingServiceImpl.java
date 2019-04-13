package de.iso.apps.service.impl;

import de.iso.apps.contracts.MailChangingEventArgs;
import de.iso.apps.contracts.TopicDistributor;
import de.iso.apps.service.MailChangingService;
import de.iso.apps.service.dto.EmployeeDTO;
import de.iso.apps.service.mapper.MailChangingMapper;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service public class MailChangingServiceImpl implements MailChangingService {
    
    private final Logger log = LoggerFactory.getLogger(MailChangingServiceImpl.class);
    
    public MailChangingServiceImpl(TopicDistributor<MailChangingEventArgs> topicableMailChanging,
                                   MailChangingMapper mailChangingMapper) {
        this.topicDistributor = topicableMailChanging;
        this.mailChangingMapper = mailChangingMapper;
    }
    
    
    private final TopicDistributor<MailChangingEventArgs> topicDistributor;
    private final MailChangingMapper mailChangingMapper;
    
    
    @Override
    public void propagate(EmployeeDTO nextEmployeeDTO, EmployeeDTO prevEmployeeDTO) {
        try {
            var mail = mailChangingMapper.employeeDTOToMailChangingDTO(nextEmployeeDTO, prevEmployeeDTO);
            if (mail.hasChange()) {
                topicDistributor.send(mail);
            }
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }
    
}
