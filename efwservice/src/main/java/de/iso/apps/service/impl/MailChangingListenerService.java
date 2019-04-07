package de.iso.apps.service.impl;

import de.iso.apps.contracts.ExternalObservailable;
import de.iso.apps.contracts.ExternalObserver;
import de.iso.apps.service.EmployeeService;
import de.iso.apps.service.dto.EmployeeDTO;
import de.iso.apps.service.dto.MailChangingDTO;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service public class MailChangingListenerService implements ExternalObservailable<String, MailChangingDTO> {
    
    private final Logger log = LoggerFactory.getLogger(MailChangingListenerService.class);
    private final EmployeeService employeeService;
    private final ExternalObserver<String, MailChangingDTO> externalObserver;
    
    public MailChangingListenerService(EmployeeService employeeService,
                                       @Qualifier("externalObserver")
                                           ExternalObserver<String, MailChangingDTO> externalObserver) {
        this.employeeService = employeeService;
        externalObserver.add(this);
        this.externalObserver = externalObserver;
    }
    
    
    private void changeEmployee(MailChangingDTO payload) {
        Optional<EmployeeDTO> employeeDTO = getEmployeeDTO(payload.getOldMail());
        employeeDTO.ifPresent(item -> {
            if (!Objects.equals(item.getEmail(), payload.getNewMail())) {
        
                item.setEmail(payload.getNewMail());
                employeeService.save(item);
                log.info("Employee was changed by Listener");
            }
    
        });
    }
    
    private Page<EmployeeDTO> getEmployeeDTOS(String payload) {
        return employeeService.search("email=" + payload, Pageable.unpaged());
    }
    
    private void deleteEmployee(MailChangingDTO payload) {
        Optional<EmployeeDTO> employee = getEmployeeDTO(payload.getOldMail());
        employee.ifPresent(item -> {
            employeeService.delete(item.getId());
            log.info("Employee was deleted by Listener");
        });
    }
    
    private Optional<EmployeeDTO> getEmployeeDTO(String payload) {
        Page<EmployeeDTO> employees = getEmployeeDTOS(payload);
        return employees.stream().findFirst();
    }
    
    private void createEmployee(MailChangingDTO payload) {
        
        var repemployee = getEmployeeDTO(payload.getNewMail());
        if (!repemployee.isPresent()) {
            var employee = new EmployeeDTO();
            employee.setEmail(payload.getNewMail());
            employeeService.save(employee);
            log.info("Employee was created by Listener");
        }
    }
    
    @Override
    public void valueChanged(String key, MailChangingDTO value) {
        
        if (StringUtils.hasText(value.getNewMail()) && StringUtils.hasText(value.getOldMail())) {
            changeEmployee(value);
        } else if (StringUtils.hasText(value.getOldMail()) && !StringUtils.hasText(value.getNewMail())) {
            deleteEmployee(value);
        } else if (StringUtils.hasText(value.getNewMail()) && !StringUtils.hasText(value.getOldMail())) {
            createEmployee(value);
        }
        
        
    }
    
    @Override
    public int compareTo(ExternalObservailable<String, MailChangingDTO> externalObservailable) {
        return this.getClass().getTypeName().compareTo(externalObservailable.getClass().getTypeName());
    }
}
