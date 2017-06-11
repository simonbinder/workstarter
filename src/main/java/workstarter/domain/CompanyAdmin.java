package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

/**
 * A CompanyAdmin.
 */
@Entity(name="CompanyAdmin")
@DiscriminatorValue("CompanyAdmin")
@Document(indexName = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CompanyAdmin extends User {

    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(unique = true)
   private Company company;
    
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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
		return true;
	}

	@Override
	public String toString() {
		return "CompanyAdmin ["+ ", getId()=" + getId()
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
