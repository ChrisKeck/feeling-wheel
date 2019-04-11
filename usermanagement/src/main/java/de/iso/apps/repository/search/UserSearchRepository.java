package de.iso.apps.repository.search;

import de.iso.apps.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
    void findOneByEmailIgnoreCase(String oldMail);
}
