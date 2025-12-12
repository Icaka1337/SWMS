package com.uni.swms.web.rest;

import com.uni.swms.repository.SystemLogRepository;
import com.uni.swms.service.SystemLogQueryService;
import com.uni.swms.service.SystemLogService;
import com.uni.swms.service.criteria.SystemLogCriteria;
import com.uni.swms.service.dto.SystemLogDTO;
import com.uni.swms.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.uni.swms.domain.SystemLog}.
 */
@RestController
@RequestMapping("/api/system-logs")
public class SystemLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(SystemLogResource.class);

    private static final String ENTITY_NAME = "systemLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemLogService systemLogService;

    private final SystemLogRepository systemLogRepository;

    private final SystemLogQueryService systemLogQueryService;

    public SystemLogResource(
        SystemLogService systemLogService,
        SystemLogRepository systemLogRepository,
        SystemLogQueryService systemLogQueryService
    ) {
        this.systemLogService = systemLogService;
        this.systemLogRepository = systemLogRepository;
        this.systemLogQueryService = systemLogQueryService;
    }

    /**
     * {@code POST  /system-logs} : Create a new systemLog.
     *
     * @param systemLogDTO the systemLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemLogDTO, or with status {@code 400 (Bad Request)} if the systemLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SystemLogDTO> createSystemLog(@Valid @RequestBody SystemLogDTO systemLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save SystemLog : {}", systemLogDTO);
        if (systemLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemLogDTO = systemLogService.save(systemLogDTO);
        return ResponseEntity.created(new URI("/api/system-logs/" + systemLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, systemLogDTO.getId().toString()))
            .body(systemLogDTO);
    }

    /**
     * {@code PUT  /system-logs/:id} : Updates an existing systemLog.
     *
     * @param id the id of the systemLogDTO to save.
     * @param systemLogDTO the systemLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemLogDTO,
     * or with status {@code 400 (Bad Request)} if the systemLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SystemLogDTO> updateSystemLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemLogDTO systemLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SystemLog : {}, {}", id, systemLogDTO);
        if (systemLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        systemLogDTO = systemLogService.update(systemLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemLogDTO.getId().toString()))
            .body(systemLogDTO);
    }

    /**
     * {@code PATCH  /system-logs/:id} : Partial updates given fields of an existing systemLog, field will ignore if it is null
     *
     * @param id the id of the systemLogDTO to save.
     * @param systemLogDTO the systemLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemLogDTO,
     * or with status {@code 400 (Bad Request)} if the systemLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemLogDTO> partialUpdateSystemLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemLogDTO systemLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SystemLog partially : {}, {}", id, systemLogDTO);
        if (systemLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemLogDTO> result = systemLogService.partialUpdate(systemLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-logs} : get all the systemLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SystemLogDTO>> getAllSystemLogs(
        SystemLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SystemLogs by criteria: {}", criteria);

        Page<SystemLogDTO> page = systemLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-logs/count} : count all the systemLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSystemLogs(SystemLogCriteria criteria) {
        LOG.debug("REST request to count SystemLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /system-logs/:id} : get the "id" systemLog.
     *
     * @param id the id of the systemLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemLogDTO> getSystemLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SystemLog : {}", id);
        Optional<SystemLogDTO> systemLogDTO = systemLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemLogDTO);
    }

    /**
     * {@code DELETE  /system-logs/:id} : delete the "id" systemLog.
     *
     * @param id the id of the systemLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SystemLog : {}", id);
        systemLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
