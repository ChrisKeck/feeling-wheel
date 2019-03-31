package de.iso.apps.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailChangingDTO {
    private String newMail;
    private String oldMail;
}
