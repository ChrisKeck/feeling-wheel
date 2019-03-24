package de.iso.apps.repository;

import de.iso.apps.domain.Feeling;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Feeling entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeelingRepository extends JpaRepository<Feeling, Long> {

}
