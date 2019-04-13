package de.iso.apps.contracts;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter public class MailChangingDTOEventArgs implements MailChangingEventArgs {
    private static final long serialVersionUID = -301157037999041499L;
    private String newMail;
    private String oldMail;
    
    public MailChangingDTOEventArgs() {
    }
    
    @Override
    public boolean hasChange() {
        
        return !Objects.equals(getNewMail(), getOldMail());
    }
}
