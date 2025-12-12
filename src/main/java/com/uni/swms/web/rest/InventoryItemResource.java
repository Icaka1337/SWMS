package com.uni.swms.web.rest;

import com.uni.swms.repository.InventoryItemRepository;
import com.uni.swms.service.InventoryItemQueryService;
import com.uni.swms.service.InventoryItemService;
import com.uni.swms.service.criteria.InventoryItemCriteria;
import com.uni.swms.service.dto.InventoryItemDTO;
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
 * REST controller for managing {@link com.uni.swms.domain.InventoryItem}.
 */
@RestController
@RequestMapping("/api/inventory-items")
public class InventoryItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryItemResource.class);

    private static final String ENTITY_NAME = "inventoryItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventoryItemService inventoryItemService;

    private final InventoryItemRepository inventoryItemRepository;

    private final InventoryItemQueryService inventoryItemQueryService;

    public InventoryItemResource(
        InventoryItemService inventoryItemService,
        InventoryItemRepository inventoryItemRepository,
        InventoryItemQueryService inventoryItemQueryService
    ) {
        this.inventoryItemService = inventoryItemService;
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryItemQueryService = inventoryItemQueryService;
    }

    /**
     * {@code POST  /inventory-items} : Create a new inventoryItem.
     *
     * @param inventoryItemDTO the inventoryItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventoryItemDTO, or with status {@code 400 (Bad Request)} if the inventoryItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InventoryItemDTO> createInventoryItem(@Valid @RequestBody InventoryItemDTO inventoryItemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save InventoryItem : {}", inventoryItemDTO);
        if (inventoryItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new inventoryItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inventoryItemDTO = inventoryItemService.save(inventoryItemDTO);
        return ResponseEntity.created(new URI("/api/inventory-items/" + inventoryItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, inventoryItemDTO.getId().toString()))
            .body(inventoryItemDTO);
    }

    /**
     * {@code PUT  /inventory-items/:id} : Updates an existing inventoryItem.
     *
     * @param id the id of the inventoryItemDTO to save.
     * @param inventoryItemDTO the inventoryItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoryItemDTO,
     * or with status {@code 400 (Bad Request)} if the inventoryItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventoryItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> updateInventoryItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InventoryItemDTO inventoryItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InventoryItem : {}, {}", id, inventoryItemDTO);
        if (inventoryItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoryItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventoryItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inventoryItemDTO = inventoryItemService.update(inventoryItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryItemDTO.getId().toString()))
            .body(inventoryItemDTO);
    }

    /**
     * {@code PATCH  /inventory-items/:id} : Partial updates given fields of an existing inventoryItem, field will ignore if it is null
     *
     * @param id the id of the inventoryItemDTO to save.
     * @param inventoryItemDTO the inventoryItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventoryItemDTO,
     * or with status {@code 400 (Bad Request)} if the inventoryItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inventoryItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventoryItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InventoryItemDTO> partialUpdateInventoryItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InventoryItemDTO inventoryItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InventoryItem partially : {}, {}", id, inventoryItemDTO);
        if (inventoryItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventoryItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inventoryItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InventoryItemDTO> result = inventoryItemService.partialUpdate(inventoryItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inventoryItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /inventory-items} : get all the inventoryItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventoryItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InventoryItemDTO>> getAllInventoryItems(
        InventoryItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InventoryItems by criteria: {}", criteria);

        Page<InventoryItemDTO> page = inventoryItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /inventory-items/count} : count all the inventoryItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInventoryItems(InventoryItemCriteria criteria) {
        LOG.debug("REST request to count InventoryItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(inventoryItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /inventory-items/:id} : get the "id" inventoryItem.
     *
     * @param id the id of the inventoryItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventoryItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDTO> getInventoryItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InventoryItem : {}", id);
        Optional<InventoryItemDTO> inventoryItemDTO = inventoryItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventoryItemDTO);
    }

    /**
     * {@code DELETE  /inventory-items/:id} : delete the "id" inventoryItem.
     *
     * @param id the id of the inventoryItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InventoryItem : {}", id);
        inventoryItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
