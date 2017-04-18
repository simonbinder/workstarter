package workstarter.repository;

import workstarter.domain.Jobadvertisment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Jobadvertisment entity.
 */
@SuppressWarnings("unused")
public interface JobadvertismentRepository extends JpaRepository<Jobadvertisment,Long> {

}
