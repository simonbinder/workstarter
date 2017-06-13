package workstarter.service.dto;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import workstarter.config.Constants;
import workstarter.domain.Authority;
import workstarter.domain.CompanyAdmin;

public class CompanyAdminDTO {

	 private Long id;

	    @Pattern(regexp = Constants.LOGIN_REGEX)
	    @Size(min = 1, max = 100)
	    private String login;

	    @Email
	    @Size(min = 5, max = 100)
	    private String email;

	    @Size(max = 256)
	    private String imageUrl;
	    
	    @Size(max = 50)
	    private String firstName;

	    @Size(max = 50)
	    private String lastName;
	    
	    private boolean activated = false;

	    @Size(min = 2, max = 5)
	    private String langKey;

	    private String createdBy;
	    private ZonedDateTime createdDate;
	    private String lastModifiedBy;
	    private ZonedDateTime lastModifiedDate;
	    private Set<String> authorities;
	    private String location;

	    public CompanyAdminDTO() {
	        // Empty constructor needed for MapStruct.
	    }

	    public CompanyAdminDTO(CompanyAdmin companyAdmin) {
	        this(companyAdmin.getId(), companyAdmin.getLogin(), 
	            companyAdmin.getEmail(), companyAdmin.getActivated(), companyAdmin.getImageUrl(), companyAdmin.getFirstName(), companyAdmin.getLastName(),  companyAdmin.getLangKey(),
	            companyAdmin.getCreatedBy(), companyAdmin.getCreatedDate(), companyAdmin.getLastModifiedBy(), companyAdmin.getLastModifiedDate(), 
	            companyAdmin.getAuthorities().stream().map(Authority::getName)
	                .collect(Collectors.toSet()), companyAdmin.getLocation());
	    }

	    public CompanyAdminDTO(Long id, String login,
	        String email, boolean activated, String imageUrl, String langKey, String firstName, String lastName,
	        String createdBy, ZonedDateTime createdDate, String lastModifiedBy, ZonedDateTime lastModifiedDate, 
	        Set<String> authorities, String location) {

	        this.id = id;
	        this.login = login;
	        this.email = email;
	        this.firstName = firstName;
	        this.lastName = lastName;
	        this.activated = activated;
	        this.imageUrl = imageUrl;
	        this.langKey = langKey;
	        this.createdBy = createdBy;
	        this.createdDate = createdDate;
	        this.lastModifiedBy = lastModifiedBy;
	        this.lastModifiedDate = lastModifiedDate;
	        this.authorities = authorities;
	        this.location = location;
	    }

	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getLogin() {
	        return login;
	    }

	    public void setLogin(String login) {
	        this.login = login;
	    }

	    public String getEmail() {
	        return email;
	    }

		public String getImageUrl() {
	        return imageUrl;
	    }

	    public boolean isActivated() {
	        return activated;
	    }

	    public String getLangKey() {
	        return langKey;
	    }

	    public String getCreatedBy() {
	        return createdBy;
	    }

	    public ZonedDateTime getCreatedDate() {
	        return createdDate;
	    }

	    public String getLastModifiedBy() {
	        return lastModifiedBy;
	    }

	    public ZonedDateTime getLastModifiedDate() {
	        return lastModifiedDate;
	    }

	    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
	        this.lastModifiedDate = lastModifiedDate;
	    }

	    public Set<String> getAuthorities() {
	        return authorities;
	    }
	    

	    public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		

		@Override
	    public String toString() {
	        return "UserDTO{" +
	            "login='" + login + '\'' +
	            ", email='" + email + '\'' +
	            ", imageUrl='" + imageUrl + '\'' +
	            ", activated=" + activated +
	            ", langKey='" + langKey + '\'' +
	            ", createdBy=" + createdBy +
	            ", createdDate=" + createdDate +
	            ", lastModifiedBy='" + lastModifiedBy + '\'' +
	            ", lastModifiedDate=" + lastModifiedDate +
	            ", authorities=" + authorities +
	            "}";
	    }
}
