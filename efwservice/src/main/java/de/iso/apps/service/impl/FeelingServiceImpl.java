package de.iso.apps.service.impl;

import de.iso.apps.domain.Feeling;
import de.iso.apps.repository.FeelingRepository;
import de.iso.apps.repository.search.FeelingSearchRepository;
import de.iso.apps.service.FeelingService;
import de.iso.apps.service.dto.FeelingDTO;
import de.iso.apps.service.mapper.FeelingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Feeling.
 */
@Service @Transactional public class FeelingServiceImpl implements FeelingService {
    
    private final Logger log = LoggerFactory.getLogger(FeelingServiceImpl.class);
    
    private final FeelingRepository feelingRepository;
    
    private final FeelingMapper feelingMapper;
    
    private final FeelingSearchRepository feelingSearchRepository;
    
    public FeelingServiceImpl(FeelingRepository feelingRepository,
                              FeelingMapper feelingMapper,
                              FeelingSearchRepository feelingSearchRepository) {
        this.feelingRepository = feelingRepository;
        this.feelingMapper = feelingMapper;
        this.feelingSearchRepository = feelingSearchRepository;
    }
    
    /**
     * Save a feeling.
     *
     * @param feelingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public FeelingDTO save(FeelingDTO feelingDTO) {
        log.debug("Request to save Feeling : {}", feelingDTO);
        Feeling feeling = feelingMapper.toEntity(feelingDTO);
        feeling = feelingRepository.save(feeling);
        FeelingDTO result = feelingMapper.toDto(feeling);
        feelingSearchRepository.save(feeling);
        return result;
    }
    
    /**
     * Get all the feelings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FeelingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Feelings");
        return feelingRepository.findAll(pageable).map(feelingMapper::toDto);
    }
    
    
    /**
     * Get one feeling by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FeelingDTO> findOne(Long id) {
        log.debug("Request to get Feeling : {}", id);
        return feelingRepository.findById(id).map(feelingMapper::toDto);
    }
    
    /**
     * Delete the feeling by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Feeling : {}", id);
        feelingRepository.deleteById(id);
        feelingSearchRepository.deleteById(id);
    }
    
    /**
     * Search for the feeling corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FeelingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Feelings for query {}", query);
        return feelingSearchRepository.search(queryStringQuery(query), pageable).map(feelingMapper::toDto);
    }
}
