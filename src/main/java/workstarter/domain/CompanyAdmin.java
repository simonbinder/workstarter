package workstarter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A CompanyAdmin.
 */
@Entity(name="CompanyAdmin")
@DiscriminatorValue("CompanyAdmin")
@Document(indexName = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CompanyAdmin extends User {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "companyAdmin",fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Company> companies = new HashSet<>();

    @ManyToOne
    private Company company;

    public Set<Company> getCompanies() {
        return companies;
    }

    public CompanyAdmin companies(Set<Company> companies) {
        this.companies = companies;
        return this;
    }

    public CompanyAdmin addCompanies(Company company) {
        this.companies.add(company);
        company.setCompanyAdmin(this);
        return this;
    }

    public CompanyAdmin removeCompanies(Company company) {
        this.companies.remove(company);
        company.setCompanyAdmin(null);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Company getCompany() {
        return company;
    }

    public CompanyAdmin company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((companies == null) ? 0 : companies.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
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
		CompanyAdmin other = (CompanyAdmin) obj;
		if (companies == null) {
			if (other.companies != null)
				return false;
		} else if (!companies.equals(other.companies))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompanyAdmin [companies=" + companies + ", company=" + company + ", getId()=" + getId()
				+ ", getLogin()=" + getLogin() + ", getPassword()=" + getPassword() + ", getEmail()=" + getEmail()
				+ ", getImageUrl()=" + getImageUrl() + ", getActivated()=" + getActivated() + ", getFirstName()="
				+ getFirstName() + ", getLastName()=" + getLastName() + ", getActivationKey()=" + getActivationKey()
				+ ", getResetKey()=" + getResetKey() + ", getResetDate()=" + getResetDate() + ", getLangKey()="
				+ getLangKey() + ", getAuthorities()=" + getAuthorities() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + ", getCreatedBy()=" + getCreatedBy() + ", getCreatedDate()="
				+ getCreatedDate() + ", getLastModifiedBy()=" + getLastModifiedBy() + ", getLastModifiedDate()="
				+ getLastModifiedDate() + ", getClass()=" + getClass() + "]";
	}
    
    

}
