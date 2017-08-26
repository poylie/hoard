package com.hoard.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hoard.app.domain.SalesOrderItem;

import com.hoard.app.repository.SalesOrderItemRepository;
import com.hoard.app.repository.search.SalesOrderItemSearchRepository;
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
 * REST controller for managing SalesOrderItem.
 */
@RestController
@RequestMapping("/api")
public class SalesOrderItemResource {

    private final Logger log = LoggerFactory.getLogger(SalesOrderItemResource.class);

    private static final String ENTITY_NAME = "salesOrderItem";

    private final SalesOrderItemRepository salesOrderItemRepository;

    private final SalesOrderItemSearchRepository salesOrderItemSearchRepository;

    public SalesOrderItemResource(SalesOrderItemRepository salesOrderItemRepository, SalesOrderItemSearchRepository salesOrderItemSearchRepository) {
        this.salesOrderItemRepository = salesOrderItemRepository;
        this.salesOrderItemSearchRepository = salesOrderItemSearchRepository;
    }

    /**
     * POST  /sales-order-items : Create a new salesOrderItem.
     *
     * @param salesOrderItem the salesOrderItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salesOrderItem, or with status 400 (Bad Request) if the salesOrderItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales-order-items")
    @Timed
    public ResponseEntity<SalesOrderItem> createSalesOrderItem(@Valid @RequestBody SalesOrderItem salesOrderItem) throws URISyntaxException {
        log.debug("REST request to save SalesOrderItem : {}", salesOrderItem);
        if (salesOrderItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new salesOrderItem cannot already have an ID")).body(null);
        }
        SalesOrderItem result = salesOrderItemRepository.save(salesOrderItem);
        salesOrderItemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sales-order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sales-order-items : Updates an existing salesOrderItem.
     *
     * @param salesOrderItem the salesOrderItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salesOrderItem,
     * or with status 400 (Bad Request) if the salesOrderItem is not valid,
     * or with status 500 (Internal Server Error) if the salesOrderItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sales-order-items")
    @Timed
    public ResponseEntity<SalesOrderItem> updateSalesOrderItem(@Valid @RequestBody SalesOrderItem salesOrderItem) throws URISyntaxException {
        log.debug("REST request to update SalesOrderItem : {}", salesOrderItem);
        if (salesOrderItem.getId() == null) {
            return createSalesOrderItem(salesOrderItem);
        }
        SalesOrderItem result = salesOrderItemRepository.save(salesOrderItem);
        salesOrderItemSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salesOrderItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sales-order-items : get all the salesOrderItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salesOrderItems in body
     */
    @GetMapping("/sales-order-items")
    @Timed
    public List<SalesOrderItem> getAllSalesOrderItems() {
        log.debug("REST request to get all SalesOrderItems");
        return salesOrderItemRepository.findAll();
    }

    /**
     * GET  /sales-order-items/:id : get the "id" salesOrderItem.
     *
     * @param id the id of the salesOrderItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salesOrderItem, or with status 404 (Not Found)
     */
    @GetMapping("/sales-order-items/{id}")
    @Timed
    public ResponseEntity<SalesOrderItem> getSalesOrderItem(@PathVariable Long id) {
        log.debug("REST request to get SalesOrderItem : {}", id);
        SalesOrderItem salesOrderItem = salesOrderItemRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salesOrderItem));
    }

    /**
     * DELETE  /sales-order-items/:id : delete the "id" salesOrderItem.
     *
     * @param id the id of the salesOrderItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sales-order-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalesOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete SalesOrderItem : {}", id);
        salesOrderItemRepository.delete(id);
        salesOrderItemSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/sales-order-items?query=:query : search for the salesOrderItem corresponding
     * to the query.
     *
     * @param query the query of the salesOrderItem search
     * @return the result of the search
     */
    @GetMapping("/_search/sales-order-items")
    @Timed
    public List<SalesOrderItem> searchSalesOrderItems(@RequestParam String query) {
        log.debug("REST request to search SalesOrderItems for query {}", query);
        return StreamSupport
            .stream(salesOrderItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
