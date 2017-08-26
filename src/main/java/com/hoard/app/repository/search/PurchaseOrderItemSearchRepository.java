package com.hoard.app.repository.search;

import com.hoard.app.domain.PurchaseOrderItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PurchaseOrderItem entity.
 */
public interface PurchaseOrderItemSearchRepository extends ElasticsearchRepository<PurchaseOrderItem, Long> {
}
