package de.iso.apps.service.impl;

import de.iso.apps.contracts.ExternalObservailable;
import de.iso.apps.contracts.ExternalObserver;
import de.iso.apps.contracts.MailChangingEventArgs;
import de.iso.apps.service.UserService;
import de.iso.apps.service.dto.UserDTO;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service public class MailChangingListenerService implements ExternalObservailable<String, MailChangingEventArgs> {
    private final Logger log = LoggerFactory.getLogger(MailChangingListenerService.class);
    private final UserService userService;
    private final ExternalObserver<String, MailChangingEventArgs> externalObserver;
    
    
    public MailChangingListenerService(UserService service,
                                       @Qualifier("externalObserver")
                                               ExternalObserver<String, MailChangingEventArgs> externalObserver) {
        userService = service;
        externalObserver.add(this);
        this.externalObserver = externalObserver;
    }
    
    
    private void changeEmployee(MailChangingEventArgs payload) {
        var optionalUser = userService.getUserWithAuthoritiesByLogin(payload.getOldMail());
        if (optionalUser.isPresent() && !Objects.equals(optionalUser.get().getEmail(), payload.getNewMail())) {
            var user = optionalUser.get();
            user.setEmail(payload.getNewMail());
            userService.updateUser(user);
            log.info("User was changed by Listener");
        }
    }
    
    private boolean wasMailDeleted(MailChangingEventArgs payload) {
        return StringUtils.hasText(payload.getOldMail()) && !StringUtils.hasText(payload.getNewMail());
    }
    
    private void deleteEmployee(MailChangingEventArgs payload) {
        var employee = userService.getUserWithAuthoritiesByEmail(payload.getOldMail());
        employee.ifPresent(item -> {
            userService.deleteUser(item.getLogin());
            log.info("User was deleted by Listener");
        });
    }
    
    private boolean wasMailCreated(MailChangingEventArgs payload) {
        return StringUtils.hasText(payload.getNewMail()) && !StringUtils.hasText(payload.getOldMail());
    }
    
    private void createEmployee(MailChangingEventArgs payload) {
        var repemployee = userService.getUserWithAuthoritiesByEmail(payload.getNewMail());
        if (!repemployee.isPresent()) {
            var employee = new UserDTO();
            employee.setEmail(payload.getNewMail());
            employee.setLogin(payload.getNewMail());
            userService.createUser(employee);
            log.info("User was created by Listener");
        }
    }
    
    private boolean wasMailChanged(MailChangingEventArgs payload) {
        return StringUtils.hasText(payload.getNewMail()) && StringUtils.hasText(payload.getOldMail());
    }
    
    @Override
    public void valueChanged(String s, MailChangingEventArgs mailChangingEventArgs) {
        if (wasMailChanged(mailChangingEventArgs)) {
            changeEmployee(mailChangingEventArgs);
        } else if (wasMailDeleted(mailChangingEventArgs)) {
            deleteEmployee(mailChangingEventArgs);
        } else if (wasMailCreated(mailChangingEventArgs)) {
            createEmployee(mailChangingEventArgs);
        }
        
    }
    
    @Override
    public int compareTo(ExternalObservailable<String, MailChangingEventArgs> externalObservailable) {
        return this.getClass().getTypeName().compareTo(externalObservailable.getClass().getTypeName());
    }
}
    
