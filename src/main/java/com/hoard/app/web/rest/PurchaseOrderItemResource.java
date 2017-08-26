package com.hoard.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hoard.app.domain.PurchaseOrderItem;

import com.hoard.app.repository.PurchaseOrderItemRepository;
import com.hoard.app.repository.search.PurchaseOrderItemSearchRepository;
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
 * REST controller for managing PurchaseOrderItem.
 */
@RestController
@RequestMapping("/api")
public class PurchaseOrderItemResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseOrderItemResource.class);

    private static final String ENTITY_NAME = "purchaseOrderItem";

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    private final PurchaseOrderItemSearchRepository purchaseOrderItemSearchRepository;

    public PurchaseOrderItemResource(PurchaseOrderItemRepository purchaseOrderItemRepository, PurchaseOrderItemSearchRepository purchaseOrderItemSearchRepository) {
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.purchaseOrderItemSearchRepository = purchaseOrderItemSearchRepository;
    }

    /**
     * POST  /purchase-order-items : Create a new purchaseOrderItem.
     *
     * @param purchaseOrderItem the purchaseOrderItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purchaseOrderItem, or with status 400 (Bad Request) if the purchaseOrderItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purchase-order-items")
    @Timed
    public ResponseEntity<PurchaseOrderItem> createPurchaseOrderItem(@Valid @RequestBody PurchaseOrderItem purchaseOrderItem) throws URISyntaxException {
        log.debug("REST request to save PurchaseOrderItem : {}", purchaseOrderItem);
        if (purchaseOrderItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new purchaseOrderItem cannot already have an ID")).body(null);
        }
        PurchaseOrderItem result = purchaseOrderItemRepository.save(purchaseOrderItem);
        purchaseOrderItemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/purchase-order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /purchase-order-items : Updates an existing purchaseOrderItem.
     *
     * @param purchaseOrderItem the purchaseOrderItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purchaseOrderItem,
     * or with status 400 (Bad Request) if the purchaseOrderItem is not valid,
     * or with status 500 (Internal Server Error) if the purchaseOrderItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purchase-order-items")
    @Timed
    public ResponseEntity<PurchaseOrderItem> updatePurchaseOrderItem(@Valid @RequestBody PurchaseOrderItem purchaseOrderItem) throws URISyntaxException {
        log.debug("REST request to update PurchaseOrderItem : {}", purchaseOrderItem);
        if (purchaseOrderItem.getId() == null) {
            return createPurchaseOrderItem(purchaseOrderItem);
        }
        PurchaseOrderItem result = purchaseOrderItemRepository.save(purchaseOrderItem);
        purchaseOrderItemSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purchaseOrderItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /purchase-order-items : get all the purchaseOrderItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of purchaseOrderItems in body
     */
    @GetMapping("/purchase-order-items")
    @Timed
    public List<PurchaseOrderItem> getAllPurchaseOrderItems() {
        log.debug("REST request to get all PurchaseOrderItems");
        return purchaseOrderItemRepository.findAll();
    }

    /**
     * GET  /purchase-order-items/:id : get the "id" purchaseOrderItem.
     *
     * @param id the id of the purchaseOrderItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purchaseOrderItem, or with status 404 (Not Found)
     */
    @GetMapping("/purchase-order-items/{id}")
    @Timed
    public ResponseEntity<PurchaseOrderItem> getPurchaseOrderItem(@PathVariable Long id) {
        log.debug("REST request to get PurchaseOrderItem : {}", id);
        PurchaseOrderItem purchaseOrderItem = purchaseOrderItemRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(purchaseOrderItem));
    }

    /**
     * DELETE  /purchase-order-items/:id : delete the "id" purchaseOrderItem.
     *
     * @param id the id of the purchaseOrderItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purchase-order-items/{id}")
    @Timed
    public ResponseEntity<Void> deletePurchaseOrderItem(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseOrderItem : {}", id);
        purchaseOrderItemRepository.delete(id);
        purchaseOrderItemSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/purchase-order-items?query=:query : search for the purchaseOrderItem corresponding
     * to the query.
     *
     * @param query the query of the purchaseOrderItem search
     * @return the result of the search
     */
    @GetMapping("/_search/purchase-order-items")
    @Timed
    public List<PurchaseOrderItem> searchPurchaseOrderItems(@RequestParam String query) {
        log.debug("REST request to search PurchaseOrderItems for query {}", query);
        return StreamSupport
            .stream(purchaseOrderItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
