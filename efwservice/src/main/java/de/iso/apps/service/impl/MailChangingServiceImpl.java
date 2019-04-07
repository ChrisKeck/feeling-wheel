package de.iso.apps.service.impl;

import de.iso.apps.contracts.Topicable;
import de.iso.apps.service.MailChangingService;
import de.iso.apps.service.dto.EmployeeDTO;
import de.iso.apps.service.dto.MailChangingDTO;
import de.iso.apps.service.mapper.MailChangingMapper;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service public class MailChangingServiceImpl implements MailChangingService {
    
    private final Logger log = LoggerFactory.getLogger(MailChangingServiceImpl.class);
    
    public MailChangingServiceImpl(Topicable<MailChangingDTO> topicableMailChanging,
                                   MailChangingMapper mailChangingMapper) {
        this.topicable = topicableMailChanging;
        this.mailChangingMapper = mailChangingMapper;
    }
    
    
    private final Topicable<MailChangingDTO> topicable;
    private final MailChangingMapper mailChangingMapper;
    
    
    @Override
    public void propagate(EmployeeDTO userDTO, EmployeeDTO oldemployee) {
        try {
            var mail = mailChangingMapper.employeeDTOToMailChangingDTO(userDTO, oldemployee);
            if (mail.hasChange()) {
                topicable.send(mail);
            }
        } catch (Exception ex) {
            log.error("Error", ex);
        }
    }
    
}
