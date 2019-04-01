package de.iso.apps.web.rest;

import de.iso.apps.service.FeelingService;
import de.iso.apps.service.dto.FeelingDTO;
import de.iso.apps.web.rest.errors.BadRequestAlertException;
import de.iso.apps.web.rest.util.HeaderUtil;
import de.iso.apps.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Feeling.
 */
@RestController @RequestMapping("/api") public class FeelingResource {
    
    private static final String ENTITY_NAME = "efwserviceFeeling";
    private final Logger log = LoggerFactory.getLogger(FeelingResource.class);
    private final FeelingService feelingService;
    
    public FeelingResource(FeelingService feelingService) {
        this.feelingService = feelingService;
    }
    
    /**
     * POST  /feelings : Create a new feeling.
     *
     * @param feelingDTO the feelingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new feelingDTO, or with status 400 (Bad
     * Request) if the feeling has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/feelings")
    public ResponseEntity<FeelingDTO> createFeeling(@Valid @RequestBody FeelingDTO feelingDTO)
    throws URISyntaxException {
        log.debug("REST request to save Feeling : {}", feelingDTO);
        if (feelingDTO.getId() != null) {
            throw new BadRequestAlertException("A new feeling cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeelingDTO result = feelingService.save(feelingDTO);
        return ResponseEntity.created(new URI("/api/feelings/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                             .body(result);
    }
    
    /**
     * PUT  /feelings : Updates an existing feeling.
     *
     * @param feelingDTO the feelingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated feelingDTO, or with status 400 (Bad
     * Request) if the feelingDTO is not valid, or with status 500 (Internal Server Error) if the feelingDTO couldn't be
     * updated
     */
    @PutMapping("/feelings")
    public ResponseEntity<FeelingDTO> updateFeeling(@Valid @RequestBody FeelingDTO feelingDTO) {
        log.debug("REST request to update Feeling : {}", feelingDTO);
        if (feelingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FeelingDTO result = feelingService.save(feelingDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
                                                                              feelingDTO.getId().toString())).body(
            result);
    }
    
    /**
     * GET  /feelings : get all the feelings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of feelings in body
     */
    @GetMapping("/feelings")
    public ResponseEntity<List<FeelingDTO>> getAllFeelings(Pageable pageable) {
        log.debug("REST request to get a page of Feelings");
        Page<FeelingDTO> page = feelingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/feelings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /feelings/:id : get the "id" feeling.
     *
     * @param id the id of the feelingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the feelingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/feelings/{id}")
    public ResponseEntity<FeelingDTO> getFeeling(@PathVariable Long id) {
        log.debug("REST request to get Feeling : {}", id);
        Optional<FeelingDTO> feelingDTO = feelingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feelingDTO);
    }
    
    /**
     * DELETE  /feelings/:id : delete the "id" feeling.
     *
     * @param id the id of the feelingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/feelings/{id}")
    public ResponseEntity<Void> deleteFeeling(@PathVariable Long id) {
        log.debug("REST request to delete Feeling : {}", id);
        feelingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * SEARCH  /_search/feelings?query=:query : search for the feeling corresponding to the query.
     *
     * @param query the query of the feeling search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/feelings")
    public ResponseEntity<List<FeelingDTO>> searchFeelings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Feelings for query {}", query);
        Page<FeelingDTO> page = feelingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/feelings");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
}
