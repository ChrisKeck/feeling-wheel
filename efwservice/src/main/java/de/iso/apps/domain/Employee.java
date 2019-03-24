package de.iso.apps.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    
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

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public Employee email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<FeelWheel> getFeelWheels() {
        return feelWheels;
    }

    public Employee feelWheels(Set<FeelWheel> feelWheels) {
        this.feelWheels = feelWheels;
        return this;
    }

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

    public void setFeelWheels(Set<FeelWheel> feelWheels) {
        this.feelWheels = feelWheels;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Employee employees(Set<Employee> employees) {
        this.employees = employees;
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

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Employee employee(Employee employee) {
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
        Employee employee = (Employee) o;
        if (employee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), employee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
