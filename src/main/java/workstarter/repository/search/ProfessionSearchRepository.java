package workstarter.repository.search;

import workstarter.domain.Profession;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Profession entity.
 */
public interface ProfessionSearchRepository extends ElasticsearchRepository<Profession, Long> {
}
