package workstarter.repository;

import workstarter.domain.Profession;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Profession entity.
 */
@SuppressWarnings("unused")
public interface ProfessionRepository extends JpaRepository<Profession,Long> {

}
