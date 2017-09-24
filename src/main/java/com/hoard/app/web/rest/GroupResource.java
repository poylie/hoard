package com.hoard.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hoard.app.domain.Group;
import com.hoard.app.domain.User;
import com.hoard.app.domain.UserGroup;
import com.hoard.app.domain.enumeration.Feature;
import com.hoard.app.domain.enumeration.Permission;
import com.hoard.app.repository.GroupRepository;
import com.hoard.app.repository.UserGroupRepository;
import com.hoard.app.repository.UserRepository;
import com.hoard.app.repository.search.GroupSearchRepository;
import com.hoard.app.security.SecurityUtils;
import com.hoard.app.web.rest.util.HeaderUtil;
import com.hoard.app.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Group.
 */
@RestController
@RequestMapping("/api")
public class GroupResource {

    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    private static final String ENTITY_NAME = "group";

    private final GroupRepository groupRepository;

    private final GroupSearchRepository groupSearchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    public GroupResource(GroupRepository groupRepository, GroupSearchRepository groupSearchRepository) {
        this.groupRepository = groupRepository;
        this.groupSearchRepository = groupSearchRepository;
    }

    /**
     * POST  /groups : Create a new group.
     *
     * @param group the group to create
     * @return the ResponseEntity with status 201 (Created) and with body the new group, or with status 400 (Bad Request) if the group has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groups")
    @Timed
    public ResponseEntity<Group> createGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.debug("REST request to save Group : {}", group);
        if (group.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new group cannot already have an ID")).body(null);
        }

        group.setUsers(populateUserGroup(group));

        Group result = groupRepository.save(group);
        groupSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private Set<UserGroup> populateUserGroup(Group group) {
        Set<UserGroup> userGroups = new HashSet<>();

        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        Arrays.stream(Feature.values()).forEach(feature -> {
            Arrays.stream(Permission.values()).forEach(permission -> {
                UserGroup userGroup = new UserGroup();
                userGroup.setPermission(permission);
                userGroup.setFeature(feature);
                userGroup.setUser(user);
                userGroup.setGroup(group);

                userGroups.add(userGroup);
            });
        });

        return userGroups;
    }


    /**
     * PUT  /groups : Updates an existing group.
     *
     * @param group the group to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated group,
     * or with status 400 (Bad Request) if the group is not valid,
     * or with status 500 (Internal Server Error) if the group couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groups")
    @Timed
    public ResponseEntity<Group> updateGroup(@Valid @RequestBody Group group) throws URISyntaxException {
        log.debug("REST request to update Group : {}", group);
        if (group.getId() == null) {
            return createGroup(group);
        }
        Group result = groupRepository.save(group);
        groupSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, group.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groups : get all the groups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of groups in body
     */
    @GetMapping("/groups")
    @Timed
    public ResponseEntity<List<Group>> getAllGroups(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Groups");
        Page<Group> page = groupRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/groupsForUser")
    @Timed
    public ResponseEntity<List<Group>> getAllGroupsForCurrentUser(@ApiParam Pageable pageable) {
        log.debug("REST request to get all Groups for User");
        Page<Group> page = new PageImpl<Group>(groupRepository.findDistinctByUsersUserLogin(SecurityUtils.getCurrentUserLogin(), pageable));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groupsForUser");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /groups/:id : get the "id" group.
     *
     * @param id the id of the group to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the group, or with status 404 (Not Found)
     */
    @GetMapping("/groups/{id}")
    @Timed
    public ResponseEntity<Group> getGroup(@PathVariable Long id) {
        log.debug("REST request to get Group : {}", id);
        Group group = groupRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(group));
    }

    /**
     * DELETE  /groups/:id : delete the "id" group.
     *
     * @param id the id of the group to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        log.debug("REST request to delete Group : {}", id);
        groupRepository.delete(id);
        groupSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/groups?query=:query : search for the group corresponding
     * to the query.
     *
     * @param query    the query of the group search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/groups")
    @Timed
    public ResponseEntity<List<Group>> searchGroups(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Groups for query {}", query);
        Page<Group> page = groupSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * SEARCH  /_search/groups?query=:query : search for the group corresponding
     * to the query.
     *
     * @param query the query of the group search
     * @return the result of the search
     */
    @GetMapping("/_search/groupsCurrentuser")
    @Timed
    public ResponseEntity<List<Group>> searchGroupsCurrentUser(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search Groups for query {}", query);

        List<UserGroup> userGroupList = userGroupRepository.findByUserIsCurrentUser();
        Page<Group> page = new PageImpl<Group>(StreamSupport
            .stream(groupSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
            .filter(group -> userGroupList.stream().map(userGroup -> userGroup.getGroup().getId()).collect(Collectors.toList()).contains(group.getId())).collect(Collectors.toList()));

        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
