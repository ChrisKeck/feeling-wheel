package de.iso.apps.service.mapper;

import de.iso.apps.contracts.MailChangingDTOEventArgs;
import de.iso.apps.contracts.MailChangingEventArgs;
import de.iso.apps.service.dto.EmployeeDTO;
import lombok.var;
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
        var mail = new MailChangingDTOEventArgs();
        mail.setNewMail(newMail);
        mail.setOldMail(oldMail);
        return mail;
    }
}
