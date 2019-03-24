package de.iso.apps.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import de.iso.apps.domain.enumeration.FeelType;

/**
 * A Feeling.
 */
@Entity
@Table(name = "feeling")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "feeling")
public class Feeling implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "feeltype", nullable = false)
    private FeelType feeltype;

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "is_speechable")
    private Boolean isSpeechable;

    @ManyToOne
    @JsonIgnoreProperties("feelings")
    private FeelWheel feelwheel;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeelType getFeeltype() {
        return feeltype;
    }

    public Feeling feeltype(FeelType feeltype) {
        this.feeltype = feeltype;
        return this;
    }

    public void setFeeltype(FeelType feeltype) {
        this.feeltype = feeltype;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Feeling capacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean isIsSpeechable() {
        return isSpeechable;
    }

    public Feeling isSpeechable(Boolean isSpeechable) {
        this.isSpeechable = isSpeechable;
        return this;
    }

    public void setIsSpeechable(Boolean isSpeechable) {
        this.isSpeechable = isSpeechable;
    }

    public FeelWheel getFeelwheel() {
        return feelwheel;
    }

    public Feeling feelwheel(FeelWheel feelWheel) {
        this.feelwheel = feelWheel;
        return this;
    }

    public void setFeelwheel(FeelWheel feelWheel) {
        this.feelwheel = feelWheel;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Feeling feeling = (Feeling) o;
        if (feeling.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feeling.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Feeling{" +
            "id=" + getId() +
            ", feeltype='" + getFeeltype() + "'" +
            ", capacity=" + getCapacity() +
            ", isSpeechable='" + isIsSpeechable() + "'" +
            "}";
    }
}
