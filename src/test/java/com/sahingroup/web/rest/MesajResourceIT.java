package com.sahingroup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sahingroup.IntegrationTest;
import com.sahingroup.domain.Mesaj;
import com.sahingroup.repository.MesajRepository;
import com.sahingroup.service.MesajService;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link MesajResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MesajResourceIT {

    private static final String DEFAULT_MESAJ_ICERIK = "AAAAAAAAAA";
    private static final String UPDATED_MESAJ_ICERIK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AKTIF = false;
    private static final Boolean UPDATED_AKTIF = true;

    private static final String ENTITY_API_URL = "/api/mesajs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MesajRepository mesajRepository;

    @Mock
    private MesajRepository mesajRepositoryMock;

    @Mock
    private MesajService mesajServiceMock;

    @Autowired
    private MockMvc restMesajMockMvc;

    private Mesaj mesaj;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mesaj createEntity() {
        Mesaj mesaj = new Mesaj().mesajIcerik(DEFAULT_MESAJ_ICERIK).aktif(DEFAULT_AKTIF);
        return mesaj;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mesaj createUpdatedEntity() {
        Mesaj mesaj = new Mesaj().mesajIcerik(UPDATED_MESAJ_ICERIK).aktif(UPDATED_AKTIF);
        return mesaj;
    }

    @BeforeEach
    public void initTest() {
        mesajRepository.deleteAll();
        mesaj = createEntity();
    }

    @Test
    void createMesaj() throws Exception {
        int databaseSizeBeforeCreate = mesajRepository.findAll().size();
        // Create the Mesaj
        restMesajMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesaj)))
            .andExpect(status().isCreated());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeCreate + 1);
        Mesaj testMesaj = mesajList.get(mesajList.size() - 1);
        assertThat(testMesaj.getMesajIcerik()).isEqualTo(DEFAULT_MESAJ_ICERIK);
        assertThat(testMesaj.getAktif()).isEqualTo(DEFAULT_AKTIF);
    }

    @Test
    void createMesajWithExistingId() throws Exception {
        // Create the Mesaj with an existing ID
        mesaj.setId("existing_id");

        int databaseSizeBeforeCreate = mesajRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMesajMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesaj)))
            .andExpect(status().isBadRequest());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMesajs() throws Exception {
        // Initialize the database
        mesajRepository.save(mesaj);

        // Get all the mesajList
        restMesajMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mesaj.getId())))
            .andExpect(jsonPath("$.[*].mesajIcerik").value(hasItem(DEFAULT_MESAJ_ICERIK.toString())))
            .andExpect(jsonPath("$.[*].aktif").value(hasItem(DEFAULT_AKTIF.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMesajsWithEagerRelationshipsIsEnabled() throws Exception {
        when(mesajServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMesajMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mesajServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMesajsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(mesajServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMesajMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(mesajServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getMesaj() throws Exception {
        // Initialize the database
        mesajRepository.save(mesaj);

        // Get the mesaj
        restMesajMockMvc
            .perform(get(ENTITY_API_URL_ID, mesaj.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mesaj.getId()))
            .andExpect(jsonPath("$.mesajIcerik").value(DEFAULT_MESAJ_ICERIK.toString()))
            .andExpect(jsonPath("$.aktif").value(DEFAULT_AKTIF.booleanValue()));
    }

    @Test
    void getNonExistingMesaj() throws Exception {
        // Get the mesaj
        restMesajMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewMesaj() throws Exception {
        // Initialize the database
        mesajRepository.save(mesaj);

        int databaseSizeBeforeUpdate = mesajRepository.findAll().size();

        // Update the mesaj
        Mesaj updatedMesaj = mesajRepository.findById(mesaj.getId()).get();
        updatedMesaj.mesajIcerik(UPDATED_MESAJ_ICERIK).aktif(UPDATED_AKTIF);

        restMesajMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMesaj.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMesaj))
            )
            .andExpect(status().isOk());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeUpdate);
        Mesaj testMesaj = mesajList.get(mesajList.size() - 1);
        assertThat(testMesaj.getMesajIcerik()).isEqualTo(UPDATED_MESAJ_ICERIK);
        assertThat(testMesaj.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    void putNonExistingMesaj() throws Exception {
        int databaseSizeBeforeUpdate = mesajRepository.findAll().size();
        mesaj.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesajMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mesaj.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesaj))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMesaj() throws Exception {
        int databaseSizeBeforeUpdate = mesajRepository.findAll().size();
        mesaj.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesajMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mesaj))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMesaj() throws Exception {
        int databaseSizeBeforeUpdate = mesajRepository.findAll().size();
        mesaj.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesajMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mesaj)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMesajWithPatch() throws Exception {
        // Initialize the database
        mesajRepository.save(mesaj);

        int databaseSizeBeforeUpdate = mesajRepository.findAll().size();

        // Update the mesaj using partial update
        Mesaj partialUpdatedMesaj = new Mesaj();
        partialUpdatedMesaj.setId(mesaj.getId());

        partialUpdatedMesaj.aktif(UPDATED_AKTIF);

        restMesajMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesaj.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesaj))
            )
            .andExpect(status().isOk());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeUpdate);
        Mesaj testMesaj = mesajList.get(mesajList.size() - 1);
        assertThat(testMesaj.getMesajIcerik()).isEqualTo(DEFAULT_MESAJ_ICERIK);
        assertThat(testMesaj.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    void fullUpdateMesajWithPatch() throws Exception {
        // Initialize the database
        mesajRepository.save(mesaj);

        int databaseSizeBeforeUpdate = mesajRepository.findAll().size();

        // Update the mesaj using partial update
        Mesaj partialUpdatedMesaj = new Mesaj();
        partialUpdatedMesaj.setId(mesaj.getId());

        partialUpdatedMesaj.mesajIcerik(UPDATED_MESAJ_ICERIK).aktif(UPDATED_AKTIF);

        restMesajMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMesaj.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMesaj))
            )
            .andExpect(status().isOk());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeUpdate);
        Mesaj testMesaj = mesajList.get(mesajList.size() - 1);
        assertThat(testMesaj.getMesajIcerik()).isEqualTo(UPDATED_MESAJ_ICERIK);
        assertThat(testMesaj.getAktif()).isEqualTo(UPDATED_AKTIF);
    }

    @Test
    void patchNonExistingMesaj() throws Exception {
        int databaseSizeBeforeUpdate = mesajRepository.findAll().size();
        mesaj.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMesajMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mesaj.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesaj))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMesaj() throws Exception {
        int databaseSizeBeforeUpdate = mesajRepository.findAll().size();
        mesaj.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesajMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mesaj))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMesaj() throws Exception {
        int databaseSizeBeforeUpdate = mesajRepository.findAll().size();
        mesaj.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMesajMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mesaj)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mesaj in the database
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMesaj() throws Exception {
        // Initialize the database
        mesajRepository.save(mesaj);

        int databaseSizeBeforeDelete = mesajRepository.findAll().size();

        // Delete the mesaj
        restMesajMockMvc
            .perform(delete(ENTITY_API_URL_ID, mesaj.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mesaj> mesajList = mesajRepository.findAll();
        assertThat(mesajList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
