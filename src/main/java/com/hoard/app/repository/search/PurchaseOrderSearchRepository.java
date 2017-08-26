package com.hoard.app.repository.search;

import com.hoard.app.domain.PurchaseOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PurchaseOrder entity.
 */
public interface PurchaseOrderSearchRepository extends ElasticsearchRepository<PurchaseOrder, Long> {
}
