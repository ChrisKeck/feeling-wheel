package de.iso.apps.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.iso.apps.contracts.TimeMeasure;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created, last modified
 * by date.
 */
@Getter @Setter @MappedSuperclass @Audited @EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements TimeMeasure {
    
    
    private static final long serialVersionUID = -5222568206929805776L;
    @CreatedBy @Column(name = "created_by", nullable = false, length = 50, updatable = false) @JsonIgnore
    private String createdBy;
    
    @CreatedDate @Column(name = "created_date", updatable = false) @JsonIgnore
    private Instant createdDate = Instant.now();
    
    @LastModifiedBy @Column(name = "last_modified_by", length = 50) @JsonIgnore private String lastModifiedBy;
    
    @LastModifiedDate @Column(name = "last_modified_date") @JsonIgnore private Instant lastModifiedDate = Instant.now();
    
    
}
