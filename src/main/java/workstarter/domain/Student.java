package workstarter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A Student.
 */
@Entity
@DiscriminatorValue("Student")
@Document(indexName = "student")
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Student extends User {

    private static final long serialVersionUID = 1L;


    @Column(name = "matrikel_nummer")
    private String matrikelNummer;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Offering> offeringValues = new HashSet<>();


    public String getMatrikelNummer() {
        return matrikelNummer;
    }

    public Student matrikelNummer(String matrikelNummer) {
        this.matrikelNummer = matrikelNummer;
        return this;
    }

    public void setMatrikelNummer(String matrikelNummer) {
        this.matrikelNummer = matrikelNummer;
    }

    public Set<Offering> getOfferingValues() {
        return offeringValues;
    }

    public Student offeringValues(Set<Offering> offerings) {
        this.offeringValues = offerings;
        return this;
    }

    public Student addOfferingValue(Offering offering) {
        this.offeringValues.add(offering);
        offering.setStudent(this);
        return this;
    }

    public Student removeOfferingValue(Offering offering) {
        this.offeringValues.remove(offering);
        offering.setStudent(null);
        return this;
    }

    public void setOfferingValues(Set<Offering> offerings) {
        this.offeringValues = offerings;
    }

	@Override
	public String toString() {
		return "Student [matrikelNummer=" + matrikelNummer + ", offeringValues=" + offeringValues + ", getId()="
				+ getId() + ", getLogin()=" + getLogin() + ", getPassword()=" + getPassword() + ", getEmail()="
				+ getEmail() + ", getImageUrl()=" + getImageUrl() + ", getActivated()=" + getActivated()
				+ ", getFirstName()=" + getFirstName() + ", getLastName()=" + getLastName() + ", getActivationKey()="
				+ getActivationKey() + ", getResetKey()=" + getResetKey() + ", getResetDate()=" + getResetDate()
				+ ", getLangKey()=" + getLangKey() + ", getAuthorities()=" + getAuthorities() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + ", getCreatedBy()=" + getCreatedBy()
				+ ", getCreatedDate()=" + getCreatedDate() + ", getLastModifiedBy()=" + getLastModifiedBy()
				+ ", getLastModifiedDate()=" + getLastModifiedDate() + ", getClass()=" + getClass() + "]";
	}

}
