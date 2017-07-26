package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

/**
 * A CompanyAdmin.
 */
@Entity(name = "CompanyAdmin")
@DiscriminatorValue("CompanyAdmin")
@Document(indexName = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CompanyAdmin extends User {

	private static final long serialVersionUID = 1L;

	@Column(name = "location")
	private String location;

	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(unique = true)
	private Company company;

	@JsonManagedReference
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "CompanyAdmin [location=" + location + ", company=" + company + "]";
	}

}
