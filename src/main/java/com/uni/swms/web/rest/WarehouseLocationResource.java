package com.uni.swms.web.rest;

import com.uni.swms.repository.WarehouseLocationRepository;
import com.uni.swms.service.WarehouseLocationQueryService;
import com.uni.swms.service.WarehouseLocationService;
import com.uni.swms.service.criteria.WarehouseLocationCriteria;
import com.uni.swms.service.dto.WarehouseLocationDTO;
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
 * REST controller for managing {@link com.uni.swms.domain.WarehouseLocation}.
 */
@RestController
@RequestMapping("/api/warehouse-locations")
public class WarehouseLocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(WarehouseLocationResource.class);

    private static final String ENTITY_NAME = "warehouseLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarehouseLocationService warehouseLocationService;

    private final WarehouseLocationRepository warehouseLocationRepository;

    private final WarehouseLocationQueryService warehouseLocationQueryService;

    public WarehouseLocationResource(
        WarehouseLocationService warehouseLocationService,
        WarehouseLocationRepository warehouseLocationRepository,
        WarehouseLocationQueryService warehouseLocationQueryService
    ) {
        this.warehouseLocationService = warehouseLocationService;
        this.warehouseLocationRepository = warehouseLocationRepository;
        this.warehouseLocationQueryService = warehouseLocationQueryService;
    }

    /**
     * {@code POST  /warehouse-locations} : Create a new warehouseLocation.
     *
     * @param warehouseLocationDTO the warehouseLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warehouseLocationDTO, or with status {@code 400 (Bad Request)} if the warehouseLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WarehouseLocationDTO> createWarehouseLocation(@Valid @RequestBody WarehouseLocationDTO warehouseLocationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save WarehouseLocation : {}", warehouseLocationDTO);
        if (warehouseLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new warehouseLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        warehouseLocationDTO = warehouseLocationService.save(warehouseLocationDTO);
        return ResponseEntity.created(new URI("/api/warehouse-locations/" + warehouseLocationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, warehouseLocationDTO.getId().toString()))
            .body(warehouseLocationDTO);
    }

    /**
     * {@code PUT  /warehouse-locations/:id} : Updates an existing warehouseLocation.
     *
     * @param id the id of the warehouseLocationDTO to save.
     * @param warehouseLocationDTO the warehouseLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouseLocationDTO,
     * or with status {@code 400 (Bad Request)} if the warehouseLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warehouseLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseLocationDTO> updateWarehouseLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WarehouseLocationDTO warehouseLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WarehouseLocation : {}, {}", id, warehouseLocationDTO);
        if (warehouseLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouseLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warehouseLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        warehouseLocationDTO = warehouseLocationService.update(warehouseLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warehouseLocationDTO.getId().toString()))
            .body(warehouseLocationDTO);
    }

    /**
     * {@code PATCH  /warehouse-locations/:id} : Partial updates given fields of an existing warehouseLocation, field will ignore if it is null
     *
     * @param id the id of the warehouseLocationDTO to save.
     * @param warehouseLocationDTO the warehouseLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warehouseLocationDTO,
     * or with status {@code 400 (Bad Request)} if the warehouseLocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the warehouseLocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the warehouseLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WarehouseLocationDTO> partialUpdateWarehouseLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WarehouseLocationDTO warehouseLocationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WarehouseLocation partially : {}, {}", id, warehouseLocationDTO);
        if (warehouseLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warehouseLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warehouseLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WarehouseLocationDTO> result = warehouseLocationService.partialUpdate(warehouseLocationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warehouseLocationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /warehouse-locations} : get all the warehouseLocations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warehouseLocations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WarehouseLocationDTO>> getAllWarehouseLocations(
        WarehouseLocationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get WarehouseLocations by criteria: {}", criteria);

        Page<WarehouseLocationDTO> page = warehouseLocationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /warehouse-locations/count} : count all the warehouseLocations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWarehouseLocations(WarehouseLocationCriteria criteria) {
        LOG.debug("REST request to count WarehouseLocations by criteria: {}", criteria);
        return ResponseEntity.ok().body(warehouseLocationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /warehouse-locations/:id} : get the "id" warehouseLocation.
     *
     * @param id the id of the warehouseLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warehouseLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseLocationDTO> getWarehouseLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WarehouseLocation : {}", id);
        Optional<WarehouseLocationDTO> warehouseLocationDTO = warehouseLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warehouseLocationDTO);
    }

    /**
     * {@code DELETE  /warehouse-locations/:id} : delete the "id" warehouseLocation.
     *
     * @param id the id of the warehouseLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouseLocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WarehouseLocation : {}", id);
        warehouseLocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
