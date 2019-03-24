package de.iso.apps.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A FeelWheel.
 */
@Entity
@Table(name = "feel_wheel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "feelwheel")
public class FeelWheel implements Serializable {

    private static final long serialVersionUID = 1L;
    
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public FeelWheel subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Instant getFrom() {
        return from;
    }

    public FeelWheel from(Instant from) {
        this.from = from;
        return this;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getTo() {
        return to;
    }

    public FeelWheel to(Instant to) {
        this.to = to;
        return this;
    }

    public void setTo(Instant to) {
        this.to = to;
    }

    public Set<Feeling> getFeelings() {
        return feelings;
    }

    public FeelWheel feelings(Set<Feeling> feelings) {
        this.feelings = feelings;
        return this;
    }

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

    public void setFeelings(Set<Feeling> feelings) {
        this.feelings = feelings;
    }

    public Employee getEmployee() {
        return employee;
    }

    public FeelWheel employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
        FeelWheel feelWheel = (FeelWheel) o;
        if (feelWheel.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feelWheel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FeelWheel{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            "}";
    }
}
