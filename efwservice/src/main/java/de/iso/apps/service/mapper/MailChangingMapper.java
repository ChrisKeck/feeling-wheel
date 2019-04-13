package de.iso.apps.service.mapper;

import de.iso.apps.contracts.MailChangingEventArgs;
import de.iso.apps.service.dto.DtoFactory;
import de.iso.apps.service.dto.EmployeeDTO;
import org.springframework.stereotype.Component;

@Component public class MailChangingMapper {
    
    public MailChangingEventArgs employeeDTOToMailChangingDTO(EmployeeDTO newMailUserDTO,
                                                              EmployeeDTO oldMailUserDTO) {
        String newMail = null;
        String oldMail = null;
        if (newMailUserDTO != null) {
            newMail = newMailUserDTO.getEmail();
        }
        if (oldMailUserDTO != null) {
            oldMail = oldMailUserDTO.getEmail();
        }
        return DtoFactory.createArgs(newMail,
                                     oldMail);
    }
}
