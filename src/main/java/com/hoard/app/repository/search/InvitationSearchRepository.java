package com.hoard.app.repository.search;

import com.hoard.app.domain.Invitation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Invitation entity.
 */
public interface InvitationSearchRepository extends ElasticsearchRepository<Invitation, Long> {
}
