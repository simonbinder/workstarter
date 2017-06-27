package workstarter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @NotNull
    @Column(name = "domain", nullable = false)
    private String domain;
    
    @NotNull
    @Column(name = "sector", nullable = false)
    private String sector;
    
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "image_url")
    private String imageURL;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
    @Fetch(value = FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Jobadvertisment> jobs = new ArrayList<>();
    
    @OneToOne
    @JoinColumn(unique = true)
    private CompanyAdmin admin;

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


    public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonBackReference
	public CompanyAdmin getAdmin() {
		return admin;
	}

	public void setAdmin(CompanyAdmin admin) {
		this.admin = admin;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public List<Jobadvertisment> getJobs() {
		return jobs;
	}

	public void setJobs(List<Jobadvertisment> jobs) {
		this.jobs = jobs;
	}
	
	public Company addJob(Jobadvertisment jobadvertisement){
		this.jobs.add(jobadvertisement);
		return this;
	}
	
	public Company removeJob(Jobadvertisment jobadvertisement){
		this.jobs.remove(jobadvertisement);
		return this;
	}
	
	public Company updateJob(Jobadvertisment oldJob, Jobadvertisment jobadvertisment){
		int index = this.jobs.indexOf(oldJob);
		this.jobs.set(index, jobadvertisment);
		return this;
	}
}
