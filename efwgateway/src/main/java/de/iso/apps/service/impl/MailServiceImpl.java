package de.iso.apps.service.impl;

import de.iso.apps.service.MailService;
import de.iso.apps.service.dto.UserDTO;
import de.iso.apps.service.mapper.UserMapper;
import io.github.jhipster.config.JHipsterProperties;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service public class MailServiceImpl implements MailService {
    
    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";
    private final Logger log = LoggerFactory.getLogger(MailServiceImpl.class);
    private final JHipsterProperties jHipsterProperties;
    
    private final JavaMailSender javaMailSender;
    
    private final MessageSource messageSource;
    
    private final SpringTemplateEngine templateEngine;
    private final UserMapper userMapper;
    
    public MailServiceImpl(JHipsterProperties jHipsterProperties,
                           JavaMailSender javaMailSender,
                           MessageSource messageSource,
                           SpringTemplateEngine templateEngine,
                           UserMapper userMapper) {
        
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.userMapper = userMapper;
    }
    
    @Override
    @Async
    public void sendActivationEmail(UserDTO user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }
    
    @Override
    @Async
    public void sendEmailFromTemplate(UserDTO user, String templateName, String titleKey) {
        var mapped = userMapper.userDTOToUser(user);
        
        Locale locale = Locale.forLanguageTag(mapped.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, mapped);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
        
    }
    
    @Override
    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                  isMultipart,
                  isHtml,
                  to,
                  subject,
                  content);
        
        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }
    
    @Override
    @Async
    public void sendCreationEmail(UserDTO user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }
    
    @Override
    @Async
    public void sendPasswordResetMail(UserDTO user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }
}
