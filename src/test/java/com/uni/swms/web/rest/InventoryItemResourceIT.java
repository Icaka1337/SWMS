package com.uni.swms.web.rest;

import static com.uni.swms.domain.InventoryItemAsserts.*;
import static com.uni.swms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.swms.IntegrationTest;
import com.uni.swms.domain.InventoryItem;
import com.uni.swms.domain.Product;
import com.uni.swms.domain.WarehouseLocation;
import com.uni.swms.repository.InventoryItemRepository;
import com.uni.swms.service.dto.InventoryItemDTO;
import com.uni.swms.service.mapper.InventoryItemMapper;
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
 * Integration tests for the {@link InventoryItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventoryItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/inventory-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private InventoryItemMapper inventoryItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventoryItemMockMvc;

    private InventoryItem inventoryItem;

    private InventoryItem insertedInventoryItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItem createEntity(EntityManager em) {
        InventoryItem inventoryItem = new InventoryItem().quantity(DEFAULT_QUANTITY).lastUpdated(DEFAULT_LAST_UPDATED);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        inventoryItem.setProduct(product);
        // Add required entity
        WarehouseLocation warehouseLocation;
        if (TestUtil.findAll(em, WarehouseLocation.class).isEmpty()) {
            warehouseLocation = WarehouseLocationResourceIT.createEntity();
            em.persist(warehouseLocation);
            em.flush();
        } else {
            warehouseLocation = TestUtil.findAll(em, WarehouseLocation.class).get(0);
        }
        inventoryItem.setLocation(warehouseLocation);
        return inventoryItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InventoryItem createUpdatedEntity(EntityManager em) {
        InventoryItem updatedInventoryItem = new InventoryItem().quantity(UPDATED_QUANTITY).lastUpdated(UPDATED_LAST_UPDATED);
        // Add required entity
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createUpdatedEntity();
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        updatedInventoryItem.setProduct(product);
        // Add required entity
        WarehouseLocation warehouseLocation;
        if (TestUtil.findAll(em, WarehouseLocation.class).isEmpty()) {
            warehouseLocation = WarehouseLocationResourceIT.createUpdatedEntity();
            em.persist(warehouseLocation);
            em.flush();
        } else {
            warehouseLocation = TestUtil.findAll(em, WarehouseLocation.class).get(0);
        }
        updatedInventoryItem.setLocation(warehouseLocation);
        return updatedInventoryItem;
    }

    @BeforeEach
    void initTest() {
        inventoryItem = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInventoryItem != null) {
            inventoryItemRepository.delete(insertedInventoryItem);
            insertedInventoryItem = null;
        }
    }

    @Test
    @Transactional
    void createInventoryItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);
        var returnedInventoryItemDTO = om.readValue(
            restInventoryItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventoryItemDTO.class
        );

        // Validate the InventoryItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInventoryItem = inventoryItemMapper.toEntity(returnedInventoryItemDTO);
        assertInventoryItemUpdatableFieldsEquals(returnedInventoryItem, getPersistedInventoryItem(returnedInventoryItem));

        insertedInventoryItem = returnedInventoryItem;
    }

    @Test
    @Transactional
    void createInventoryItemWithExistingId() throws Exception {
        // Create the InventoryItem with an existing ID
        inventoryItem.setId(1L);
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventoryItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventoryItem.setQuantity(null);

        // Create the InventoryItem, which fails.
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        restInventoryItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInventoryItems() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get the inventoryItem
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL_ID, inventoryItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventoryItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getInventoryItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        Long id = inventoryItem.getId();

        defaultInventoryItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInventoryItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInventoryItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity equals to
        defaultInventoryItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity in
        defaultInventoryItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is not null
        defaultInventoryItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is greater than or equal to
        defaultInventoryItemFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is less than or equal to
        defaultInventoryItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is less than
        defaultInventoryItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where quantity is greater than
        defaultInventoryItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLastUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where lastUpdated equals to
        defaultInventoryItemFiltering("lastUpdated.equals=" + DEFAULT_LAST_UPDATED, "lastUpdated.equals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLastUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where lastUpdated in
        defaultInventoryItemFiltering(
            "lastUpdated.in=" + DEFAULT_LAST_UPDATED + "," + UPDATED_LAST_UPDATED,
            "lastUpdated.in=" + UPDATED_LAST_UPDATED
        );
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLastUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        // Get all the inventoryItemList where lastUpdated is not null
        defaultInventoryItemFiltering("lastUpdated.specified=true", "lastUpdated.specified=false");
    }

    @Test
    @Transactional
    void getAllInventoryItemsByProductIsEqualToSomething() throws Exception {
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            product = ProductResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        inventoryItem.setProduct(product);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long productId = product.getId();
        // Get all the inventoryItemList where product equals to productId
        defaultInventoryItemShouldBeFound("productId.equals=" + productId);

        // Get all the inventoryItemList where product equals to (productId + 1)
        defaultInventoryItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllInventoryItemsByLocationIsEqualToSomething() throws Exception {
        WarehouseLocation location;
        if (TestUtil.findAll(em, WarehouseLocation.class).isEmpty()) {
            inventoryItemRepository.saveAndFlush(inventoryItem);
            location = WarehouseLocationResourceIT.createEntity();
        } else {
            location = TestUtil.findAll(em, WarehouseLocation.class).get(0);
        }
        em.persist(location);
        em.flush();
        inventoryItem.setLocation(location);
        inventoryItemRepository.saveAndFlush(inventoryItem);
        Long locationId = location.getId();
        // Get all the inventoryItemList where location equals to locationId
        defaultInventoryItemShouldBeFound("locationId.equals=" + locationId);

        // Get all the inventoryItemList where location equals to (locationId + 1)
        defaultInventoryItemShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }

    private void defaultInventoryItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInventoryItemShouldBeFound(shouldBeFound);
        defaultInventoryItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInventoryItemShouldBeFound(String filter) throws Exception {
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventoryItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));

        // Check, that the count call also returns 1
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInventoryItemShouldNotBeFound(String filter) throws Exception {
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInventoryItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInventoryItem() throws Exception {
        // Get the inventoryItem
        restInventoryItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItem
        InventoryItem updatedInventoryItem = inventoryItemRepository.findById(inventoryItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventoryItem are not directly saved in db
        em.detach(updatedInventoryItem);
        updatedInventoryItem.quantity(UPDATED_QUANTITY).lastUpdated(UPDATED_LAST_UPDATED);
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(updatedInventoryItem);

        restInventoryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventoryItemToMatchAllProperties(updatedInventoryItem);
    }

    @Test
    @Transactional
    void putNonExistingInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventoryItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventoryItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventoryItemWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItem using partial update
        InventoryItem partialUpdatedInventoryItem = new InventoryItem();
        partialUpdatedInventoryItem.setId(inventoryItem.getId());

        partialUpdatedInventoryItem.quantity(UPDATED_QUANTITY).lastUpdated(UPDATED_LAST_UPDATED);

        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItem))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventoryItem, inventoryItem),
            getPersistedInventoryItem(inventoryItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventoryItemWithPatch() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventoryItem using partial update
        InventoryItem partialUpdatedInventoryItem = new InventoryItem();
        partialUpdatedInventoryItem.setId(inventoryItem.getId());

        partialUpdatedInventoryItem.quantity(UPDATED_QUANTITY).lastUpdated(UPDATED_LAST_UPDATED);

        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventoryItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventoryItem))
            )
            .andExpect(status().isOk());

        // Validate the InventoryItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventoryItemUpdatableFieldsEquals(partialUpdatedInventoryItem, getPersistedInventoryItem(partialUpdatedInventoryItem));
    }

    @Test
    @Transactional
    void patchNonExistingInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventoryItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventoryItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventoryItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventoryItem.setId(longCount.incrementAndGet());

        // Create the InventoryItem
        InventoryItemDTO inventoryItemDTO = inventoryItemMapper.toDto(inventoryItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventoryItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventoryItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InventoryItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventoryItem() throws Exception {
        // Initialize the database
        insertedInventoryItem = inventoryItemRepository.saveAndFlush(inventoryItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventoryItem
        restInventoryItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventoryItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventoryItemRepository.count();
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

    protected InventoryItem getPersistedInventoryItem(InventoryItem inventoryItem) {
        return inventoryItemRepository.findById(inventoryItem.getId()).orElseThrow();
    }

    protected void assertPersistedInventoryItemToMatchAllProperties(InventoryItem expectedInventoryItem) {
        assertInventoryItemAllPropertiesEquals(expectedInventoryItem, getPersistedInventoryItem(expectedInventoryItem));
    }

    protected void assertPersistedInventoryItemToMatchUpdatableProperties(InventoryItem expectedInventoryItem) {
        assertInventoryItemAllUpdatablePropertiesEquals(expectedInventoryItem, getPersistedInventoryItem(expectedInventoryItem));
    }
}
