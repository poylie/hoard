package com.hoard.app.repository.search;

import com.hoard.app.domain.Request;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Request entity.
 */
public interface RequestSearchRepository extends ElasticsearchRepository<Request, Long> {
}
