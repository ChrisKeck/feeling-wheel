package de.iso.apps.repository;

import de.iso.apps.domain.FeelWheel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FeelWheel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeelWheelRepository extends JpaRepository<FeelWheel, Long> {

}
