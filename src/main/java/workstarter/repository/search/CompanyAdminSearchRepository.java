package workstarter.repository.search;

import workstarter.domain.CompanyAdmin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CompanyAdmin entity.
 */
public interface CompanyAdminSearchRepository extends ElasticsearchRepository<CompanyAdmin, Long> {
}
