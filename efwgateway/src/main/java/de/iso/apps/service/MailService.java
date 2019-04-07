package de.iso.apps.service;

import de.iso.apps.service.dto.UserDTO;

public interface MailService {
    void sendActivationEmail(UserDTO user);
    
    void sendEmailFromTemplate(UserDTO user, String templateName, String titleKey);
    
    void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml);
    
    void sendCreationEmail(UserDTO user);
    
    void sendPasswordResetMail(UserDTO user);
}
