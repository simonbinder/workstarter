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
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @OneToMany
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Jobadvertisment> jobadvertisments = new HashSet<>();

    @OneToMany
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CompanyAdmin> admins = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Company companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Set<Jobadvertisment> getJobadvertisments() {
        return jobadvertisments;
    }

    public Company jobadvertisments(Set<Jobadvertisment> jobadvertisments) {
        this.jobadvertisments = jobadvertisments;
        return this;
    }

    public Company addJobadvertisment(Jobadvertisment jobadvertisment) {
        this.jobadvertisments.add(jobadvertisment);
        return this;
    }

    public Company removeJobadvertisment(Jobadvertisment jobadvertisment) {
        this.jobadvertisments.remove(jobadvertisment);
        return this;
    }

    public void setJobadvertisments(Set<Jobadvertisment> jobadvertisments) {
        this.jobadvertisments = jobadvertisments;
    }

    public Set<CompanyAdmin> getAdmins() {
        return admins;
    }

    public Company admins(Set<CompanyAdmin> companyAdmins) {
        this.admins = companyAdmins;
        return this;
    }

    public Company addAdmins(CompanyAdmin companyAdmin) {
        this.admins.add(companyAdmin);
        return this;
    }

    public Company removeAdmins(CompanyAdmin companyAdmin) {
        this.admins.remove(companyAdmin);
        return this;
    }

    public void setAdmins(Set<CompanyAdmin> companyAdmins) {
        this.admins = companyAdmins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        if (company.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + id +
            ", companyName='" + companyName + "'" +
            '}';
    }
}
