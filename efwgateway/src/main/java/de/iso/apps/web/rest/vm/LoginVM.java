package de.iso.apps.web.rest.vm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
@Getter
@Setter
@ToString
public class LoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = ManagedUserVM.PASSWORD_MIN_LENGTH, max = ManagedUserVM.PASSWORD_MAX_LENGTH)
    private String password;

    private Boolean rememberMe;
    
}
