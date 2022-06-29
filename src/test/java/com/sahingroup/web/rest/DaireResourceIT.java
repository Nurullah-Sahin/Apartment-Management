package com.sahingroup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sahingroup.IntegrationTest;
import com.sahingroup.domain.Daire;
import com.sahingroup.repository.DaireRepository;
import com.sahingroup.service.DaireService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link DaireResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DaireResourceIT {

    private static final String DEFAULT_NO = "AAAAAAAAAA";
    private static final String UPDATED_NO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/daires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DaireRepository daireRepository;

    @Mock
    private DaireRepository daireRepositoryMock;

    @Mock
    private DaireService daireServiceMock;

    @Autowired
    private MockMvc restDaireMockMvc;

    private Daire daire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Daire createEntity() {
        Daire daire = new Daire().no(DEFAULT_NO);
        return daire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Daire createUpdatedEntity() {
        Daire daire = new Daire().no(UPDATED_NO);
        return daire;
    }

    @BeforeEach
    public void initTest() {
        daireRepository.deleteAll();
        daire = createEntity();
    }

    @Test
    void createDaire() throws Exception {
        int databaseSizeBeforeCreate = daireRepository.findAll().size();
        // Create the Daire
        restDaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(daire)))
            .andExpect(status().isCreated());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeCreate + 1);
        Daire testDaire = daireList.get(daireList.size() - 1);
        assertThat(testDaire.getNo()).isEqualTo(DEFAULT_NO);
    }

    @Test
    void createDaireWithExistingId() throws Exception {
        // Create the Daire with an existing ID
        daire.setId("existing_id");

        int databaseSizeBeforeCreate = daireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(daire)))
            .andExpect(status().isBadRequest());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDaires() throws Exception {
        // Initialize the database
        daireRepository.save(daire);

        // Get all the daireList
        restDaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(daire.getId())))
            .andExpect(jsonPath("$.[*].no").value(hasItem(DEFAULT_NO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDairesWithEagerRelationshipsIsEnabled() throws Exception {
        when(daireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(daireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDairesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(daireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(daireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getDaire() throws Exception {
        // Initialize the database
        daireRepository.save(daire);

        // Get the daire
        restDaireMockMvc
            .perform(get(ENTITY_API_URL_ID, daire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(daire.getId()))
            .andExpect(jsonPath("$.no").value(DEFAULT_NO));
    }

    @Test
    void getNonExistingDaire() throws Exception {
        // Get the daire
        restDaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewDaire() throws Exception {
        // Initialize the database
        daireRepository.save(daire);

        int databaseSizeBeforeUpdate = daireRepository.findAll().size();

        // Update the daire
        Daire updatedDaire = daireRepository.findById(daire.getId()).get();
        updatedDaire.no(UPDATED_NO);

        restDaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDaire))
            )
            .andExpect(status().isOk());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeUpdate);
        Daire testDaire = daireList.get(daireList.size() - 1);
        assertThat(testDaire.getNo()).isEqualTo(UPDATED_NO);
    }

    @Test
    void putNonExistingDaire() throws Exception {
        int databaseSizeBeforeUpdate = daireRepository.findAll().size();
        daire.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, daire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(daire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDaire() throws Exception {
        int databaseSizeBeforeUpdate = daireRepository.findAll().size();
        daire.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(daire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDaire() throws Exception {
        int databaseSizeBeforeUpdate = daireRepository.findAll().size();
        daire.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(daire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDaireWithPatch() throws Exception {
        // Initialize the database
        daireRepository.save(daire);

        int databaseSizeBeforeUpdate = daireRepository.findAll().size();

        // Update the daire using partial update
        Daire partialUpdatedDaire = new Daire();
        partialUpdatedDaire.setId(daire.getId());

        partialUpdatedDaire.no(UPDATED_NO);

        restDaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDaire))
            )
            .andExpect(status().isOk());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeUpdate);
        Daire testDaire = daireList.get(daireList.size() - 1);
        assertThat(testDaire.getNo()).isEqualTo(UPDATED_NO);
    }

    @Test
    void fullUpdateDaireWithPatch() throws Exception {
        // Initialize the database
        daireRepository.save(daire);

        int databaseSizeBeforeUpdate = daireRepository.findAll().size();

        // Update the daire using partial update
        Daire partialUpdatedDaire = new Daire();
        partialUpdatedDaire.setId(daire.getId());

        partialUpdatedDaire.no(UPDATED_NO);

        restDaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDaire))
            )
            .andExpect(status().isOk());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeUpdate);
        Daire testDaire = daireList.get(daireList.size() - 1);
        assertThat(testDaire.getNo()).isEqualTo(UPDATED_NO);
    }

    @Test
    void patchNonExistingDaire() throws Exception {
        int databaseSizeBeforeUpdate = daireRepository.findAll().size();
        daire.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, daire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(daire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDaire() throws Exception {
        int databaseSizeBeforeUpdate = daireRepository.findAll().size();
        daire.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(daire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDaire() throws Exception {
        int databaseSizeBeforeUpdate = daireRepository.findAll().size();
        daire.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDaireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(daire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Daire in the database
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDaire() throws Exception {
        // Initialize the database
        daireRepository.save(daire);

        int databaseSizeBeforeDelete = daireRepository.findAll().size();

        // Delete the daire
        restDaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, daire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Daire> daireList = daireRepository.findAll();
        assertThat(daireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
