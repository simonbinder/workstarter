package workstarter.repository;

import workstarter.domain.CompanyAdmin;
import workstarter.domain.Student;
import workstarter.domain.User;

import java.time.ZonedDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface CompanyRepository extends JpaRepository<CompanyAdmin, Long> {

    Optional<CompanyAdmin> findOneByActivationKey(String activationKey);

    List<CompanyAdmin> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<CompanyAdmin> findOneByResetKey(String resetKey);

    Optional<CompanyAdmin> findOneByEmail(String email);

    Optional<CompanyAdmin> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    CompanyAdmin findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    Optional<CompanyAdmin> findOneWithAuthoritiesByLogin(String login);

    Page<CompanyAdmin> findAllByLoginNot(Pageable pageable, String login);
}
