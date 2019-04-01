package de.iso.apps.service;

import de.iso.apps.service.dto.FeelingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Feeling.
 */
public interface FeelingService {
    
    /**
     * Save a feeling.
     *
     * @param feelingDTO the entity to save
     * @return the persisted entity
     */
    FeelingDTO save(FeelingDTO feelingDTO);
    
    /**
     * Get all the feelings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FeelingDTO> findAll(Pageable pageable);
    
    
    /**
     * Get the "id" feeling.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<FeelingDTO> findOne(Long id);
    
    /**
     * Delete the "id" feeling.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     * Search for the feeling corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FeelingDTO> search(String query, Pageable pageable);
}
