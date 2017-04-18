package workstarter.repository;

import workstarter.domain.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Student entity.
 */
@SuppressWarnings("unused")
public interface StudentRepository extends JpaRepository<Student,Long> {

    Optional<Student> findOneByActivationKey(String activationKey);

    List<Student> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<Student> findOneByResetKey(String resetKey);

    Optional<Student> findOneByEmail(String email);

    Optional<Student> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    Student findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<Student> findOneWithAuthoritiesByLogin(String login);

    Page<Student> findAllByLoginNot(Pageable pageable, String login);
}
