package workstarter.repository;

import workstarter.domain.Searching;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Searching entity.
 */
@SuppressWarnings("unused")
public interface SearchingRepository extends JpaRepository<Searching,Long> {

}
