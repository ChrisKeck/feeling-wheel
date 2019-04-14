package de.iso.apps.service.impl;

import de.iso.apps.config.audit.AuditEventConverter;
import de.iso.apps.repository.PersistenceAuditEventRepository;
import de.iso.apps.service.AuditEventService;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 */
@Service @Transactional public class AuditEventServiceImpl implements AuditEventService {
    
    private final PersistenceAuditEventRepository persistenceAuditEventRepository;
    
    private final AuditEventConverter auditEventConverter;
    
    public AuditEventServiceImpl(PersistenceAuditEventRepository persistenceAuditEventRepository,
                                 AuditEventConverter auditEventConverter) {
        
        this.persistenceAuditEventRepository = persistenceAuditEventRepository;
        this.auditEventConverter = auditEventConverter;
    }
    
    @Override
    public Page<AuditEvent> findAll(Pageable pageable) {
        return persistenceAuditEventRepository.findAll(pageable).map(auditEventConverter::convertToAuditEvent);
    }
    
    @Override
    public Page<AuditEvent> findByDates(Instant fromDate, Instant toDate, Pageable pageable) {
        return persistenceAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate, pageable).map(
            auditEventConverter::convertToAuditEvent);
    }
    
    @Override
    public Optional<AuditEvent> find(Long id) {
        return Optional.of(persistenceAuditEventRepository.findById(id)).filter(Optional::isPresent).map(
            Optional::get).map(auditEventConverter::convertToAuditEvent);
    }
}
