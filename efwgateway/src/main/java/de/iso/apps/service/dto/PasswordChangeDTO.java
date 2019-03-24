package de.iso.apps.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * A DTO representing a password change required data - current and new password.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordChangeDTO implements Serializable {
    
    private static final long serialVersionUID = -3813853982580464350L;
    private String currentPassword;
    private String newPassword;

}
