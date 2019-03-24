package de.iso.apps.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A FeelWheel.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "feel_wheel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "feelwheel")
public class FeelWheel implements Serializable {
    
    private static final long serialVersionUID = -2054759772503157281L;
    @Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "jhi_from")
    private Instant from;

    @Column(name = "jhi_to")
    private Instant to;

    @OneToMany(mappedBy = "feelwheel")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Feeling> feelings = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("feelWheels")
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    
    
    public FeelWheel addFeeling(Feeling feeling) {
        this.feelings.add(feeling);
        feeling.setFeelwheel(this);
        return this;
    }

    public FeelWheel removeFeeling(Feeling feeling) {
        this.feelings.remove(feeling);
        feeling.setFeelwheel(null);
        return this;
    }
    
    protected boolean canEqual(Object other) {
        return other != null &&
               getClass() == other.getClass() &&
               (this.getId() != null || ((FeelWheel) other).getId() != null);
    }
}
