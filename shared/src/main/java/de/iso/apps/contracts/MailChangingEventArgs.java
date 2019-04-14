package de.iso.apps.contracts;

import java.io.Serializable;

public interface MailChangingEventArgs extends Serializable {
    String getNewMail();
    
    String getOldMail();
    
    boolean hasChange();
}
