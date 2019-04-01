package de.iso.apps.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.iso.apps.domain.enumeration.FeelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Feeling.
 */
@Data @Builder @AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true) @Entity
@Table(name = "feeling") @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE) @Document(indexName = "feeling")
public class Feeling implements Serializable {
    
    private static final long serialVersionUID = -2311189753747579503L;
    @Include @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator") private Long id;
    
    @NotNull @Enumerated(EnumType.STRING) @Column(name = "feeltype", nullable = false) private FeelType feeltype;
    
    @NotNull @Column(name = "capacity", nullable = false) private Integer capacity;
    
    @Column(name = "is_speechable") private Boolean isSpeechable;
    
    @ManyToOne @JsonIgnoreProperties("feelings") private FeelWheel feelwheel;
    
    protected boolean canEqual(Object other) {
        return other != null &&
               getClass() == other.getClass() &&
               (this.getId() != null || ((Feeling) other).getId() != null);
    }
}
