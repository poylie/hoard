package com.hoard.app.repository.search;

import com.hoard.app.domain.SalesOrderItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SalesOrderItem entity.
 */
public interface SalesOrderItemSearchRepository extends ElasticsearchRepository<SalesOrderItem, Long> {
}
