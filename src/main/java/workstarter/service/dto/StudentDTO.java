package workstarter.service.dto;

import workstarter.config.Constants;

import workstarter.domain.Authority;
import workstarter.domain.Portfolio;
import workstarter.domain.Resume;
import workstarter.domain.School;
import workstarter.domain.Student;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class StudentDTO {

	private Long id;

	@Pattern(regexp = Constants.LOGIN_REGEX)
	@Size(min = 1, max = 100)
	private String login;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;

	@Size(max = 256)
	private String imageUrl;

	private boolean activated = false;

	@Size(min = 2, max = 5)
	private String langKey;

	private String createdBy;
	private ZonedDateTime createdDate;
	private String lastModifiedBy;
	private ZonedDateTime lastModifiedDate;
	private Portfolio portfolio;
	private Set<String> authorities;
	private Set<String> resumes;
	private List<String> schools;
	private String slogan;
	private String title;

	public StudentDTO() {
		// Empty constructor needed for MapStruct.
	}

	public StudentDTO(Student student) {
		this(student.getId(), student.getLogin(), student.getFirstName(), student.getLastName(), student.getEmail(),
				student.getActivated(), student.getImageUrl(), student.getLangKey(), student.getCreatedBy(),
				student.getCreatedDate(), student.getLastModifiedBy(), student.getLastModifiedDate(),
				student.getPortfolio(),
				student.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()),
				student.getResumes().stream().map(Resume::getTitle).collect(Collectors.toSet()),
				student.getSchools().stream().map(School::getName).collect(Collectors.toList()), student.getSlogan(), student.getTitle());
	}

	public StudentDTO(Long id, String login, String firstName, String lastName, String email, boolean activated,
			String imageUrl, String langKey, String createdBy, ZonedDateTime createdDate, String lastModifiedBy,
			ZonedDateTime lastModifiedDate, Portfolio portfolio, Set<String> authorities, Set<String> resumes,
			List<String> schools, String slogan, String title) {

		this.id = id;
		this.login = login;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.activated = activated;
		this.imageUrl = imageUrl;
		this.langKey = langKey;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.portfolio = portfolio;
		this.authorities = authorities;
		this.resumes = resumes;
		this.schools = schools;
		this.slogan = slogan;
		this.title = title;
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

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
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

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public Set<String> getResumes() {
		return resumes;
	}

	public List<String> getSchools() {
		return schools;
	}

	public void setSchools(List<String> schools) {
		this.schools = schools;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlogan() {
		return slogan;
	}

	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	
	

	@Override
	public String toString() {
		return "StudentDTO [id=" + id + ", login=" + login + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", imageUrl=" + imageUrl + ", activated=" + activated + ", langKey=" + langKey
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy
				+ ", lastModifiedDate=" + lastModifiedDate + ", portfolio=" + portfolio + ", authorities=" + authorities
				+ ", resumes=" + resumes + ", schools=" + schools + "]";
	}

}
