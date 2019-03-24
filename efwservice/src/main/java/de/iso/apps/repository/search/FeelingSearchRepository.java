package de.iso.apps.repository.search;

import de.iso.apps.domain.Feeling;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Feeling entity.
 */
public interface FeelingSearchRepository extends ElasticsearchRepository<Feeling, Long> {
}
