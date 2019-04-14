package de.iso.apps.contracts;

import java.io.Serializable;
import java.time.Instant;

public interface TimeMeasure extends Serializable {
    String getCreatedBy();
    
    Instant getCreatedDate();
    
    String getLastModifiedBy();
    
    Instant getLastModifiedDate();
    
    void setCreatedBy(String createdBy);
    
    void setCreatedDate(Instant createdDate);
    
    void setLastModifiedBy(String lastModifiedBy);
    
    void setLastModifiedDate(Instant lastModifiedDate);
}
