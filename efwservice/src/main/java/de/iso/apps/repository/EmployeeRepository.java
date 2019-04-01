package de.iso.apps.repository;

import de.iso.apps.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Employee entity.
 */
@Repository public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
