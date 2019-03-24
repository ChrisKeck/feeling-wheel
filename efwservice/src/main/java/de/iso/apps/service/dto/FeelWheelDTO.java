package de.iso.apps.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FeelWheel entity.
 */
public class FeelWheelDTO implements Serializable {

    private Long id;

    @NotNull
    private String subject;

    private Instant from;

    private Instant to;


    private Long employeeId;

    private String employeeEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getTo() {
        return to;
    }

    public void setTo(Instant to) {
        this.to = to;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FeelWheelDTO feelWheelDTO = (FeelWheelDTO) o;
        if (feelWheelDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feelWheelDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FeelWheelDTO{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            ", employee=" + getEmployeeId() +
            ", employee='" + getEmployeeEmail() + "'" +
            "}";
    }
}
