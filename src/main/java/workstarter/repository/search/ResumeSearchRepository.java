package workstarter.repository.search;

import workstarter.domain.Resume;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Resume entity.
 */
public interface ResumeSearchRepository extends ElasticsearchRepository<Resume, Long> {
}
