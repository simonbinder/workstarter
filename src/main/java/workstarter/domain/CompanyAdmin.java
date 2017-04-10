package workstarter.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

@Entity
@DiscriminatorValue("Company")
@Document(indexName = "company")
@Table(name = "jhi_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CompanyAdmin extends User {

	private static final long serialVersionUID = 1L;

	@Size(min = 5, max = 60)
	@Column(name = "website", length = 60, nullable=true)
	private String website;

	public CompanyAdmin(){
		// empty Constructor needed for Hibernate
	}
	
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public CompanyAdmin(String website) {
		this.website = website;
	}
	
	
}
