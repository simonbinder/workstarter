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
    private Portfolio portfolio;

    @OneToMany
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

    public Set<Resume> getResumes() {
        return resumes;
    }

    public Student resumes(Set<Resume> resumes) {
        this.resumes = resumes;
        return this;
    }

    public Student addResumes(Resume resume) {
        this.resumes.add(resume);
        return this;
    }

    public Student removeResumes(Resume resume) {
        this.resumes.remove(resume);
        return this;
    }

    public void setResumes(Set<Resume> resumes) {
        this.resumes = resumes;
    }

	@Override
	public String toString() {
		return "Student [matrikelNummer=" + matrikelNummer + ", portfolio=" + portfolio + ", resumes=" + resumes + "]";
	}

    
}
