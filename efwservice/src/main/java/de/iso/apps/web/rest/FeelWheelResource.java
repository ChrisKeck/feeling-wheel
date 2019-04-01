package de.iso.apps.web.rest;

import de.iso.apps.service.FeelWheelService;
import de.iso.apps.service.dto.FeelWheelDTO;
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
 * REST controller for managing FeelWheel.
 */
@RestController @RequestMapping("/api") public class FeelWheelResource {
    
    private static final String ENTITY_NAME = "efwserviceFeelWheel";
    private final Logger log = LoggerFactory.getLogger(FeelWheelResource.class);
    private final FeelWheelService feelWheelService;
    
    public FeelWheelResource(FeelWheelService feelWheelService) {
        this.feelWheelService = feelWheelService;
    }
    
    /**
     * POST  /feel-wheels : Create a new feelWheel.
     *
     * @param feelWheelDTO the feelWheelDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new feelWheelDTO, or with status 400 (Bad
     * Request) if the feelWheel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/feel-wheels")
    public ResponseEntity<FeelWheelDTO> createFeelWheel(@Valid @RequestBody FeelWheelDTO feelWheelDTO)
    throws URISyntaxException {
        log.debug("REST request to save FeelWheel : {}", feelWheelDTO);
        if (feelWheelDTO.getId() != null) {
            throw new BadRequestAlertException("A new feelWheel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeelWheelDTO result = feelWheelService.save(feelWheelDTO);
        return ResponseEntity.created(new URI("/api/feel-wheels/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                             .body(result);
    }
    
    /**
     * PUT  /feel-wheels : Updates an existing feelWheel.
     *
     * @param feelWheelDTO the feelWheelDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated feelWheelDTO, or with status 400 (Bad
     * Request) if the feelWheelDTO is not valid, or with status 500 (Internal Server Error) if the feelWheelDTO
     * couldn't be updated
     */
    @PutMapping("/feel-wheels")
    public ResponseEntity<FeelWheelDTO> updateFeelWheel(@Valid @RequestBody FeelWheelDTO feelWheelDTO) {
        log.debug("REST request to update FeelWheel : {}", feelWheelDTO);
        if (feelWheelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FeelWheelDTO result = feelWheelService.save(feelWheelDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
                                                                              feelWheelDTO.getId().toString())).body(
            result);
    }
    
    /**
     * GET  /feel-wheels : get all the feelWheels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of feelWheels in body
     */
    @GetMapping("/feel-wheels")
    public ResponseEntity<List<FeelWheelDTO>> getAllFeelWheels(Pageable pageable) {
        log.debug("REST request to get a page of FeelWheels");
        Page<FeelWheelDTO> page = feelWheelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/feel-wheels");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /feel-wheels/:id : get the "id" feelWheel.
     *
     * @param id the id of the feelWheelDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the feelWheelDTO, or with status 404 (Not Found)
     */
    @GetMapping("/feel-wheels/{id}")
    public ResponseEntity<FeelWheelDTO> getFeelWheel(@PathVariable Long id) {
        log.debug("REST request to get FeelWheel : {}", id);
        Optional<FeelWheelDTO> feelWheelDTO = feelWheelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feelWheelDTO);
    }
    
    /**
     * DELETE  /feel-wheels/:id : delete the "id" feelWheel.
     *
     * @param id the id of the feelWheelDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/feel-wheels/{id}")
    public ResponseEntity<Void> deleteFeelWheel(@PathVariable Long id) {
        log.debug("REST request to delete FeelWheel : {}", id);
        feelWheelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * SEARCH  /_search/feel-wheels?query=:query : search for the feelWheel corresponding to the query.
     *
     * @param query the query of the feelWheel search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/feel-wheels")
    public ResponseEntity<List<FeelWheelDTO>> searchFeelWheels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FeelWheels for query {}", query);
        Page<FeelWheelDTO> page = feelWheelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query,
                                                                                 page,
                                                                                 "/api/_search/feel-wheels");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
}
