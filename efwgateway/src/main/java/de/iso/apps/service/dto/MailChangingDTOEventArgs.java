package de.iso.apps.service.dto;

import de.iso.apps.contracts.MailChangingEventArgs;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter @Builder public class MailChangingDTOEventArgs implements MailChangingEventArgs {
    private static final long serialVersionUID = -301157037999041499L;
    private String newMail;
    private String oldMail;
    
    @Override
    public boolean hasChange() {
        
        return !Objects.equals(getNewMail(), getOldMail());
    }
}
