package de.iso.apps.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import de.iso.apps.domain.enumeration.FeelType;

/**
 * A DTO for the Feeling entity.
 */
public class FeelingDTO implements Serializable {

    private Long id;

    @NotNull
    private FeelType feeltype;

    @NotNull
    private Integer capacity;

    private Boolean isSpeechable;


    private Long feelwheelId;

    private String feelwheelSubject;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FeelType getFeeltype() {
        return feeltype;
    }

    public void setFeeltype(FeelType feeltype) {
        this.feeltype = feeltype;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean isIsSpeechable() {
        return isSpeechable;
    }

    public void setIsSpeechable(Boolean isSpeechable) {
        this.isSpeechable = isSpeechable;
    }

    public Long getFeelwheelId() {
        return feelwheelId;
    }

    public void setFeelwheelId(Long feelWheelId) {
        this.feelwheelId = feelWheelId;
    }

    public String getFeelwheelSubject() {
        return feelwheelSubject;
    }

    public void setFeelwheelSubject(String feelWheelSubject) {
        this.feelwheelSubject = feelWheelSubject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FeelingDTO feelingDTO = (FeelingDTO) o;
        if (feelingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), feelingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FeelingDTO{" +
            "id=" + getId() +
            ", feeltype='" + getFeeltype() + "'" +
            ", capacity=" + getCapacity() +
            ", isSpeechable='" + isIsSpeechable() + "'" +
            ", feelwheel=" + getFeelwheelId() +
            ", feelwheel='" + getFeelwheelSubject() + "'" +
            "}";
    }
}
