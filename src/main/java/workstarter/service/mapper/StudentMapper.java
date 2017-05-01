package workstarter.service.mapper;

import workstarter.domain.Authority;
import workstarter.domain.Offering;
import workstarter.domain.Resume;
import workstarter.domain.Searching;
import workstarter.domain.Student;
import workstarter.domain.User;
import workstarter.service.dto.StudentDTO;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity User and its DTO UserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StudentMapper {

    StudentDTO studentStudentDTO(Student student);

    List<StudentDTO> studentsToStudentDTOs(List<Student> students);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "activationKey", ignore = true)
    @Mapping(target = "resetKey", ignore = true)
    @Mapping(target = "resetDate", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "portfolio", ignore = true)
    @Mapping(target = "university", ignore = true)
    Student studentDTOToStudent(StudentDTO studentDTO);

    List<Student> studentDTOsToStudents(List<StudentDTO> studentDTOs);

    default Student userFromId(Long id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return student;
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
    
    default Set<String> stringsFromOfferings (Set<Offering> offerings) {
        return offerings.stream().map(Offering::getOfferingvalues)
            .collect(Collectors.toSet());
    }
    
    default Set<Offering> offeringsFromStrings(Set<String> strings) {
        return strings.stream().map(string -> {
            Offering off = new Offering();
            off.setOfferingvalues(string);
            return off;
        }).collect(Collectors.toSet());
    }
    
    default Set<String> stringsFromSearchings (Set<Searching> searchings) {
        return searchings.stream().map(Searching::getSearchingvalues)
            .collect(Collectors.toSet());
    }
    
    default Set<Searching> searchingsFromStrings(Set<String> strings) {
        return strings.stream().map(string -> {
            Searching search = new Searching();
            search.setSearchingvalues(string);
            return search;
        }).collect(Collectors.toSet());
    }
    
    default Set<String> stringsFromResume (Set<Resume> resumes){
    	return resumes.stream().map(Resume::getTitle).collect(Collectors.toSet());
    }
    
    default Set<Resume> resumesFromStrings(Set<String> strings) {
    	return strings.stream().map(string -> {
    		Resume resume = new Resume();
    		resume.setTitle(string);
    		return resume;
    	}).collect(Collectors.toSet());
    }
}
