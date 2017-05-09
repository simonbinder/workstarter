package workstarter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Student.
 */
@Entity(name="Student")
@DiscriminatorValue("Student")
@Document(indexName = "student")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Student extends User {

    private static final long serialVersionUID = 1L;

    @Column(name = "matrikel_nummer")
    private String matrikelNummer;

    @OneToOne
    @JoinColumn(unique = true)
    private School university;

    @OneToOne
    @JoinColumn(unique = true)
    private Portfolio portfolio;

    @OneToMany(mappedBy = "student",fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Offering> offeringValues = new HashSet<>();

    @OneToMany(mappedBy = "student",fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Searching> searchingValues = new HashSet<>();

    @OneToMany(mappedBy = "student",fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Resume> resumes = new HashSet<>();


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

    public School getUniversity() {
		return university;
	}

	public void setUniversity(School school) {
        this.university = school;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public Student portfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
        return this;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
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

    public Set<Searching> getSearchingValues() {
        return searchingValues;
    }

    public Student searchingValues(Set<Searching> searchings) {
        this.searchingValues = searchings;
        return this;
    }

    public Student addSearchingValue(Searching searching) {
        this.searchingValues.add(searching);
        searching.setStudent(this);
        return this;
    }

    public Student removeSearchingValue(Searching searching) {
        this.searchingValues.remove(searching);
        searching.setStudent(null);
        return this;
    }

    public void setSearchingValues(Set<Searching> searchings) {
        this.searchingValues = searchings;
    }

    public Set<Resume> getResumes() {
        return resumes;
    }

    public Student resumes(Set<Resume> resumes) {
        this.resumes = resumes;
        return this;
    }

    public Student addResumes(Resume resume) {
        this.resumes.add(resume);
        resume.setStudent(this);
        return this;
    }

    public Student removeResumes(Resume resume) {
        this.resumes.remove(resume);
        resume.setStudent(null);
        return this;
    }

    public void setResumes(Set<Resume> resumes) {
        this.resumes = resumes;
    }

    
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((matrikelNummer == null) ? 0 : matrikelNummer.hashCode());
		result = prime * result + ((offeringValues == null) ? 0 : offeringValues.hashCode());
		result = prime * result + ((portfolio == null) ? 0 : portfolio.hashCode());
		result = prime * result + ((resumes == null) ? 0 : resumes.hashCode());
		result = prime * result + ((searchingValues == null) ? 0 : searchingValues.hashCode());
		result = prime * result + ((university == null) ? 0 : university.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (matrikelNummer == null) {
			if (other.matrikelNummer != null)
				return false;
		} else if (!matrikelNummer.equals(other.matrikelNummer))
			return false;
		if (offeringValues == null) {
			if (other.offeringValues != null)
				return false;
		} else if (!offeringValues.equals(other.offeringValues))
			return false;
		if (portfolio == null) {
			if (other.portfolio != null)
				return false;
		} else if (!portfolio.equals(other.portfolio))
			return false;
		if (resumes == null) {
			if (other.resumes != null)
				return false;
		} else if (!resumes.equals(other.resumes))
			return false;
		if (searchingValues == null) {
			if (other.searchingValues != null)
				return false;
		} else if (!searchingValues.equals(other.searchingValues))
			return false;
		if (university == null) {
			if (other.university != null)
				return false;
		} else if (!university.equals(other.university))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [matrikelNummer=" + matrikelNummer + ", university=" + university + ", portfolio=" + portfolio
				+ ", offeringValues=" + offeringValues + ", searchingValues=" + searchingValues + ", resumes=" + resumes
				+ ", getId()=" + getId() + ", getLogin()=" + getLogin() + ", getPassword()=" + getPassword()
				+ ", getEmail()=" + getEmail() + ", getImageUrl()=" + getImageUrl() + ", getActivated()="
				+ getActivated() + ", getFirstName()=" + getFirstName() + ", getLastName()=" + getLastName()
				+ ", getActivationKey()=" + getActivationKey() + ", getResetKey()=" + getResetKey()
				+ ", getResetDate()=" + getResetDate() + ", getLangKey()=" + getLangKey() + ", getAuthorities()="
				+ getAuthorities() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ ", getCreatedBy()=" + getCreatedBy() + ", getCreatedDate()=" + getCreatedDate()
				+ ", getLastModifiedBy()=" + getLastModifiedBy() + ", getLastModifiedDate()=" + getLastModifiedDate()
				+ ", getClass()=" + getClass() + "]";
	}


}
