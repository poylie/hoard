package com.hoard.app.web.rest;

import com.hoard.app.HoardApp;

import com.hoard.app.domain.Invitation;
import com.hoard.app.repository.InvitationRepository;
import com.hoard.app.repository.search.InvitationSearchRepository;
import com.hoard.app.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hoard.app.domain.enumeration.InvitationStatus;
/**
 * Test class for the InvitationResource REST controller.
 *
 * @see InvitationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HoardApp.class)
public class InvitationResourceIntTest {

    private static final InvitationStatus DEFAULT_INVITATION_STATUS = InvitationStatus.PENDING;
    private static final InvitationStatus UPDATED_INVITATION_STATUS = InvitationStatus.ACCEPTED;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private InvitationSearchRepository invitationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInvitationMockMvc;

    private Invitation invitation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InvitationResource invitationResource = new InvitationResource(invitationRepository, invitationSearchRepository);
        this.restInvitationMockMvc = MockMvcBuilders.standaloneSetup(invitationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invitation createEntity(EntityManager em) {
        Invitation invitation = new Invitation()
            .invitationStatus(DEFAULT_INVITATION_STATUS);
        return invitation;
    }

    @Before
    public void initTest() {
        invitationSearchRepository.deleteAll();
        invitation = createEntity(em);
    }

    @Test
    @Transactional
    public void createInvitation() throws Exception {
        int databaseSizeBeforeCreate = invitationRepository.findAll().size();

        // Create the Invitation
        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitation)))
            .andExpect(status().isCreated());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeCreate + 1);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getInvitationStatus()).isEqualTo(DEFAULT_INVITATION_STATUS);

        // Validate the Invitation in Elasticsearch
        Invitation invitationEs = invitationSearchRepository.findOne(testInvitation.getId());
        assertThat(invitationEs).isEqualToComparingFieldByField(testInvitation);
    }

    @Test
    @Transactional
    public void createInvitationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = invitationRepository.findAll().size();

        // Create the Invitation with an existing ID
        invitation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvitationMockMvc.perform(post("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitation)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllInvitations() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get all the invitationList
        restInvitationMockMvc.perform(get("/api/invitations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].invitationStatus").value(hasItem(DEFAULT_INVITATION_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);

        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", invitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(invitation.getId().intValue()))
            .andExpect(jsonPath("$.invitationStatus").value(DEFAULT_INVITATION_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvitation() throws Exception {
        // Get the invitation
        restInvitationMockMvc.perform(get("/api/invitations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);
        invitationSearchRepository.save(invitation);
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Update the invitation
        Invitation updatedInvitation = invitationRepository.findOne(invitation.getId());
        updatedInvitation
            .invitationStatus(UPDATED_INVITATION_STATUS);

        restInvitationMockMvc.perform(put("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedInvitation)))
            .andExpect(status().isOk());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate);
        Invitation testInvitation = invitationList.get(invitationList.size() - 1);
        assertThat(testInvitation.getInvitationStatus()).isEqualTo(UPDATED_INVITATION_STATUS);

        // Validate the Invitation in Elasticsearch
        Invitation invitationEs = invitationSearchRepository.findOne(testInvitation.getId());
        assertThat(invitationEs).isEqualToComparingFieldByField(testInvitation);
    }

    @Test
    @Transactional
    public void updateNonExistingInvitation() throws Exception {
        int databaseSizeBeforeUpdate = invitationRepository.findAll().size();

        // Create the Invitation

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInvitationMockMvc.perform(put("/api/invitations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(invitation)))
            .andExpect(status().isCreated());

        // Validate the Invitation in the database
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);
        invitationSearchRepository.save(invitation);
        int databaseSizeBeforeDelete = invitationRepository.findAll().size();

        // Get the invitation
        restInvitationMockMvc.perform(delete("/api/invitations/{id}", invitation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean invitationExistsInEs = invitationSearchRepository.exists(invitation.getId());
        assertThat(invitationExistsInEs).isFalse();

        // Validate the database is empty
        List<Invitation> invitationList = invitationRepository.findAll();
        assertThat(invitationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInvitation() throws Exception {
        // Initialize the database
        invitationRepository.saveAndFlush(invitation);
        invitationSearchRepository.save(invitation);

        // Search the invitation
        restInvitationMockMvc.perform(get("/api/_search/invitations?query=id:" + invitation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invitation.getId().intValue())))
            .andExpect(jsonPath("$.[*].invitationStatus").value(hasItem(DEFAULT_INVITATION_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invitation.class);
        Invitation invitation1 = new Invitation();
        invitation1.setId(1L);
        Invitation invitation2 = new Invitation();
        invitation2.setId(invitation1.getId());
        assertThat(invitation1).isEqualTo(invitation2);
        invitation2.setId(2L);
        assertThat(invitation1).isNotEqualTo(invitation2);
        invitation1.setId(null);
        assertThat(invitation1).isNotEqualTo(invitation2);
    }
}
