package de.iso.apps.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Builder
@Getter @Setter public class MailChangingDTO {
    private String newMail;
    private String oldMail;
    
    public boolean hasChange() {
        return !Objects.equals(getNewMail(), getOldMail());
    }
}
