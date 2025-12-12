package com.uni.swms.web.rest;

import static com.uni.swms.domain.AIInsightAsserts.*;
import static com.uni.swms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.swms.IntegrationTest;
import com.uni.swms.domain.AIInsight;
import com.uni.swms.domain.Product;
import com.uni.swms.repository.AIInsightRepository;
import com.uni.swms.service.dto.AIInsightDTO;
import com.uni.swms.service.mapper.AIInsightMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AIInsightResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AIInsightResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 1D;
    private static final Double UPDATED_CONFIDENCE = 2D;
    private static final Double SMALLER_CONFIDENCE = 1D - 1D;

    private static final Instant DEFAULT_GENERATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GENERATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ai-insights";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AIInsightRepository aIInsightRepository;

    @Autowired
    private AIInsightMapper aIInsightMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAIInsightMockMvc;

    private AIInsight aIInsight;

    private AIInsight insertedAIInsight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AIInsight createEntity(EntityManager em) {
        AIInsight aIInsight = new AIInsight()
            .type(DEFAULT_TYPE)
            .message(DEFAULT_MESSAGE)
            .confidence(DEFAULT_CONFIDENCE)
            .generatedAt(DEFAULT_GENERATED_AT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        aIInsight.setProduct(product);
        return aIInsight;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AIInsight createUpdatedEntity(EntityManager em) {
        AIInsight updatedAIInsight = new AIInsight()
            .type(UPDATED_TYPE)
            .message(UPDATED_MESSAGE)
            .confidence(UPDATED_CONFIDENCE)
            .generatedAt(UPDATED_GENERATED_AT);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedAIInsight.setProduct(product);
        return updatedAIInsight;
    }

    @BeforeEach
    void initTest() {
        aIInsight = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAIInsight != null) {
            aIInsightRepository.delete(insertedAIInsight);
            insertedAIInsight = null;
        }
    }

    @Test
    @Transactional
    void createAIInsight() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AIInsight
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);
        var returnedAIInsightDTO = om.readValue(
            restAIInsightMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIInsightDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AIInsightDTO.class
        );

        // Validate the AIInsight in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAIInsight = aIInsightMapper.toEntity(returnedAIInsightDTO);
        assertAIInsightUpdatableFieldsEquals(returnedAIInsight, getPersistedAIInsight(returnedAIInsight));

        insertedAIInsight = returnedAIInsight;
    }

    @Test
    @Transactional
    void createAIInsightWithExistingId() throws Exception {
        // Create the AIInsight with an existing ID
        aIInsight.setId(1L);
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAIInsightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIInsightDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AIInsight in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIInsight.setType(null);

        // Create the AIInsight, which fails.
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        restAIInsightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIInsightDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIInsight.setMessage(null);

        // Create the AIInsight, which fails.
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        restAIInsightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIInsightDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGeneratedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aIInsight.setGeneratedAt(null);

        // Create the AIInsight, which fails.
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        restAIInsightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIInsightDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAIInsights() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList
        restAIInsightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aIInsight.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(DEFAULT_GENERATED_AT.toString())));
    }

    @Test
    @Transactional
    void getAIInsight() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get the aIInsight
        restAIInsightMockMvc
            .perform(get(ENTITY_API_URL_ID, aIInsight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aIInsight.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.generatedAt").value(DEFAULT_GENERATED_AT.toString()));
    }

    @Test
    @Transactional
    void getAIInsightsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        Long id = aIInsight.getId();

        defaultAIInsightFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAIInsightFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAIInsightFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAIInsightsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where type equals to
        defaultAIInsightFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where type in
        defaultAIInsightFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where type is not null
        defaultAIInsightFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllAIInsightsByTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where type contains
        defaultAIInsightFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where type does not contain
        defaultAIInsightFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where message equals to
        defaultAIInsightFiltering("message.equals=" + DEFAULT_MESSAGE, "message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where message in
        defaultAIInsightFiltering("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE, "message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where message is not null
        defaultAIInsightFiltering("message.specified=true", "message.specified=false");
    }

    @Test
    @Transactional
    void getAllAIInsightsByMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where message contains
        defaultAIInsightFiltering("message.contains=" + DEFAULT_MESSAGE, "message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where message does not contain
        defaultAIInsightFiltering("message.doesNotContain=" + UPDATED_MESSAGE, "message.doesNotContain=" + DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where confidence equals to
        defaultAIInsightFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where confidence in
        defaultAIInsightFiltering("confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE, "confidence.in=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where confidence is not null
        defaultAIInsightFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllAIInsightsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where confidence is greater than or equal to
        defaultAIInsightFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + UPDATED_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAIInsightsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where confidence is less than or equal to
        defaultAIInsightFiltering("confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE, "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where confidence is less than
        defaultAIInsightFiltering("confidence.lessThan=" + UPDATED_CONFIDENCE, "confidence.lessThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where confidence is greater than
        defaultAIInsightFiltering("confidence.greaterThan=" + SMALLER_CONFIDENCE, "confidence.greaterThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAIInsightsByGeneratedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where generatedAt equals to
        defaultAIInsightFiltering("generatedAt.equals=" + DEFAULT_GENERATED_AT, "generatedAt.equals=" + UPDATED_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllAIInsightsByGeneratedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where generatedAt in
        defaultAIInsightFiltering(
            "generatedAt.in=" + DEFAULT_GENERATED_AT + "," + UPDATED_GENERATED_AT,
            "generatedAt.in=" + UPDATED_GENERATED_AT
        );
    }

    @Test
    @Transactional
    void getAllAIInsightsByGeneratedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        // Get all the aIInsightList where generatedAt is not null
        defaultAIInsightFiltering("generatedAt.specified=true", "generatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllAIInsightsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            aIInsightRepository.saveAndFlush(aIInsight);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        aIInsight.setProduct(product);
        aIInsightRepository.saveAndFlush(aIInsight);
        Long productId = product.getId();
        // Get all the aIInsightList where product equals to productId
        defaultAIInsightShouldBeFound("productId.equals=" + productId);

        // Get all the aIInsightList where product equals to (productId + 1)
        defaultAIInsightShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultAIInsightFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAIInsightShouldBeFound(shouldBeFound);
        defaultAIInsightShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAIInsightShouldBeFound(String filter) throws Exception {
        restAIInsightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aIInsight.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(DEFAULT_GENERATED_AT.toString())));

        // Check, that the count call also returns 1
        restAIInsightMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAIInsightShouldNotBeFound(String filter) throws Exception {
        restAIInsightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAIInsightMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAIInsight() throws Exception {
        // Get the aIInsight
        restAIInsightMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAIInsight() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIInsight
        AIInsight updatedAIInsight = aIInsightRepository.findById(aIInsight.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAIInsight are not directly saved in db
        em.detach(updatedAIInsight);
        updatedAIInsight.type(UPDATED_TYPE).message(UPDATED_MESSAGE).confidence(UPDATED_CONFIDENCE).generatedAt(UPDATED_GENERATED_AT);
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(updatedAIInsight);

        restAIInsightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aIInsightDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aIInsightDTO))
            )
            .andExpect(status().isOk());

        // Validate the AIInsight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAIInsightToMatchAllProperties(updatedAIInsight);
    }

    @Test
    @Transactional
    void putNonExistingAIInsight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIInsight.setId(longCount.incrementAndGet());

        // Create the AIInsight
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAIInsightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aIInsightDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aIInsightDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIInsight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAIInsight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIInsight.setId(longCount.incrementAndGet());

        // Create the AIInsight
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIInsightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aIInsightDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIInsight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAIInsight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIInsight.setId(longCount.incrementAndGet());

        // Create the AIInsight
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIInsightMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIInsightDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AIInsight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAIInsightWithPatch() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIInsight using partial update
        AIInsight partialUpdatedAIInsight = new AIInsight();
        partialUpdatedAIInsight.setId(aIInsight.getId());

        partialUpdatedAIInsight.type(UPDATED_TYPE);

        restAIInsightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAIInsight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAIInsight))
            )
            .andExpect(status().isOk());

        // Validate the AIInsight in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAIInsightUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAIInsight, aIInsight),
            getPersistedAIInsight(aIInsight)
        );
    }

    @Test
    @Transactional
    void fullUpdateAIInsightWithPatch() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIInsight using partial update
        AIInsight partialUpdatedAIInsight = new AIInsight();
        partialUpdatedAIInsight.setId(aIInsight.getId());

        partialUpdatedAIInsight
            .type(UPDATED_TYPE)
            .message(UPDATED_MESSAGE)
            .confidence(UPDATED_CONFIDENCE)
            .generatedAt(UPDATED_GENERATED_AT);

        restAIInsightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAIInsight.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAIInsight))
            )
            .andExpect(status().isOk());

        // Validate the AIInsight in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAIInsightUpdatableFieldsEquals(partialUpdatedAIInsight, getPersistedAIInsight(partialUpdatedAIInsight));
    }

    @Test
    @Transactional
    void patchNonExistingAIInsight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIInsight.setId(longCount.incrementAndGet());

        // Create the AIInsight
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAIInsightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aIInsightDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aIInsightDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIInsight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAIInsight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIInsight.setId(longCount.incrementAndGet());

        // Create the AIInsight
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIInsightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aIInsightDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIInsight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAIInsight() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIInsight.setId(longCount.incrementAndGet());

        // Create the AIInsight
        AIInsightDTO aIInsightDTO = aIInsightMapper.toDto(aIInsight);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIInsightMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aIInsightDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AIInsight in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAIInsight() throws Exception {
        // Initialize the database
        insertedAIInsight = aIInsightRepository.saveAndFlush(aIInsight);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aIInsight
        restAIInsightMockMvc
            .perform(delete(ENTITY_API_URL_ID, aIInsight.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aIInsightRepository.count();
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

    protected AIInsight getPersistedAIInsight(AIInsight aIInsight) {
        return aIInsightRepository.findById(aIInsight.getId()).orElseThrow();
    }

    protected void assertPersistedAIInsightToMatchAllProperties(AIInsight expectedAIInsight) {
        assertAIInsightAllPropertiesEquals(expectedAIInsight, getPersistedAIInsight(expectedAIInsight));
    }

    protected void assertPersistedAIInsightToMatchUpdatableProperties(AIInsight expectedAIInsight) {
        assertAIInsightAllUpdatablePropertiesEquals(expectedAIInsight, getPersistedAIInsight(expectedAIInsight));
    }
}
