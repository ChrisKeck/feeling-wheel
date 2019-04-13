package de.iso.apps.contracts;

import java.io.Serializable;

public interface Mail extends Serializable {
    
    Long getId();
    
    void setId(Long id);
    
    String getEmail();
    
    void setEmail(String email);
    
}
