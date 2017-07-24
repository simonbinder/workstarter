package workstarter.web.rest.vm;

import workstarter.domain.Company;
import workstarter.service.dto.CompanyAdminDTO;
import javax.validation.constraints.Size;

import java.time.ZonedDateTime;
import java.util.Set;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedCompanyAdminVM extends CompanyAdminDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public ManagedCompanyAdminVM() {
        // Empty constructor needed for Jackson.
    }

    public ManagedCompanyAdminVM(Long id, String login, String password, String firstName, String lastName,
                         String email, boolean activated, String imageUrl, String langKey,
                         String createdBy, ZonedDateTime createdDate, String lastModifiedBy, ZonedDateTime lastModifiedDate,
                        Set<String> authorities, String location, Company company) {

        super(id, login,email, activated, imageUrl, langKey, firstName, lastName, 
            createdBy, createdDate, lastModifiedBy, lastModifiedDate,  authorities, location, company);

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" +
            "} " + super.toString();
    }
}
