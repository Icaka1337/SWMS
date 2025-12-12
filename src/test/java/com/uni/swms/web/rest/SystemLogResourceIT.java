package com.uni.swms.web.rest;

import static com.uni.swms.domain.SystemLogAsserts.*;
import static com.uni.swms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.swms.IntegrationTest;
import com.uni.swms.domain.SystemLog;
import com.uni.swms.repository.SystemLogRepository;
import com.uni.swms.service.dto.SystemLogDTO;
import com.uni.swms.service.mapper.SystemLogMapper;
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
 * Integration tests for the {@link SystemLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemLogResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/system-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Autowired
    private SystemLogMapper systemLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemLogMockMvc;

    private SystemLog systemLog;

    private SystemLog insertedSystemLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemLog createEntity() {
        return new SystemLog()
            .username(DEFAULT_USERNAME)
            .action(DEFAULT_ACTION)
            .entityName(DEFAULT_ENTITY_NAME)
            .details(DEFAULT_DETAILS)
            .timestamp(DEFAULT_TIMESTAMP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemLog createUpdatedEntity() {
        return new SystemLog()
            .username(UPDATED_USERNAME)
            .action(UPDATED_ACTION)
            .entityName(UPDATED_ENTITY_NAME)
            .details(UPDATED_DETAILS)
            .timestamp(UPDATED_TIMESTAMP);
    }

    @BeforeEach
    void initTest() {
        systemLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSystemLog != null) {
            systemLogRepository.delete(insertedSystemLog);
            insertedSystemLog = null;
        }
    }

    @Test
    @Transactional
    void createSystemLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemLog
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(systemLog);
        var returnedSystemLogDTO = om.readValue(
            restSystemLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemLogDTO.class
        );

        // Validate the SystemLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSystemLog = systemLogMapper.toEntity(returnedSystemLogDTO);
        assertSystemLogUpdatableFieldsEquals(returnedSystemLog, getPersistedSystemLog(returnedSystemLog));

        insertedSystemLog = returnedSystemLog;
    }

    @Test
    @Transactional
    void createSystemLogWithExistingId() throws Exception {
        // Create the SystemLog with an existing ID
        systemLog.setId(1L);
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(systemLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemLog.setTimestamp(null);

        // Create the SystemLog, which fails.
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(systemLog);

        restSystemLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemLogs() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList
        restSystemLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getSystemLog() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get the systemLog
        restSystemLogMockMvc
            .perform(get(ENTITY_API_URL_ID, systemLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemLog.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getSystemLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        Long id = systemLog.getId();

        defaultSystemLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSystemLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSystemLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSystemLogsByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where username equals to
        defaultSystemLogFiltering("username.equals=" + DEFAULT_USERNAME, "username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSystemLogsByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where username in
        defaultSystemLogFiltering("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME, "username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSystemLogsByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where username is not null
        defaultSystemLogFiltering("username.specified=true", "username.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemLogsByUsernameContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where username contains
        defaultSystemLogFiltering("username.contains=" + DEFAULT_USERNAME, "username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllSystemLogsByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where username does not contain
        defaultSystemLogFiltering("username.doesNotContain=" + UPDATED_USERNAME, "username.doesNotContain=" + DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void getAllSystemLogsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where action equals to
        defaultSystemLogFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllSystemLogsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where action in
        defaultSystemLogFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllSystemLogsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where action is not null
        defaultSystemLogFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemLogsByActionContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where action contains
        defaultSystemLogFiltering("action.contains=" + DEFAULT_ACTION, "action.contains=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllSystemLogsByActionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where action does not contain
        defaultSystemLogFiltering("action.doesNotContain=" + UPDATED_ACTION, "action.doesNotContain=" + DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void getAllSystemLogsByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where entityName equals to
        defaultSystemLogFiltering("entityName.equals=" + DEFAULT_ENTITY_NAME, "entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllSystemLogsByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where entityName in
        defaultSystemLogFiltering(
            "entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME,
            "entityName.in=" + UPDATED_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllSystemLogsByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where entityName is not null
        defaultSystemLogFiltering("entityName.specified=true", "entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemLogsByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where entityName contains
        defaultSystemLogFiltering("entityName.contains=" + DEFAULT_ENTITY_NAME, "entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllSystemLogsByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where entityName does not contain
        defaultSystemLogFiltering("entityName.doesNotContain=" + UPDATED_ENTITY_NAME, "entityName.doesNotContain=" + DEFAULT_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllSystemLogsByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where details equals to
        defaultSystemLogFiltering("details.equals=" + DEFAULT_DETAILS, "details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllSystemLogsByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where details in
        defaultSystemLogFiltering("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS, "details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllSystemLogsByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where details is not null
        defaultSystemLogFiltering("details.specified=true", "details.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemLogsByDetailsContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where details contains
        defaultSystemLogFiltering("details.contains=" + DEFAULT_DETAILS, "details.contains=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllSystemLogsByDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where details does not contain
        defaultSystemLogFiltering("details.doesNotContain=" + UPDATED_DETAILS, "details.doesNotContain=" + DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    void getAllSystemLogsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where timestamp equals to
        defaultSystemLogFiltering("timestamp.equals=" + DEFAULT_TIMESTAMP, "timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllSystemLogsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where timestamp in
        defaultSystemLogFiltering("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP, "timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllSystemLogsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        // Get all the systemLogList where timestamp is not null
        defaultSystemLogFiltering("timestamp.specified=true", "timestamp.specified=false");
    }

    private void defaultSystemLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSystemLogShouldBeFound(shouldBeFound);
        defaultSystemLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemLogShouldBeFound(String filter) throws Exception {
        restSystemLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));

        // Check, that the count call also returns 1
        restSystemLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemLogShouldNotBeFound(String filter) throws Exception {
        restSystemLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSystemLog() throws Exception {
        // Get the systemLog
        restSystemLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemLog() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemLog
        SystemLog updatedSystemLog = systemLogRepository.findById(systemLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemLog are not directly saved in db
        em.detach(updatedSystemLog);
        updatedSystemLog
            .username(UPDATED_USERNAME)
            .action(UPDATED_ACTION)
            .entityName(UPDATED_ENTITY_NAME)
            .details(UPDATED_DETAILS)
            .timestamp(UPDATED_TIMESTAMP);
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(updatedSystemLog);

        restSystemLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemLogToMatchAllProperties(updatedSystemLog);
    }

    @Test
    @Transactional
    void putNonExistingSystemLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemLog.setId(longCount.incrementAndGet());

        // Create the SystemLog
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(systemLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemLog.setId(longCount.incrementAndGet());

        // Create the SystemLog
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(systemLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemLog.setId(longCount.incrementAndGet());

        // Create the SystemLog
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(systemLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemLogWithPatch() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemLog using partial update
        SystemLog partialUpdatedSystemLog = new SystemLog();
        partialUpdatedSystemLog.setId(systemLog.getId());

        partialUpdatedSystemLog.action(UPDATED_ACTION).entityName(UPDATED_ENTITY_NAME);

        restSystemLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemLog))
            )
            .andExpect(status().isOk());

        // Validate the SystemLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemLog, systemLog),
            getPersistedSystemLog(systemLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemLogWithPatch() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemLog using partial update
        SystemLog partialUpdatedSystemLog = new SystemLog();
        partialUpdatedSystemLog.setId(systemLog.getId());

        partialUpdatedSystemLog
            .username(UPDATED_USERNAME)
            .action(UPDATED_ACTION)
            .entityName(UPDATED_ENTITY_NAME)
            .details(UPDATED_DETAILS)
            .timestamp(UPDATED_TIMESTAMP);

        restSystemLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemLog))
            )
            .andExpect(status().isOk());

        // Validate the SystemLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemLogUpdatableFieldsEquals(partialUpdatedSystemLog, getPersistedSystemLog(partialUpdatedSystemLog));
    }

    @Test
    @Transactional
    void patchNonExistingSystemLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemLog.setId(longCount.incrementAndGet());

        // Create the SystemLog
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(systemLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemLog.setId(longCount.incrementAndGet());

        // Create the SystemLog
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(systemLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemLog.setId(longCount.incrementAndGet());

        // Create the SystemLog
        SystemLogDTO systemLogDTO = systemLogMapper.toDto(systemLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemLog() throws Exception {
        // Initialize the database
        insertedSystemLog = systemLogRepository.saveAndFlush(systemLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemLog
        restSystemLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemLogRepository.count();
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

    protected SystemLog getPersistedSystemLog(SystemLog systemLog) {
        return systemLogRepository.findById(systemLog.getId()).orElseThrow();
    }

    protected void assertPersistedSystemLogToMatchAllProperties(SystemLog expectedSystemLog) {
        assertSystemLogAllPropertiesEquals(expectedSystemLog, getPersistedSystemLog(expectedSystemLog));
    }

    protected void assertPersistedSystemLogToMatchUpdatableProperties(SystemLog expectedSystemLog) {
        assertSystemLogAllUpdatablePropertiesEquals(expectedSystemLog, getPersistedSystemLog(expectedSystemLog));
    }
}
