package de.iso.apps.contracts;

import java.io.Serializable;

public interface Userable extends Serializable {
    Long getId();
    
    void setId(Long id);
    
    String getLogin();
    
    void setLogin(String login);
    
    String getFirstName();
    
    void setFirstName(String firstName);
    
    String getLastName();
    
    void setLastName(String lastName);
    
    String getEmail();
    
    void setEmail(String email);
    
    boolean isActivated();
    
    void setActivated(boolean activated);
    
    String getLangKey();
    
    void setLangKey(String langKey);
    
    String getImageUrl();
    
    void setImageUrl(String imageUrl);
    
}
