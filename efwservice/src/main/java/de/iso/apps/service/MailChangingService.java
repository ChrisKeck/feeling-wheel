package de.iso.apps.service;

import de.iso.apps.service.dto.EmployeeDTO;

public interface MailChangingService {
    
    void propagate(EmployeeDTO result, EmployeeDTO email);
}
