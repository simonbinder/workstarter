package workstarter.repository.search;

import workstarter.domain.Jobadvertisment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Jobadvertisment entity.
 */
public interface JobadvertismentSearchRepository extends ElasticsearchRepository<Jobadvertisment, Long> {
}
