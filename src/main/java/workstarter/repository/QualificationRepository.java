package workstarter.repository;

import workstarter.domain.Qualification;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Qualification entity.
 */
@SuppressWarnings("unused")
public interface QualificationRepository extends JpaRepository<Qualification,Long> {

}
