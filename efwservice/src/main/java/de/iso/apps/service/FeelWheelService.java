package de.iso.apps.service;

import de.iso.apps.service.dto.FeelWheelDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing FeelWheel.
 */
public interface FeelWheelService {
    
    /**
     * Save a feelWheel.
     *
     * @param feelWheelDTO the entity to save
     * @return the persisted entity
     */
    FeelWheelDTO save(FeelWheelDTO feelWheelDTO);
    
    /**
     * Get all the feelWheels.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FeelWheelDTO> findAll(Pageable pageable);
    
    
    /**
     * Get the "id" feelWheel.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FeelWheelDTO> findOne(Long id);
    
    /**
     * Delete the "id" feelWheel.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     * Search for the feelWheel corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FeelWheelDTO> search(String query, Pageable pageable);
}
