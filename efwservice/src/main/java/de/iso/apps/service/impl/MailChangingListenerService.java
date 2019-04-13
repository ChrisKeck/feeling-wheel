package de.iso.apps.service.impl;

import de.iso.apps.contracts.ExternalObservailable;
import de.iso.apps.contracts.ExternalObserver;
import de.iso.apps.contracts.MailChangingEventArgs;
import de.iso.apps.service.EmployeeService;
import de.iso.apps.service.dto.EmployeeDTO;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service public class MailChangingListenerService implements ExternalObservailable<String, MailChangingEventArgs> {
    
    private final Logger log = LoggerFactory.getLogger(MailChangingListenerService.class);
    private final EmployeeService employeeService;
    private final ExternalObserver<String, MailChangingEventArgs> externalObserver;
    
    public MailChangingListenerService(EmployeeService employeeService,
                                       @Qualifier("externalObserver")
                                               ExternalObserver<String, MailChangingEventArgs> externalObserver) {
        this.employeeService = employeeService;
        externalObserver.add(this);
        this.externalObserver = externalObserver;
    }
    
    
    private void changeEmployee(MailChangingEventArgs payload) {
        Optional<EmployeeDTO> employeeDTO = getEmployeeDTO(payload.getOldMail());
        employeeDTO.ifPresent(item -> {
            item.setEmail(payload.getNewMail());
            employeeService.save(item);
            log.info("Employee was changed by Listener");
        });
    }
    
    private Page<EmployeeDTO> getEmployeeDTOS(String payload) {
        return employeeService.search("email=" + payload, Pageable.unpaged());
    }
    
    private void deleteEmployee(MailChangingEventArgs payload) {
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
    
    private void createEmployee(MailChangingEventArgs payload) {
        
        var repemployee = getEmployeeDTO(payload.getNewMail());
        if (!repemployee.isPresent()) {
            var employee = new EmployeeDTO();
            employee.setEmail(payload.getNewMail());
            employeeService.save(employee);
            log.info("Employee was created by Listener");
        }
    }
    
    @Override
    public void valueChanged(String key, MailChangingEventArgs value) {
        
        if (StringUtils.hasText(value.getNewMail()) && StringUtils.hasText(value.getOldMail())) {
            changeEmployee(value);
        } else if (StringUtils.hasText(value.getOldMail()) && !StringUtils.hasText(value.getNewMail())) {
            deleteEmployee(value);
        } else if (StringUtils.hasText(value.getNewMail()) && !StringUtils.hasText(value.getOldMail())) {
            createEmployee(value);
        }
        
        
    }
    
    @Override
    public int compareTo(ExternalObservailable<String, MailChangingEventArgs> externalObservailable) {
        return this.getClass().getTypeName().compareTo(externalObservailable.getClass().getTypeName());
    }
}
