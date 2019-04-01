package de.iso.apps.service.dto;

import de.iso.apps.domain.enumeration.FeelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the Feeling entity.
 */
@Data @Builder @AllArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true) @NoArgsConstructor
public class FeelingDTO implements Serializable {
    
    private static final long serialVersionUID = 2528690780601204297L;
    @Include private Long id;
    
    @NotNull private FeelType feeltype;
    
    @NotNull private Integer capacity;
    
    private Boolean isSpeechable;
    
    
    private Long feelwheelId;
    
    private String feelwheelSubject;
    
    protected boolean canEqual(Object other) {
        return other != null &&
               getClass() == other.getClass() &&
               (this.getId() != null || ((FeelingDTO) other).getId() != null);
    }
}
