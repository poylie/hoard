package com.hoard.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hoard.app.domain.Invitation;

import com.hoard.app.repository.InvitationRepository;
import com.hoard.app.repository.search.InvitationSearchRepository;
import com.hoard.app.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Invitation.
 */
@RestController
@RequestMapping("/api")
public class InvitationResource {

    private final Logger log = LoggerFactory.getLogger(InvitationResource.class);

    private static final String ENTITY_NAME = "invitation";

    private final InvitationRepository invitationRepository;

    private final InvitationSearchRepository invitationSearchRepository;

    public InvitationResource(InvitationRepository invitationRepository, InvitationSearchRepository invitationSearchRepository) {
        this.invitationRepository = invitationRepository;
        this.invitationSearchRepository = invitationSearchRepository;
    }

    /**
     * POST  /invitations : Create a new invitation.
     *
     * @param invitation the invitation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invitation, or with status 400 (Bad Request) if the invitation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/invitations")
    @Timed
    public ResponseEntity<Invitation> createInvitation(@RequestBody Invitation invitation) throws URISyntaxException {
        log.debug("REST request to save Invitation : {}", invitation);
        if (invitation.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new invitation cannot already have an ID")).body(null);
        }
        Invitation result = invitationRepository.save(invitation);
        invitationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/invitations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /invitations : Updates an existing invitation.
     *
     * @param invitation the invitation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invitation,
     * or with status 400 (Bad Request) if the invitation is not valid,
     * or with status 500 (Internal Server Error) if the invitation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/invitations")
    @Timed
    public ResponseEntity<Invitation> updateInvitation(@RequestBody Invitation invitation) throws URISyntaxException {
        log.debug("REST request to update Invitation : {}", invitation);
        if (invitation.getId() == null) {
            return createInvitation(invitation);
        }
        Invitation result = invitationRepository.save(invitation);
        invitationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, invitation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invitations : get all the invitations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of invitations in body
     */
    @GetMapping("/invitations")
    @Timed
    public List<Invitation> getAllInvitations() {
        log.debug("REST request to get all Invitations");
        return invitationRepository.findAll();
    }

    /**
     * GET  /invitations/:id : get the "id" invitation.
     *
     * @param id the id of the invitation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invitation, or with status 404 (Not Found)
     */
    @GetMapping("/invitations/{id}")
    @Timed
    public ResponseEntity<Invitation> getInvitation(@PathVariable Long id) {
        log.debug("REST request to get Invitation : {}", id);
        Invitation invitation = invitationRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(invitation));
    }

    /**
     * DELETE  /invitations/:id : delete the "id" invitation.
     *
     * @param id the id of the invitation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/invitations/{id}")
    @Timed
    public ResponseEntity<Void> deleteInvitation(@PathVariable Long id) {
        log.debug("REST request to delete Invitation : {}", id);
        invitationRepository.delete(id);
        invitationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/invitations?query=:query : search for the invitation corresponding
     * to the query.
     *
     * @param query the query of the invitation search
     * @return the result of the search
     */
    @GetMapping("/_search/invitations")
    @Timed
    public List<Invitation> searchInvitations(@RequestParam String query) {
        log.debug("REST request to search Invitations for query {}", query);
        return StreamSupport
            .stream(invitationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
