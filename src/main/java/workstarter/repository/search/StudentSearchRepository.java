package workstarter.repository.search;

import workstarter.domain.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Student entity.
 */
public interface StudentSearchRepository extends ElasticsearchRepository<Student, Long> {
}
