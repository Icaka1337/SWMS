package com.uni.swms.web.rest;

import com.uni.swms.repository.AIInsightRepository;
import com.uni.swms.service.AIInsightQueryService;
import com.uni.swms.service.AIInsightService;
import com.uni.swms.service.criteria.AIInsightCriteria;
import com.uni.swms.service.dto.AIInsightDTO;
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
 * REST controller for managing {@link com.uni.swms.domain.AIInsight}.
 */
@RestController
@RequestMapping("/api/ai-insights")
public class AIInsightResource {

    private static final Logger LOG = LoggerFactory.getLogger(AIInsightResource.class);

    private static final String ENTITY_NAME = "aIInsight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AIInsightService aIInsightService;

    private final AIInsightRepository aIInsightRepository;

    private final AIInsightQueryService aIInsightQueryService;

    public AIInsightResource(
        AIInsightService aIInsightService,
        AIInsightRepository aIInsightRepository,
        AIInsightQueryService aIInsightQueryService
    ) {
        this.aIInsightService = aIInsightService;
        this.aIInsightRepository = aIInsightRepository;
        this.aIInsightQueryService = aIInsightQueryService;
    }

    /**
     * {@code POST  /ai-insights} : Create a new aIInsight.
     *
     * @param aIInsightDTO the aIInsightDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aIInsightDTO, or with status {@code 400 (Bad Request)} if the aIInsight has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AIInsightDTO> createAIInsight(@Valid @RequestBody AIInsightDTO aIInsightDTO) throws URISyntaxException {
        LOG.debug("REST request to save AIInsight : {}", aIInsightDTO);
        if (aIInsightDTO.getId() != null) {
            throw new BadRequestAlertException("A new aIInsight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aIInsightDTO = aIInsightService.save(aIInsightDTO);
        return ResponseEntity.created(new URI("/api/ai-insights/" + aIInsightDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aIInsightDTO.getId().toString()))
            .body(aIInsightDTO);
    }

    /**
     * {@code PUT  /ai-insights/:id} : Updates an existing aIInsight.
     *
     * @param id the id of the aIInsightDTO to save.
     * @param aIInsightDTO the aIInsightDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIInsightDTO,
     * or with status {@code 400 (Bad Request)} if the aIInsightDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aIInsightDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AIInsightDTO> updateAIInsight(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AIInsightDTO aIInsightDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AIInsight : {}, {}", id, aIInsightDTO);
        if (aIInsightDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aIInsightDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aIInsightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aIInsightDTO = aIInsightService.update(aIInsightDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aIInsightDTO.getId().toString()))
            .body(aIInsightDTO);
    }

    /**
     * {@code PATCH  /ai-insights/:id} : Partial updates given fields of an existing aIInsight, field will ignore if it is null
     *
     * @param id the id of the aIInsightDTO to save.
     * @param aIInsightDTO the aIInsightDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aIInsightDTO,
     * or with status {@code 400 (Bad Request)} if the aIInsightDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aIInsightDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aIInsightDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AIInsightDTO> partialUpdateAIInsight(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AIInsightDTO aIInsightDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AIInsight partially : {}, {}", id, aIInsightDTO);
        if (aIInsightDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aIInsightDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aIInsightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AIInsightDTO> result = aIInsightService.partialUpdate(aIInsightDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aIInsightDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ai-insights} : get all the aIInsights.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aIInsights in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AIInsightDTO>> getAllAIInsights(
        AIInsightCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AIInsights by criteria: {}", criteria);

        Page<AIInsightDTO> page = aIInsightQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ai-insights/count} : count all the aIInsights.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAIInsights(AIInsightCriteria criteria) {
        LOG.debug("REST request to count AIInsights by criteria: {}", criteria);
        return ResponseEntity.ok().body(aIInsightQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ai-insights/:id} : get the "id" aIInsight.
     *
     * @param id the id of the aIInsightDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aIInsightDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AIInsightDTO> getAIInsight(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AIInsight : {}", id);
        Optional<AIInsightDTO> aIInsightDTO = aIInsightService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aIInsightDTO);
    }

    /**
     * {@code DELETE  /ai-insights/:id} : delete the "id" aIInsight.
     *
     * @param id the id of the aIInsightDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAIInsight(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AIInsight : {}", id);
        aIInsightService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
