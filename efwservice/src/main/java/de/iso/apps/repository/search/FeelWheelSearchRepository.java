package de.iso.apps.repository.search;

import de.iso.apps.domain.FeelWheel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FeelWheel entity.
 */
public interface FeelWheelSearchRepository extends ElasticsearchRepository<FeelWheel, Long> {
}
