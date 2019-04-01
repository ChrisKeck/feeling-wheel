package de.iso.apps.repository.search;

import de.iso.apps.domain.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

/**
 * Spring Data Elasticsearch repository for the Employee entity.
 */
public interface EmployeeSearchRepository extends ElasticsearchRepository<Employee, Long> {
    Optional<Employee> findOneByEmailIgnoreCase(String email);
    
}
