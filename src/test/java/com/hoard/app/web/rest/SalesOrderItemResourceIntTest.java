package com.hoard.app.web.rest;

import com.hoard.app.HoardApp;

import com.hoard.app.domain.SalesOrderItem;
import com.hoard.app.repository.SalesOrderItemRepository;
import com.hoard.app.repository.search.SalesOrderItemSearchRepository;
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
 * Test class for the SalesOrderItemResource REST controller.
 *
 * @see SalesOrderItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HoardApp.class)
public class SalesOrderItemResourceIntTest {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_TOTAL_PRICE = 1;
    private static final Integer UPDATED_TOTAL_PRICE = 2;

    @Autowired
    private SalesOrderItemRepository salesOrderItemRepository;

    @Autowired
    private SalesOrderItemSearchRepository salesOrderItemSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalesOrderItemMockMvc;

    private SalesOrderItem salesOrderItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SalesOrderItemResource salesOrderItemResource = new SalesOrderItemResource(salesOrderItemRepository, salesOrderItemSearchRepository);
        this.restSalesOrderItemMockMvc = MockMvcBuilders.standaloneSetup(salesOrderItemResource)
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
    public static SalesOrderItem createEntity(EntityManager em) {
        SalesOrderItem salesOrderItem = new SalesOrderItem()
            .quantity(DEFAULT_QUANTITY)
            .totalPrice(DEFAULT_TOTAL_PRICE);
        return salesOrderItem;
    }

