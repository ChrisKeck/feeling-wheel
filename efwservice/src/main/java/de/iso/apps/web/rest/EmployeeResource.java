package de.iso.apps.web.rest;

import de.iso.apps.service.EmployeeService;
import de.iso.apps.service.MailChangingService;
import de.iso.apps.service.dto.EmployeeDTO;
import de.iso.apps.web.rest.errors.BadRequestAlertException;
import de.iso.apps.web.rest.util.HeaderUtil;
import de.iso.apps.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.var;
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
 * REST controller for managing Employee.
 */
@RestController @RequestMapping("/api") public class EmployeeResource {
    
    private static final String ENTITY_NAME = "efwserviceEmployee";
    private final Logger log = LoggerFactory.getLogger(EmployeeResource.class);
    private final EmployeeService employeeService;
    private final MailChangingService mailChangingService;
    
    public EmployeeResource(EmployeeService employeeService, MailChangingService mailChangingService) {
        this.employeeService = employeeService;
        this.mailChangingService = mailChangingService;
    }
    
    /**
     * POST  /employees : Create a new employee.
     *
     * @param employeeDTO the employeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new employeeDTO, or with status 400 (Bad
     * Request) if the employee has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO)
    throws URISyntaxException {
        log.debug("REST request to save Employee : {}", employeeDTO);
        if (employeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new employee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmployeeDTO result = employeeService.save(employeeDTO);
        mailChangingService.propagate(result, null);
        return ResponseEntity.created(new URI("/api/employees/" + result.getId()))
                             .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                             .body(result);
    }
    
    /**
     * PUT  /employees : Updates an existing employee.
     *
     * @param employeeDTO the employeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated employeeDTO, or with status 400 (Bad
     * Request) if the employeeDTO is not valid, or with status 500 (Internal Server Error) if the employeeDTO couldn't
     * be updated
     */
    @PutMapping("/employees")
    public ResponseEntity<EmployeeDTO> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.debug("REST request to update Employee : {}", employeeDTO);
        if (employeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EmployeeDTO result = employeeService.save(employeeDTO);
        mailChangingService.propagate(result, employeeDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME,
                                                                              employeeDTO.getId().toString())).body(
            result);
    }
    
    /**
     * GET  /employees : get all the employees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of employees in body
     */
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(Pageable pageable) {
        log.debug("REST request to get a page of Employees");
        Page<EmployeeDTO> page = employeeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employees");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
    /**
     * GET  /employees/:id : get the "id" employee.
     *
     * @param id the id of the employeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the employeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get Employee : {}", id);
        Optional<EmployeeDTO> employeeDTO = employeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeDTO);
    }
    
    /**
     * DELETE  /employees/:id : delete the "id" employee.
     *
     * @param id the id of the employeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.debug("REST request to delete Employee : {}", id);
        var olEmp = employeeService.findOne(id);
        employeeService.delete(id);
        olEmp.ifPresent(item -> mailChangingService.propagate(null, item));
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * SEARCH  /_search/employees?query=:query : search for the employee corresponding to the query.
     *
     * @param query the query of the employee search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/employees")
    public ResponseEntity<List<EmployeeDTO>> searchEmployees(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Employees for query {}", query);
        Page<EmployeeDTO> page = employeeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/employees");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    
}
