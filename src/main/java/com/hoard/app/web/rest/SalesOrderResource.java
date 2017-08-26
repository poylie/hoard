package com.hoard.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hoard.app.domain.SalesOrder;

import com.hoard.app.repository.SalesOrderRepository;
import com.hoard.app.repository.search.SalesOrderSearchRepository;
import com.hoard.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SalesOrder.
 */
@RestController
@RequestMapping("/api")
public class SalesOrderResource {

    private final Logger log = LoggerFactory.getLogger(SalesOrderResource.class);

    private static final String ENTITY_NAME = "salesOrder";

    private final SalesOrderRepository salesOrderRepository;

    private final SalesOrderSearchRepository salesOrderSearchRepository;

    public SalesOrderResource(SalesOrderRepository salesOrderRepository, SalesOrderSearchRepository salesOrderSearchRepository) {
        this.salesOrderRepository = salesOrderRepository;
        this.salesOrderSearchRepository = salesOrderSearchRepository;
    }

    /**
     * POST  /sales-orders : Create a new salesOrder.
     *
     * @param salesOrder the salesOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salesOrder, or with status 400 (Bad Request) if the salesOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales-orders")
    @Timed
    public ResponseEntity<SalesOrder> createSalesOrder(@Valid @RequestBody SalesOrder salesOrder) throws URISyntaxException {
        log.debug("REST request to save SalesOrder : {}", salesOrder);
        if (salesOrder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new salesOrder cannot already have an ID")).body(null);
        }
        SalesOrder result = salesOrderRepository.save(salesOrder);
        salesOrderSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sales-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sales-orders : Updates an existing salesOrder.
     *
     * @param salesOrder the salesOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salesOrder,
     * or with status 400 (Bad Request) if the salesOrder is not valid,
     * or with status 500 (Internal Server Error) if the salesOrder couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sales-orders")
    @Timed
    public ResponseEntity<SalesOrder> updateSalesOrder(@Valid @RequestBody SalesOrder salesOrder) throws URISyntaxException {
        log.debug("REST request to update SalesOrder : {}", salesOrder);
        if (salesOrder.getId() == null) {
            return createSalesOrder(salesOrder);
        }
        SalesOrder result = salesOrderRepository.save(salesOrder);
        salesOrderSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salesOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sales-orders : get all the salesOrders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salesOrders in body
     */
    @GetMapping("/sales-orders")
    @Timed
    public List<SalesOrder> getAllSalesOrders() {
        log.debug("REST request to get all SalesOrders");
        return salesOrderRepository.findAll();
    }

    /**
     * GET  /sales-orders/:id : get the "id" salesOrder.
     *
     * @param id the id of the salesOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salesOrder, or with status 404 (Not Found)
     */
    @GetMapping("/sales-orders/{id}")
    @Timed
    public ResponseEntity<SalesOrder> getSalesOrder(@PathVariable Long id) {
        log.debug("REST request to get SalesOrder : {}", id);
        SalesOrder salesOrder = salesOrderRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salesOrder));
    }

    /**
     * DELETE  /sales-orders/:id : delete the "id" salesOrder.
     *
     * @param id the id of the salesOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sales-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable Long id) {
        log.debug("REST request to delete SalesOrder : {}", id);
        salesOrderRepository.delete(id);
        salesOrderSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sales-orders?query=:query : search for the salesOrder corresponding
     * to the query.
     *
     * @param query the query of the salesOrder search
     * @return the result of the search
     */
    @GetMapping("/_search/sales-orders")
    @Timed
    public List<SalesOrder> searchSalesOrders(@RequestParam String query) {
        log.debug("REST request to search SalesOrders for query {}", query);
        return StreamSupport
            .stream(salesOrderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
