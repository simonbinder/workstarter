package workstarter.repository;

import workstarter.domain.Resume;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Resume entity.
 */
@SuppressWarnings("unused")
public interface ResumeRepository extends JpaRepository<Resume,Long> {

}
