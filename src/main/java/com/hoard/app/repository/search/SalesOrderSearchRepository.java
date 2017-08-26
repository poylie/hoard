package com.hoard.app.repository.search;

import com.hoard.app.domain.SalesOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SalesOrder entity.
 */
public interface SalesOrderSearchRepository extends ElasticsearchRepository<SalesOrder, Long> {
}
