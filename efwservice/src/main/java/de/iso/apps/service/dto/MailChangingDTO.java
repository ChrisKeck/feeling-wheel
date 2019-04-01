package de.iso.apps.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter public class MailChangingDTO {
    private String newMail;
    private String oldMail;
}
