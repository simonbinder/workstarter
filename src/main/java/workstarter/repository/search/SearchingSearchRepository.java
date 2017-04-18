package workstarter.repository.search;

import workstarter.domain.Searching;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Searching entity.
 */
public interface SearchingSearchRepository extends ElasticsearchRepository<Searching, Long> {
}
