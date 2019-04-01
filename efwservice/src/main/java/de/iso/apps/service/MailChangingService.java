package de.iso.apps.service;

import de.iso.apps.service.dto.MailChangingDTO;

public interface MailChangingService {
    void propagate(MailChangingDTO userDTO);
}
