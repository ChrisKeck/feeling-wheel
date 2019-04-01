package de.iso.apps.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the FeelWheel entity.
 */
@Data @Builder @AllArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true) @NoArgsConstructor
public class FeelWheelDTO implements Serializable {
    
    private static final long serialVersionUID = -3931739120912753732L;
    @Include private Long id;
    
    @NotNull private String subject;
    
    private Instant from;
    
    private Instant to;
    
    
    private Long employeeId;
    
    private String employeeEmail;
    
    protected boolean canEqual(Object other) {
        return other != null &&
               getClass() == other.getClass() &&
               (this.getId() != null || ((FeelWheelDTO) other).getId() != null);
    }
    
}
