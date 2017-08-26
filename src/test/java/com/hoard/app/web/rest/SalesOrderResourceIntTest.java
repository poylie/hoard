package com.hoard.app.web.rest;

import com.hoard.app.HoardApp;

import com.hoard.app.domain.SalesOrder;
import com.hoard.app.repository.SalesOrderRepository;
import com.hoard.app.repository.search.SalesOrderSearchRepository;
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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.hoard.app.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hoard.app.domain.enumeration.Status;
/**
 * Test class for the SalesOrderResource REST controller.
 *
 * @see SalesOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HoardApp.class)
public class SalesOrderResourceIntTest {

    private static final String DEFAULT_REFERENCE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_LAST_UPDATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CUSTOMER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.APPROVED;
    private static final Status UPDATED_STATUS = Status.PENDING;

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private SalesOrderSearchRepository salesOrderSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalesOrderMockMvc;

    private SalesOrder salesOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SalesOrderResource salesOrderResource = new SalesOrderResource(salesOrderRepository, salesOrderSearchRepository);
        this.restSalesOrderMockMvc = MockMvcBuilders.standaloneSetup(salesOrderResource)
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
    public static SalesOrder createEntity(EntityManager em) {
        SalesOrder salesOrder = new SalesOrder()
            .referenceNumber(DEFAULT_REFERENCE_NUMBER)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED)
            .customer(DEFAULT_CUSTOMER)
            .status(DEFAULT_STATUS);
        return salesOrder;
    }

    @Before
    public void initTest() {
        salesOrderSearchRepository.deleteAll();
        salesOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesOrder() throws Exception {
        int databaseSizeBeforeCreate = salesOrderRepository.findAll().size();

        // Create the SalesOrder
        restSalesOrderMockMvc.perform(post("/api/sales-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrder)))
            .andExpect(status().isCreated());

        // Validate the SalesOrder in the database
        List<SalesOrder> salesOrderList = salesOrderRepository.findAll();
        assertThat(salesOrderList).hasSize(databaseSizeBeforeCreate + 1);
        SalesOrder testSalesOrder = salesOrderList.get(salesOrderList.size() - 1);
        assertThat(testSalesOrder.getReferenceNumber()).isEqualTo(DEFAULT_REFERENCE_NUMBER);
        assertThat(testSalesOrder.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
        assertThat(testSalesOrder.getLastUpdated()).isEqualTo(DEFAULT_LAST_UPDATED);
        assertThat(testSalesOrder.getCustomer()).isEqualTo(DEFAULT_CUSTOMER);
        assertThat(testSalesOrder.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the SalesOrder in Elasticsearch
        SalesOrder salesOrderEs = salesOrderSearchRepository.findOne(testSalesOrder.getId());
        assertThat(salesOrderEs).isEqualToComparingFieldByField(testSalesOrder);
    }

    @Test
    @Transactional
    public void createSalesOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesOrderRepository.findAll().size();

        // Create the SalesOrder with an existing ID
        salesOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesOrderMockMvc.perform(post("/api/sales-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrder)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SalesOrder> salesOrderList = salesOrderRepository.findAll();
        assertThat(salesOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkReferenceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesOrderRepository.findAll().size();
        // set the field null
        salesOrder.setReferenceNumber(null);

        // Create the SalesOrder, which fails.

        restSalesOrderMockMvc.perform(post("/api/sales-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrder)))
            .andExpect(status().isBadRequest());

        List<SalesOrder> salesOrderList = salesOrderRepository.findAll();
        assertThat(salesOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesOrderRepository.findAll().size();
        // set the field null
        salesOrder.setTransactionDate(null);

        // Create the SalesOrder, which fails.

        restSalesOrderMockMvc.perform(post("/api/sales-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrder)))
            .andExpect(status().isBadRequest());

        List<SalesOrder> salesOrderList = salesOrderRepository.findAll();
        assertThat(salesOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastUpdatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesOrderRepository.findAll().size();
        // set the field null
        salesOrder.setLastUpdated(null);

        // Create the SalesOrder, which fails.

        restSalesOrderMockMvc.perform(post("/api/sales-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrder)))
            .andExpect(status().isBadRequest());

        List<SalesOrder> salesOrderList = salesOrderRepository.findAll();
        assertThat(salesOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustomerIsRequired() throws Exception {
        int databaseSizeBeforeTest = salesOrderRepository.findAll().size();
        // set the field null
        salesOrder.setCustomer(null);

        // Create the SalesOrder, which fails.

        restSalesOrderMockMvc.perform(post("/api/sales-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrder)))
            .andExpect(status().isBadRequest());

        List<SalesOrder> salesOrderList = salesOrderRepository.findAll();
        assertThat(salesOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSalesOrders() throws Exception {
        // Initialize the database
        salesOrderRepository.saveAndFlush(salesOrder);

        // Get all the salesOrderList
        restSalesOrderMockMvc.perform(get("/api/sales-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED))))
            .andExpect(jsonPath("$.[*].customer").value(hasItem(DEFAULT_CUSTOMER.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getSalesOrder() throws Exception {
        // Initialize the database
        salesOrderRepository.saveAndFlush(salesOrder);

        // Get the salesOrder
        restSalesOrderMockMvc.perform(get("/api/sales-orders/{id}", salesOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(salesOrder.getId().intValue()))
            .andExpect(jsonPath("$.referenceNumber").value(DEFAULT_REFERENCE_NUMBER.toString()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(sameInstant(DEFAULT_LAST_UPDATED)))
            .andExpect(jsonPath("$.customer").value(DEFAULT_CUSTOMER.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSalesOrder() throws Exception {
        // Get the salesOrder
        restSalesOrderMockMvc.perform(get("/api/sales-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesOrder() throws Exception {
        // Initialize the database
        salesOrderRepository.saveAndFlush(salesOrder);
        salesOrderSearchRepository.save(salesOrder);
        int databaseSizeBeforeUpdate = salesOrderRepository.findAll().size();

        // Update the salesOrder
        SalesOrder updatedSalesOrder = salesOrderRepository.findOne(salesOrder.getId());
        updatedSalesOrder
            .referenceNumber(UPDATED_REFERENCE_NUMBER)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED)
            .customer(UPDATED_CUSTOMER)
            .status(UPDATED_STATUS);

        restSalesOrderMockMvc.perform(put("/api/sales-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalesOrder)))
            .andExpect(status().isOk());

        // Validate the SalesOrder in the database
        List<SalesOrder> salesOrderList = salesOrderRepository.findAll();
        assertThat(salesOrderList).hasSize(databaseSizeBeforeUpdate);
        SalesOrder testSalesOrder = salesOrderList.get(salesOrderList.size() - 1);
        assertThat(testSalesOrder.getReferenceNumber()).isEqualTo(UPDATED_REFERENCE_NUMBER);
        assertThat(testSalesOrder.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
        assertThat(testSalesOrder.getLastUpdated()).isEqualTo(UPDATED_LAST_UPDATED);
        assertThat(testSalesOrder.getCustomer()).isEqualTo(UPDATED_CUSTOMER);
        assertThat(testSalesOrder.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the SalesOrder in Elasticsearch
        SalesOrder salesOrderEs = salesOrderSearchRepository.findOne(testSalesOrder.getId());
        assertThat(salesOrderEs).isEqualToComparingFieldByField(testSalesOrder);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesOrder() throws Exception {
        int databaseSizeBeforeUpdate = salesOrderRepository.findAll().size();

        // Create the SalesOrder

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalesOrderMockMvc.perform(put("/api/sales-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesOrder)))
            .andExpect(status().isCreated());

        // Validate the SalesOrder in the database
        List<SalesOrder> salesOrderList = salesOrderRepository.findAll();
        assertThat(salesOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSalesOrder() throws Exception {
        // Initialize the database
        salesOrderRepository.saveAndFlush(salesOrder);
        salesOrderSearchRepository.save(salesOrder);
        int databaseSizeBeforeDelete = salesOrderRepository.findAll().size();

        // Get the salesOrder
        restSalesOrderMockMvc.perform(delete("/api/sales-orders/{id}", salesOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean salesOrderExistsInEs = salesOrderSearchRepository.exists(salesOrder.getId());
        assertThat(salesOrderExistsInEs).isFalse();

        // Validate the database is empty
        List<SalesOrder> salesOrderList = salesOrderRepository.findAll();
        assertThat(salesOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSalesOrder() throws Exception {
        // Initialize the database
        salesOrderRepository.saveAndFlush(salesOrder);
        salesOrderSearchRepository.save(salesOrder);

        // Search the salesOrder
        restSalesOrderMockMvc.perform(get("/api/_search/sales-orders?query=id:" + salesOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].referenceNumber").value(hasItem(DEFAULT_REFERENCE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(sameInstant(DEFAULT_LAST_UPDATED))))
            .andExpect(jsonPath("$.[*].customer").value(hasItem(DEFAULT_CUSTOMER.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesOrder.class);
        SalesOrder salesOrder1 = new SalesOrder();
        salesOrder1.setId(1L);
        SalesOrder salesOrder2 = new SalesOrder();
        salesOrder2.setId(salesOrder1.getId());
        assertThat(salesOrder1).isEqualTo(salesOrder2);
        salesOrder2.setId(2L);
        assertThat(salesOrder1).isNotEqualTo(salesOrder2);
        salesOrder1.setId(null);
        assertThat(salesOrder1).isNotEqualTo(salesOrder2);
    }
}
