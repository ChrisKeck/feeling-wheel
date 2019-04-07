package de.iso.apps.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter @Builder public class MailChangingDTO {
    private String newMail;
    private String oldMail;
    
    public boolean hasChanged() {
        
        return !Objects.equals(getNewMail(), getOldMail());
    }
}
