package com.uni.swms.web.rest;

import static com.uni.swms.domain.WarehouseLocationAsserts.*;
import static com.uni.swms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.swms.IntegrationTest;
import com.uni.swms.domain.WarehouseLocation;
import com.uni.swms.repository.WarehouseLocationRepository;
import com.uni.swms.service.dto.WarehouseLocationDTO;
import com.uni.swms.service.mapper.WarehouseLocationMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WarehouseLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarehouseLocationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;
    private static final Integer SMALLER_CAPACITY = 1 - 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/warehouse-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WarehouseLocationRepository warehouseLocationRepository;

    @Autowired
    private WarehouseLocationMapper warehouseLocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarehouseLocationMockMvc;

    private WarehouseLocation warehouseLocation;

    private WarehouseLocation insertedWarehouseLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarehouseLocation createEntity() {
        return new WarehouseLocation()
            .name(DEFAULT_NAME)
            .section(DEFAULT_SECTION)
            .capacity(DEFAULT_CAPACITY)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarehouseLocation createUpdatedEntity() {
        return new WarehouseLocation()
            .name(UPDATED_NAME)
            .section(UPDATED_SECTION)
            .capacity(UPDATED_CAPACITY)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        warehouseLocation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWarehouseLocation != null) {
            warehouseLocationRepository.delete(insertedWarehouseLocation);
            insertedWarehouseLocation = null;
        }
    }

    @Test
    @Transactional
    void createWarehouseLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WarehouseLocation
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(warehouseLocation);
        var returnedWarehouseLocationDTO = om.readValue(
            restWarehouseLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warehouseLocationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WarehouseLocationDTO.class
        );

        // Validate the WarehouseLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWarehouseLocation = warehouseLocationMapper.toEntity(returnedWarehouseLocationDTO);
        assertWarehouseLocationUpdatableFieldsEquals(returnedWarehouseLocation, getPersistedWarehouseLocation(returnedWarehouseLocation));

        insertedWarehouseLocation = returnedWarehouseLocation;
    }

    @Test
    @Transactional
    void createWarehouseLocationWithExistingId() throws Exception {
        // Create the WarehouseLocation with an existing ID
        warehouseLocation.setId(1L);
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(warehouseLocation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarehouseLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warehouseLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WarehouseLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        warehouseLocation.setName(null);

        // Create the WarehouseLocation, which fails.
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(warehouseLocation);

        restWarehouseLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warehouseLocationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWarehouseLocations() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList
        restWarehouseLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouseLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getWarehouseLocation() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get the warehouseLocation
        restWarehouseLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, warehouseLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warehouseLocation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getWarehouseLocationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        Long id = warehouseLocation.getId();

        defaultWarehouseLocationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWarehouseLocationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWarehouseLocationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where name equals to
        defaultWarehouseLocationFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where name in
        defaultWarehouseLocationFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where name is not null
        defaultWarehouseLocationFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where name contains
        defaultWarehouseLocationFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where name does not contain
        defaultWarehouseLocationFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where section equals to
        defaultWarehouseLocationFiltering("section.equals=" + DEFAULT_SECTION, "section.equals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsBySectionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where section in
        defaultWarehouseLocationFiltering("section.in=" + DEFAULT_SECTION + "," + UPDATED_SECTION, "section.in=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsBySectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where section is not null
        defaultWarehouseLocationFiltering("section.specified=true", "section.specified=false");
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsBySectionContainsSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where section contains
        defaultWarehouseLocationFiltering("section.contains=" + DEFAULT_SECTION, "section.contains=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsBySectionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where section does not contain
        defaultWarehouseLocationFiltering("section.doesNotContain=" + UPDATED_SECTION, "section.doesNotContain=" + DEFAULT_SECTION);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByCapacityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where capacity equals to
        defaultWarehouseLocationFiltering("capacity.equals=" + DEFAULT_CAPACITY, "capacity.equals=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByCapacityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where capacity in
        defaultWarehouseLocationFiltering("capacity.in=" + DEFAULT_CAPACITY + "," + UPDATED_CAPACITY, "capacity.in=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByCapacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where capacity is not null
        defaultWarehouseLocationFiltering("capacity.specified=true", "capacity.specified=false");
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByCapacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where capacity is greater than or equal to
        defaultWarehouseLocationFiltering(
            "capacity.greaterThanOrEqual=" + DEFAULT_CAPACITY,
            "capacity.greaterThanOrEqual=" + UPDATED_CAPACITY
        );
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByCapacityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where capacity is less than or equal to
        defaultWarehouseLocationFiltering("capacity.lessThanOrEqual=" + DEFAULT_CAPACITY, "capacity.lessThanOrEqual=" + SMALLER_CAPACITY);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByCapacityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where capacity is less than
        defaultWarehouseLocationFiltering("capacity.lessThan=" + UPDATED_CAPACITY, "capacity.lessThan=" + DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByCapacityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where capacity is greater than
        defaultWarehouseLocationFiltering("capacity.greaterThan=" + SMALLER_CAPACITY, "capacity.greaterThan=" + DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where description equals to
        defaultWarehouseLocationFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where description in
        defaultWarehouseLocationFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where description is not null
        defaultWarehouseLocationFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where description contains
        defaultWarehouseLocationFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllWarehouseLocationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        // Get all the warehouseLocationList where description does not contain
        defaultWarehouseLocationFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    private void defaultWarehouseLocationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWarehouseLocationShouldBeFound(shouldBeFound);
        defaultWarehouseLocationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWarehouseLocationShouldBeFound(String filter) throws Exception {
        restWarehouseLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warehouseLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restWarehouseLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWarehouseLocationShouldNotBeFound(String filter) throws Exception {
        restWarehouseLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWarehouseLocationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWarehouseLocation() throws Exception {
        // Get the warehouseLocation
        restWarehouseLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWarehouseLocation() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warehouseLocation
        WarehouseLocation updatedWarehouseLocation = warehouseLocationRepository.findById(warehouseLocation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWarehouseLocation are not directly saved in db
        em.detach(updatedWarehouseLocation);
        updatedWarehouseLocation.name(UPDATED_NAME).section(UPDATED_SECTION).capacity(UPDATED_CAPACITY).description(UPDATED_DESCRIPTION);
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(updatedWarehouseLocation);

        restWarehouseLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warehouseLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warehouseLocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the WarehouseLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWarehouseLocationToMatchAllProperties(updatedWarehouseLocation);
    }

    @Test
    @Transactional
    void putNonExistingWarehouseLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouseLocation.setId(longCount.incrementAndGet());

        // Create the WarehouseLocation
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(warehouseLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warehouseLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warehouseLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarehouseLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarehouseLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouseLocation.setId(longCount.incrementAndGet());

        // Create the WarehouseLocation
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(warehouseLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warehouseLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarehouseLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarehouseLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouseLocation.setId(longCount.incrementAndGet());

        // Create the WarehouseLocation
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(warehouseLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warehouseLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarehouseLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarehouseLocationWithPatch() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warehouseLocation using partial update
        WarehouseLocation partialUpdatedWarehouseLocation = new WarehouseLocation();
        partialUpdatedWarehouseLocation.setId(warehouseLocation.getId());

        partialUpdatedWarehouseLocation.name(UPDATED_NAME).capacity(UPDATED_CAPACITY);

        restWarehouseLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouseLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWarehouseLocation))
            )
            .andExpect(status().isOk());

        // Validate the WarehouseLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarehouseLocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWarehouseLocation, warehouseLocation),
            getPersistedWarehouseLocation(warehouseLocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateWarehouseLocationWithPatch() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warehouseLocation using partial update
        WarehouseLocation partialUpdatedWarehouseLocation = new WarehouseLocation();
        partialUpdatedWarehouseLocation.setId(warehouseLocation.getId());

        partialUpdatedWarehouseLocation
            .name(UPDATED_NAME)
            .section(UPDATED_SECTION)
            .capacity(UPDATED_CAPACITY)
            .description(UPDATED_DESCRIPTION);

        restWarehouseLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarehouseLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWarehouseLocation))
            )
            .andExpect(status().isOk());

        // Validate the WarehouseLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarehouseLocationUpdatableFieldsEquals(
            partialUpdatedWarehouseLocation,
            getPersistedWarehouseLocation(partialUpdatedWarehouseLocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingWarehouseLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouseLocation.setId(longCount.incrementAndGet());

        // Create the WarehouseLocation
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(warehouseLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarehouseLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warehouseLocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(warehouseLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarehouseLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarehouseLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouseLocation.setId(longCount.incrementAndGet());

        // Create the WarehouseLocation
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(warehouseLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(warehouseLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarehouseLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarehouseLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        warehouseLocation.setId(longCount.incrementAndGet());

        // Create the WarehouseLocation
        WarehouseLocationDTO warehouseLocationDTO = warehouseLocationMapper.toDto(warehouseLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarehouseLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(warehouseLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarehouseLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarehouseLocation() throws Exception {
        // Initialize the database
        insertedWarehouseLocation = warehouseLocationRepository.saveAndFlush(warehouseLocation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the warehouseLocation
        restWarehouseLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, warehouseLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return warehouseLocationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected WarehouseLocation getPersistedWarehouseLocation(WarehouseLocation warehouseLocation) {
        return warehouseLocationRepository.findById(warehouseLocation.getId()).orElseThrow();
    }

    protected void assertPersistedWarehouseLocationToMatchAllProperties(WarehouseLocation expectedWarehouseLocation) {
        assertWarehouseLocationAllPropertiesEquals(expectedWarehouseLocation, getPersistedWarehouseLocation(expectedWarehouseLocation));
    }

    protected void assertPersistedWarehouseLocationToMatchUpdatableProperties(WarehouseLocation expectedWarehouseLocation) {
        assertWarehouseLocationAllUpdatablePropertiesEquals(
            expectedWarehouseLocation,
            getPersistedWarehouseLocation(expectedWarehouseLocation)
        );
    }
}
