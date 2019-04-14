package de.iso.apps.service.dto;

import de.iso.apps.config.Constants;
import de.iso.apps.contracts.BaseUser;
import de.iso.apps.contracts.TimeMeasure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

/**
 * A DTO representing a user, with his authorities.
 */
@Data @Builder @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true) @AllArgsConstructor
public class UserDTO implements BaseUser, TimeMeasure {
    private static final long serialVersionUID = 5129857733638000361L;
    @Include private Long id;
    
    @NotBlank @Pattern(regexp = Constants.LOGIN_REGEX) @Size(min = 1, max = 50) private String login;
    
    @Size(max = 50) private String firstName;
    
    @Size(max = 50) private String lastName;
    
    @Email @Size(min = 5, max = 254) private String email;
    
    @Size(max = 256) private String imageUrl;
    
    private boolean activated = false;
    
    @Size(min = 2, max = 6) private String langKey;
    
    private String createdBy;
    
    private Instant createdDate;
    
    private String lastModifiedBy;
    
    private Instant lastModifiedDate;
    
    private Set<String> authorities;
    
    protected boolean canEqual(Object other) {
        return other != null &&
               getClass() == other.getClass() &&
               (this.getId() != null || ((UserDTO) other).getId() != null);
    }
}
