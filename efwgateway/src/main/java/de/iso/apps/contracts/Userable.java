package de.iso.apps.contracts;

import java.io.Serializable;

public interface Userable extends Serializable {
    Long getId();
    
    String getLogin();
    
    String getFirstName();
    
    String getLastName();
    
    String getEmail();
    
    boolean isActivated();
    
    String getLangKey();
    
    String getImageUrl();
    
    
    void setId(Long id);
    
    void setLogin(String login);
    
    
    void setFirstName(String firstName);
    
    void setLastName(String lastName);
    
    void setEmail(String email);
    
    void setActivated(boolean activated);
    
    void setLangKey(String langKey);
    
    void setImageUrl(String imageUrl);
    
}
