package workstarter.service.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import workstarter.domain.Authority;
import workstarter.domain.CompanyAdmin;
import workstarter.domain.Student;
import workstarter.service.dto.CompanyAdminDTO;
import workstarter.service.dto.StudentDTO;

@Mapper(componentModel = "spring", uses = {})
public interface CompanyAdminMapper {

    CompanyAdminDTO companyToCompanyDTO(CompanyAdmin company);

    List<CompanyAdminDTO> companiesToCompanyDTOs(List<CompanyAdmin> companies);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "activationKey", ignore = true)
    @Mapping(target = "resetKey", ignore = true)
    @Mapping(target = "resetDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    CompanyAdmin companyDTOToCompany(CompanyAdminDTO companyDTO);

    List<CompanyAdmin> companyDTOsToCompanies(List<CompanyAdminDTO> companyDTOs);

    default CompanyAdmin companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        CompanyAdmin company = new CompanyAdmin();
        company.setId(id);
        return company;
    }

    default Set<String> stringsFromAuthorities (Set<Authority> authorities) {
        return authorities.stream().map(Authority::getName)
            .collect(Collectors.toSet());
    }

    default Set<Authority> authoritiesFromStrings(Set<String> strings) {
        return strings.stream().map(string -> {
            Authority auth = new Authority();
            auth.setName(string);
            return auth;
        }).collect(Collectors.toSet());
    }
}
