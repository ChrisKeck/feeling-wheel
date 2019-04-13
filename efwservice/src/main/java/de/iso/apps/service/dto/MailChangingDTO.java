package de.iso.apps.service.dto;

import de.iso.apps.contracts.MailChangingEventArgs;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Builder
@Getter @Setter public class MailChangingDTO implements MailChangingEventArgs {
    private static final long serialVersionUID = 8708595976735931927L;
    private String newMail;
    private String oldMail;
    
    @Override
    public boolean hasChange() {
        return !Objects.equals(getNewMail(), getOldMail());
    }
}
