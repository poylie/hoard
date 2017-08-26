package com.hoard.app.web.rest;

import com.hoard.app.HoardApp;

import com.hoard.app.domain.PurchaseOrderItem;
import com.hoard.app.repository.PurchaseOrderItemRepository;
import com.hoard.app.repository.search.PurchaseOrderItemSearchRepository;
import com.hoard.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PurchaseOrderItemResource REST controller.
 *
 * @see PurchaseOrderItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HoardApp.class)
public class PurchaseOrderItemResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_TOTAL_COST = 1;
    private static final Integer UPDATED_TOTAL_COST = 2;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private PurchaseOrderItemSearchRepository purchaseOrderItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPurchaseOrderItemMockMvc;

    private PurchaseOrderItem purchaseOrderItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PurchaseOrderItemResource purchaseOrderItemResource = new PurchaseOrderItemResource(purchaseOrderItemRepository, purchaseOrderItemSearchRepository);
        this.restPurchaseOrderItemMockMvc = MockMvcBuilders.standaloneSetup(purchaseOrderItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderItem createEntity(EntityManager em) {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem()
            .quantity(DEFAULT_QUANTITY)
            .totalCost(DEFAULT_TOTAL_COST);
        return purchaseOrderItem;
    }

    @Before
    public void initTest() {
        purchaseOrderItemSearchRepository.deleteAll();
        purchaseOrderItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseOrderItem() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderItemRepository.findAll().size();

        // Create the PurchaseOrderItem
        restPurchaseOrderItemMockMvc.perform(post("/api/purchase-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItem)))
            .andExpect(status().isCreated());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseOrderItem testPurchaseOrderItem = purchaseOrderItemList.get(purchaseOrderItemList.size() - 1);
        assertThat(testPurchaseOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPurchaseOrderItem.getTotalCost()).isEqualTo(DEFAULT_TOTAL_COST);

        // Validate the PurchaseOrderItem in Elasticsearch
        PurchaseOrderItem purchaseOrderItemEs = purchaseOrderItemSearchRepository.findOne(testPurchaseOrderItem.getId());
        assertThat(purchaseOrderItemEs).isEqualToComparingFieldByField(testPurchaseOrderItem);
    }

    @Test
    @Transactional
    public void createPurchaseOrderItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseOrderItemRepository.findAll().size();

        // Create the PurchaseOrderItem with an existing ID
        purchaseOrderItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderItemMockMvc.perform(post("/api/purchase-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItem)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseOrderItemRepository.findAll().size();
        // set the field null
        purchaseOrderItem.setQuantity(null);

        // Create the PurchaseOrderItem, which fails.

        restPurchaseOrderItemMockMvc.perform(post("/api/purchase-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItem)))
            .andExpect(status().isBadRequest());

        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchaseOrderItems() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList
        restPurchaseOrderItemMockMvc.perform(get("/api/purchase-order-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(DEFAULT_TOTAL_COST)));
    }

    @Test
    @Transactional
    public void getPurchaseOrderItem() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get the purchaseOrderItem
        restPurchaseOrderItemMockMvc.perform(get("/api/purchase-order-items/{id}", purchaseOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.totalCost").value(DEFAULT_TOTAL_COST));
    }

    @Test
    @Transactional
    public void getNonExistingPurchaseOrderItem() throws Exception {
        // Get the purchaseOrderItem
        restPurchaseOrderItemMockMvc.perform(get("/api/purchase-order-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseOrderItem() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);
        purchaseOrderItemSearchRepository.save(purchaseOrderItem);
        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();

        // Update the purchaseOrderItem
        PurchaseOrderItem updatedPurchaseOrderItem = purchaseOrderItemRepository.findOne(purchaseOrderItem.getId());
        updatedPurchaseOrderItem
            .quantity(UPDATED_QUANTITY)
            .totalCost(UPDATED_TOTAL_COST);

        restPurchaseOrderItemMockMvc.perform(put("/api/purchase-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPurchaseOrderItem)))
            .andExpect(status().isOk());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseOrderItem testPurchaseOrderItem = purchaseOrderItemList.get(purchaseOrderItemList.size() - 1);
        assertThat(testPurchaseOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPurchaseOrderItem.getTotalCost()).isEqualTo(UPDATED_TOTAL_COST);

        // Validate the PurchaseOrderItem in Elasticsearch
        PurchaseOrderItem purchaseOrderItemEs = purchaseOrderItemSearchRepository.findOne(testPurchaseOrderItem.getId());
        assertThat(purchaseOrderItemEs).isEqualToComparingFieldByField(testPurchaseOrderItem);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseOrderItemRepository.findAll().size();

        // Create the PurchaseOrderItem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPurchaseOrderItemMockMvc.perform(put("/api/purchase-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseOrderItem)))
            .andExpect(status().isCreated());

        // Validate the PurchaseOrderItem in the database
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePurchaseOrderItem() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);
        purchaseOrderItemSearchRepository.save(purchaseOrderItem);
        int databaseSizeBeforeDelete = purchaseOrderItemRepository.findAll().size();

        // Get the purchaseOrderItem
        restPurchaseOrderItemMockMvc.perform(delete("/api/purchase-order-items/{id}", purchaseOrderItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean purchaseOrderItemExistsInEs = purchaseOrderItemSearchRepository.exists(purchaseOrderItem.getId());
        assertThat(purchaseOrderItemExistsInEs).isFalse();

        // Validate the database is empty
        List<PurchaseOrderItem> purchaseOrderItemList = purchaseOrderItemRepository.findAll();
        assertThat(purchaseOrderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPurchaseOrderItem() throws Exception {
        // Initialize the database
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);
        purchaseOrderItemSearchRepository.save(purchaseOrderItem);

        // Search the purchaseOrderItem
        restPurchaseOrderItemMockMvc.perform(get("/api/_search/purchase-order-items?query=id:" + purchaseOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(DEFAULT_TOTAL_COST)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrderItem.class);
        PurchaseOrderItem purchaseOrderItem1 = new PurchaseOrderItem();
        purchaseOrderItem1.setId(1L);
        PurchaseOrderItem purchaseOrderItem2 = new PurchaseOrderItem();
        purchaseOrderItem2.setId(purchaseOrderItem1.getId());
        assertThat(purchaseOrderItem1).isEqualTo(purchaseOrderItem2);
        purchaseOrderItem2.setId(2L);
        assertThat(purchaseOrderItem1).isNotEqualTo(purchaseOrderItem2);
        purchaseOrderItem1.setId(null);
        assertThat(purchaseOrderItem1).isNotEqualTo(purchaseOrderItem2);
    }
}
