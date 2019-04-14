package de.iso.apps.service.dto;

import de.iso.apps.contracts.Mail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * A DTO for the Employee entity.
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmployeeDTO implements Mail {
    
    private static final long serialVersionUID = -3647582173605257917L;
    @Include private Long id;
    
    @NotNull private String email;
    
    
    private Long employeeId;
    
    private String employeeEmail;
    
    protected boolean canEqual(Object other) {
        return other != null &&
               getClass() == other.getClass() &&
               (this.getId() != null || ((EmployeeDTO) other).getId() != null);
    }
}