    @Before
    public void initTest() {
        salesOrderItemSearchRepository.deleteAll();
        salesOrderItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesOrderItem() throws Exception {
        int databaseSizeBeforeCreate = salesOrderItemRepository.findAll().size();

        // Create the SalesOrderItem
        restSalesOrderItemMockMvc.perform(post("/api/sales-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrderItem)))
            .andExpect(status().isCreated());

        // Validate the SalesOrderItem in the database
        List<SalesOrderItem> salesOrderItemList = salesOrderItemRepository.findAll();
        assertThat(salesOrderItemList).hasSize(databaseSizeBeforeCreate + 1);
        SalesOrderItem testSalesOrderItem = salesOrderItemList.get(salesOrderItemList.size() - 1);
        assertThat(testSalesOrderItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testSalesOrderItem.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);

        // Validate the SalesOrderItem in Elasticsearch
        SalesOrderItem salesOrderItemEs = salesOrderItemSearchRepository.findOne(testSalesOrderItem.getId());
        assertThat(salesOrderItemEs).isEqualToComparingFieldByField(testSalesOrderItem);
    }

    @Test
    @Transactional
    public void createSalesOrderItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesOrderItemRepository.findAll().size();

        // Create the SalesOrderItem with an existing ID
        salesOrderItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesOrderItemMockMvc.perform(post("/api/sales-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrderItem)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SalesOrderItem> salesOrderItemList = salesOrderItemRepository.findAll();
        assertThat(salesOrderItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesOrderItemRepository.findAll().size();
        // set the field null
        salesOrderItem.setQuantity(null);

        // Create the SalesOrderItem, which fails.

        restSalesOrderItemMockMvc.perform(post("/api/sales-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrderItem)))
            .andExpect(status().isBadRequest());

        List<SalesOrderItem> salesOrderItemList = salesOrderItemRepository.findAll();
        assertThat(salesOrderItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSalesOrderItems() throws Exception {
        // Initialize the database
        salesOrderItemRepository.saveAndFlush(salesOrderItem);

        // Get all the salesOrderItemList
        restSalesOrderItemMockMvc.perform(get("/api/sales-order-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE)));
    }

    @Test
    @Transactional
    public void getSalesOrderItem() throws Exception {
        // Initialize the database
        salesOrderItemRepository.saveAndFlush(salesOrderItem);

        // Get the salesOrderItem
        restSalesOrderItemMockMvc.perform(get("/api/sales-order-items/{id}", salesOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salesOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE));
    }

    @Test
    @Transactional
    public void getNonExistingSalesOrderItem() throws Exception {
        // Get the salesOrderItem
        restSalesOrderItemMockMvc.perform(get("/api/sales-order-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesOrderItem() throws Exception {
        // Initialize the database
        salesOrderItemRepository.saveAndFlush(salesOrderItem);
        salesOrderItemSearchRepository.save(salesOrderItem);
        int databaseSizeBeforeUpdate = salesOrderItemRepository.findAll().size();

        // Update the salesOrderItem
        SalesOrderItem updatedSalesOrderItem = salesOrderItemRepository.findOne(salesOrderItem.getId());
        updatedSalesOrderItem
            .quantity(UPDATED_QUANTITY)
            .totalPrice(UPDATED_TOTAL_PRICE);

        restSalesOrderItemMockMvc.perform(put("/api/sales-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalesOrderItem)))
            .andExpect(status().isOk());

        // Validate the SalesOrderItem in the database
        List<SalesOrderItem> salesOrderItemList = salesOrderItemRepository.findAll();
        assertThat(salesOrderItemList).hasSize(databaseSizeBeforeUpdate);
        SalesOrderItem testSalesOrderItem = salesOrderItemList.get(salesOrderItemList.size() - 1);
        assertThat(testSalesOrderItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testSalesOrderItem.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);

        // Validate the SalesOrderItem in Elasticsearch
        SalesOrderItem salesOrderItemEs = salesOrderItemSearchRepository.findOne(testSalesOrderItem.getId());
        assertThat(salesOrderItemEs).isEqualToComparingFieldByField(testSalesOrderItem);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesOrderItem() throws Exception {
        int databaseSizeBeforeUpdate = salesOrderItemRepository.findAll().size();

        // Create the SalesOrderItem

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalesOrderItemMockMvc.perform(put("/api/sales-order-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrderItem)))
            .andExpect(status().isCreated());

        // Validate the SalesOrderItem in the database
        List<SalesOrderItem> salesOrderItemList = salesOrderItemRepository.findAll();
        assertThat(salesOrderItemList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSalesOrderItem() throws Exception {
        // Initialize the database
        salesOrderItemRepository.saveAndFlush(salesOrderItem);
        salesOrderItemSearchRepository.save(salesOrderItem);
        int databaseSizeBeforeDelete = salesOrderItemRepository.findAll().size();

        // Get the salesOrderItem
        restSalesOrderItemMockMvc.perform(delete("/api/sales-order-items/{id}", salesOrderItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean salesOrderItemExistsInEs = salesOrderItemSearchRepository.exists(salesOrderItem.getId());
        assertThat(salesOrderItemExistsInEs).isFalse();

        // Validate the database is empty
        List<SalesOrderItem> salesOrderItemList = salesOrderItemRepository.findAll();
        assertThat(salesOrderItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSalesOrderItem() throws Exception {
        // Initialize the database
        salesOrderItemRepository.saveAndFlush(salesOrderItem);
        salesOrderItemSearchRepository.save(salesOrderItem);

        // Search the salesOrderItem
        restSalesOrderItemMockMvc.perform(get("/api/_search/sales-order-items?query=id:" + salesOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesOrderItem.class);
        SalesOrderItem salesOrderItem1 = new SalesOrderItem();
        salesOrderItem1.setId(1L);
        SalesOrderItem salesOrderItem2 = new SalesOrderItem();
        salesOrderItem2.setId(salesOrderItem1.getId());
        assertThat(salesOrderItem1).isEqualTo(salesOrderItem2);
        salesOrderItem2.setId(2L);
        assertThat(salesOrderItem1).isNotEqualTo(salesOrderItem2);
        salesOrderItem1.setId(null);
        assertThat(salesOrderItem1).isNotEqualTo(salesOrderItem2);
    }
}
