package workstarter.repository.search;

import workstarter.domain.Keywords;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Keywords entity.
 */
public interface KeywordsSearchRepository extends ElasticsearchRepository<Keywords, Long> {
}
