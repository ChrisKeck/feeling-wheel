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
import java.util.HashSet;
import java.util.Set;

/**
 * A Employee.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employee")
public class Employee implements Serializable {
    
    private static final long serialVersionUID = -4055529959146231035L;
    @Include
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FeelWheel> feelWheels = new HashSet<>();
    @OneToMany(mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Employee> employees = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("employees")
    private Employee employee;
    
    
    public Employee addFeelWheel(FeelWheel feelWheel) {
        this.feelWheels.add(feelWheel);
        feelWheel.setEmployee(this);
        return this;
    }

    public Employee removeFeelWheel(FeelWheel feelWheel) {
        this.feelWheels.remove(feelWheel);
        feelWheel.setEmployee(null);
        return this;
    }
    
    
    public Employee addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setEmployee(this);
        return this;
    }

    public Employee removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setEmployee(null);
        return this;
    }
    
    protected boolean canEqual(Object other) {
        return other != null &&
               getClass() == other.getClass() &&
               (this.getId() != null || ((Employee) other).getId() != null);
    }
}
