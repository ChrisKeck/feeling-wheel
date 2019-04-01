package de.iso.apps.service.impl;

import de.iso.apps.domain.Employee;
import de.iso.apps.repository.EmployeeRepository;
import de.iso.apps.repository.search.EmployeeSearchRepository;
import de.iso.apps.service.EmployeeService;
import de.iso.apps.service.MailChangingService;
import de.iso.apps.service.dto.EmployeeDTO;
import de.iso.apps.service.mapper.EmployeeMapper;
import de.iso.apps.service.mapper.MailChangingMapper;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Employee.
 */
@Service @Transactional public class EmployeeServiceImpl implements EmployeeService {
    
    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    
    private final EmployeeRepository employeeRepository;
    
    private final EmployeeMapper employeeMapper;
    
    private final EmployeeSearchRepository employeeSearchRepository;
    private final MailChangingService mailChangingService;
    private final MailChangingMapper mailChangingMapper;
    
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper,
                               EmployeeSearchRepository employeeSearchRepository,
                               MailChangingService mailChangingService,
                               MailChangingMapper mailChangingMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.employeeSearchRepository = employeeSearchRepository;
        this.mailChangingService = mailChangingService;
        this.mailChangingMapper = mailChangingMapper;
    }
    
    /**
     * Save a employee.
     *
     * @param employeeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        Optional<EmployeeDTO> oldemployee = Optional.empty();
        if (employeeDTO.getId() != null) {
            oldemployee = this.findOne(employeeDTO.getId());
        }
        Employee employee = employeeMapper.toEntity(employeeDTO);
    
        employee = employeeRepository.save(employee);
        EmployeeDTO result = employeeMapper.toDto(employee);
    
    
        employeeSearchRepository.save(employee);
        oldemployee.map((EmployeeDTO item) -> {
            var mailChanging = mailChangingMapper.employeeDTOToMailChangingDTO(result, item);
            if (!Objects.equals(mailChanging.getNewMail(), mailChanging.getOldMail())) {
                mailChangingService.propagate(mailChanging);
            }
            return item;
        });
        return result;
    }
    
    /**
     * Get all the employees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable).map(employeeMapper::toDto);
    }
    
    
    /**
     * Get one employee by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id).map(employeeMapper::toDto);
    }
    
    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        var employee = this.findOne(id);
        employeeRepository.deleteById(id);
        employeeSearchRepository.deleteById(id);
        employee.ifPresent(item -> {
            var mailChanging = mailChangingMapper.employeeDTOToMailChangingDTO(null, item);
            mailChangingService.propagate(mailChanging);
        });
    }
    
    
    /**
     * Search for the employee corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Employees for query {}", query);
        return employeeSearchRepository.search(queryStringQuery(query), pageable).map(employeeMapper::toDto);
    }
}
