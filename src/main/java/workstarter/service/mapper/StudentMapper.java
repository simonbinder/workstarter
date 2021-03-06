package workstarter.service.mapper;

import workstarter.domain.Authority;
import workstarter.domain.Keywords;
import workstarter.domain.Profession;
import workstarter.domain.Project;
import workstarter.domain.Resume;
import workstarter.domain.School;
import workstarter.domain.Student;
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
    @Mapping(target = "slogan", ignore = true)
    @Mapping(target = "title", ignore = true)
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
    
    default List<String> stringsFromSchools (List<School> schools){
    	return schools.stream().map(School::getName).collect(Collectors.toList());
    }
    
    default List<School> schoolsFromStrings(List<String> strings) {
    	return strings.stream().map(string -> {
    		School school = new School();
    		school.setName(string);
    		return school;
    	}).collect(Collectors.toList());
    }
    
    default List<String> stringsFromKeywords (List<Keywords> keyword){
    	return keyword.stream().map(Keywords::getName).collect(Collectors.toList());
    }
    
    default List<Keywords> keywordsFromStrings(List<String> strings) {
    	return strings.stream().map(string -> {
    		Keywords keyword = new Keywords();
    		keyword.setName(string);
    		return keyword;
    	}).collect(Collectors.toList());
    }
    
    default List<String> stringsFromProfessions (List<Profession> profession){
    	return profession.stream().map(Profession::getCompanyName).collect(Collectors.toList());
    }
    
    default List<Profession> professionsFromStrings(List<String> strings) {
    	return strings.stream().map(string -> {
    		Profession profession = new Profession();
    		profession.setCompanyName(string);
    		return profession;
    	}).collect(Collectors.toList());
    }
    
    default List<String> stringsFromProjects (List<Project> project){
    	return project.stream().map(Project::getTitle).collect(Collectors.toList());
    }
    
    default List<Project> projectsFromStrings(List<String> strings) {
    	return strings.stream().map(string -> {
    		Project project = new Project();
    		project.setTitle(string);
    		return project;
    	}).collect(Collectors.toList());
    }
}
