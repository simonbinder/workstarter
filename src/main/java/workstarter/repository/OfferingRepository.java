package workstarter.repository;

import workstarter.domain.Offering;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Offering entity.
 */
@SuppressWarnings("unused")
public interface OfferingRepository extends JpaRepository<Offering,Long> {

}
