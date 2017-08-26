package com.hoard.app.repository.search;

import com.hoard.app.domain.Group;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Group entity.
 */
public interface GroupSearchRepository extends ElasticsearchRepository<Group, Long> {
}
