package de.iso.apps.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder public class MailChangingDTO {
    private String newMail;
    private String oldMail;
}
