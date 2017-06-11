package workstarter.web.rest.vm;

import workstarter.domain.Portfolio;
import workstarter.domain.School;
import workstarter.service.dto.StudentDTO;
import javax.validation.constraints.Size;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * View Model extending the UserDTO, which is meant to be used in the user
 * management UI.
 */
public class ManagedStudentVM extends StudentDTO {

	public static final int PASSWORD_MIN_LENGTH = 4;

	public static final int PASSWORD_MAX_LENGTH = 100;

	@Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
	private String password;

	public ManagedStudentVM() {
		// Empty constructor needed for Jackson.
	}

	public ManagedStudentVM(Long id, String login, String password, String firstName, String lastName, String email,
			boolean activated, String imageUrl, String langKey, String createdBy, ZonedDateTime createdDate,
			String lastModifiedBy, ZonedDateTime lastModifiedDate, School university, Portfolio portfolio,
			Set<String> authorities) {

		super(id, login, firstName, lastName, email, activated, imageUrl, langKey, createdBy, createdDate,
				lastModifiedBy, lastModifiedDate, portfolio, authorities);

		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "ManagedUserVM{" + "} " + super.toString();
	}
}
