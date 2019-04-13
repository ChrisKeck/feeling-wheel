package de.iso.apps.service.dto;

import de.iso.apps.contracts.MailChangingEventArgs;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoFactory {
    public static MailChangingEventArgs createArgs(String newMail, String oldMail) {
        return new MailChangingDTO(newMail, oldMail);
    }
}
