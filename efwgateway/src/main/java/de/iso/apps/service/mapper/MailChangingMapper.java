package de.iso.apps.service.mapper;

import de.iso.apps.contracts.Userable;
import de.iso.apps.service.dto.MailChangingDTO;
import lombok.var;
import org.springframework.stereotype.Component;

@Component public class MailChangingMapper {
    
    public MailChangingDTO toMailChangingDTO(Userable newMailUserDTO, Userable oldMailUserDTO) {
        var mailBuilder = MailChangingDTO.builder();
        if (newMailUserDTO != null) {
            mailBuilder.newMail(newMailUserDTO.getEmail());
        }
        if (oldMailUserDTO != null) {
            mailBuilder.oldMail(oldMailUserDTO.getEmail());
        }
        return mailBuilder.build();
    }
}
