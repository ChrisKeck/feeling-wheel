package de.iso.apps.contracts;

import java.io.Serializable;

public interface BaseUser extends Serializable, Mail {
    
    String getFirstName();
    
    String getLastName();
    
    
    boolean isActivated();
    
    
    void setFirstName(String firstName);
    
    void setLastName(String lastName);
    
    void setActivated(boolean activated);
    
    String getLogin();
    
    void setLogin(String login);
    
    String getImageUrl();
    
    String getLangKey();
    
    void setLangKey(String langKey);
    
    void setImageUrl(String imageUrl);
}
