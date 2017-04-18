package workstarter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Resume.
 */
@Entity
@Table(name = "resume")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "resume")
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Student student;

    @OneToMany(mappedBy = "resume")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<School> schools = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Company> companies = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Company> internships = new HashSet<>();

    @OneToMany(mappedBy = "resume")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Qualification> qualifications = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Resume title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Resume description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Student getStudent() {
        return student;
    }

    public Resume student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Set<School> getSchools() {
        return schools;
    }

    public Resume schools(Set<School> schools) {
        this.schools = schools;
        return this;
    }

    public Resume addSchools(School school) {
        this.schools.add(school);
        school.setResume(this);
        return this;
    }

    public Resume removeSchools(School school) {
        this.schools.remove(school);
        school.setResume(null);
        return this;
    }

    public void setSchools(Set<School> schools) {
        this.schools = schools;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public Resume companies(Set<Company> companies) {
        this.companies = companies;
        return this;
    }

    public Resume addCompanies(Company company) {
        this.companies.add(company);
        company.setResume(this);
        return this;
    }

    public Resume removeCompanies(Company company) {
        this.companies.remove(company);
        company.setResume(null);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Set<Company> getInternships() {
        return internships;
    }

    public Resume internships(Set<Company> companies) {
        this.internships = companies;
        return this;
    }

    public Resume addInternships(Company company) {
        this.internships.add(company);
        company.setResume(this);
        return this;
    }

    public Resume removeInternships(Company company) {
        this.internships.remove(company);
        company.setResume(null);
        return this;
    }

    public void setInternships(Set<Company> companies) {
        this.internships = companies;
    }

    public Set<Qualification> getQualifications() {
        return qualifications;
    }

    public Resume qualifications(Set<Qualification> qualifications) {
        this.qualifications = qualifications;
        return this;
    }

    public Resume addQualifications(Qualification qualification) {
        this.qualifications.add(qualification);
        qualification.setResume(this);
        return this;
    }

    public Resume removeQualifications(Qualification qualification) {
        this.qualifications.remove(qualification);
        qualification.setResume(null);
        return this;
    }

    public void setQualifications(Set<Qualification> qualifications) {
        this.qualifications = qualifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Resume resume = (Resume) o;
        if (resume.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, resume.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Resume{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
