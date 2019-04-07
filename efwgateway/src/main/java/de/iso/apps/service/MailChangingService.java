package de.iso.apps.service;

import de.iso.apps.service.dto.UserDTO;

public interface MailChangingService {
    void propagate(UserDTO newUser, UserDTO oldUser);
}
