package workstarter.repository;

import workstarter.domain.Keywords;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Keywords entity.
 */
@SuppressWarnings("unused")
public interface KeywordsRepository extends JpaRepository<Keywords,Long> {

}
