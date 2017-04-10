package workstarter.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Offering entity.
 */
public class OfferingDTO implements Serializable {

    private Long id;

    private String offeringValues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getOfferingValues() {
        return offeringValues;
    }

    public void setOfferingValues(String offeringValues) {
        this.offeringValues = offeringValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OfferingDTO offeringDTO = (OfferingDTO) o;

        if ( ! Objects.equals(id, offeringDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OfferingDTO{" +
            "id=" + id +
            ", offeringValues='" + offeringValues + "'" +
            '}';
    }
}
