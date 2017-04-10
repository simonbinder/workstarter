package workstarter.repository.search;

import workstarter.domain.Offering;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Offering entity.
 */
public interface OfferingSearchRepository extends ElasticsearchRepository<Offering, Long> {
}
