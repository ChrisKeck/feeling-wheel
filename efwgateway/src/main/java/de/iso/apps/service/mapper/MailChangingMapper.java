package de.iso.apps.service.mapper;

import de.iso.apps.contracts.Mail;
import de.iso.apps.contracts.MailChangingEventArgs;
import de.iso.apps.service.dto.MailChangingDTOEventArgs;
import lombok.var;
import org.springframework.stereotype.Component;

@Component public class MailChangingMapper {
    
    public MailChangingEventArgs toMailChangingDTO(Mail newMailUserDTO, Mail oldMailUserDTO) {
        var mailBuilder = MailChangingDTOEventArgs.builder();
        if (newMailUserDTO != null) {
            mailBuilder.newMail(newMailUserDTO.getEmail());
        }
        if (oldMailUserDTO != null) {
            mailBuilder.oldMail(oldMailUserDTO.getEmail());
        }
        return mailBuilder.build();
    }
}
