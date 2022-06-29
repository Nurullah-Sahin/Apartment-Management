package com.sahingroup.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sahingroup.IntegrationTest;
import com.sahingroup.domain.Apartman;
import com.sahingroup.repository.ApartmanRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ApartmanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApartmanResourceIT {

    private static final String DEFAULT_AD = "AAAAAAAAAA";
    private static final String UPDATED_AD = "BBBBBBBBBB";

    private static final Integer DEFAULT_KAT_SAYISI = 1;
    private static final Integer UPDATED_KAT_SAYISI = 2;

    private static final Integer DEFAULT_DAIRE_SAYISI = 1;
    private static final Integer UPDATED_DAIRE_SAYISI = 2;

    private static final String DEFAULT_ADRES = "AAAAAAAAAA";
    private static final String UPDATED_ADRES = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOLU_DAIRE_SAYISI = 1;
    private static final Integer UPDATED_DOLU_DAIRE_SAYISI = 2;

    private static final String ENTITY_API_URL = "/api/apartmen";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ApartmanRepository apartmanRepository;

    @Autowired
    private MockMvc restApartmanMockMvc;

    private Apartman apartman;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apartman createEntity() {
        Apartman apartman = new Apartman()
            .ad(DEFAULT_AD)
            .katSayisi(DEFAULT_KAT_SAYISI)
            .daireSayisi(DEFAULT_DAIRE_SAYISI)
            .adres(DEFAULT_ADRES)
            .doluDaireSayisi(DEFAULT_DOLU_DAIRE_SAYISI);
        return apartman;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Apartman createUpdatedEntity() {
        Apartman apartman = new Apartman()
            .ad(UPDATED_AD)
            .katSayisi(UPDATED_KAT_SAYISI)
            .daireSayisi(UPDATED_DAIRE_SAYISI)
            .adres(UPDATED_ADRES)
            .doluDaireSayisi(UPDATED_DOLU_DAIRE_SAYISI);
        return apartman;
    }

    @BeforeEach
    public void initTest() {
        apartmanRepository.deleteAll();
        apartman = createEntity();
    }

    @Test
    void createApartman() throws Exception {
        int databaseSizeBeforeCreate = apartmanRepository.findAll().size();
        // Create the Apartman
        restApartmanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apartman)))
            .andExpect(status().isCreated());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeCreate + 1);
        Apartman testApartman = apartmanList.get(apartmanList.size() - 1);
        assertThat(testApartman.getAd()).isEqualTo(DEFAULT_AD);
        assertThat(testApartman.getKatSayisi()).isEqualTo(DEFAULT_KAT_SAYISI);
        assertThat(testApartman.getDaireSayisi()).isEqualTo(DEFAULT_DAIRE_SAYISI);
        assertThat(testApartman.getAdres()).isEqualTo(DEFAULT_ADRES);
        assertThat(testApartman.getDoluDaireSayisi()).isEqualTo(DEFAULT_DOLU_DAIRE_SAYISI);
    }

    @Test
    void createApartmanWithExistingId() throws Exception {
        // Create the Apartman with an existing ID
        apartman.setId("existing_id");

        int databaseSizeBeforeCreate = apartmanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApartmanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apartman)))
            .andExpect(status().isBadRequest());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllApartmen() throws Exception {
        // Initialize the database
        apartmanRepository.save(apartman);

        // Get all the apartmanList
        restApartmanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apartman.getId())))
            .andExpect(jsonPath("$.[*].ad").value(hasItem(DEFAULT_AD)))
            .andExpect(jsonPath("$.[*].katSayisi").value(hasItem(DEFAULT_KAT_SAYISI)))
            .andExpect(jsonPath("$.[*].daireSayisi").value(hasItem(DEFAULT_DAIRE_SAYISI)))
            .andExpect(jsonPath("$.[*].adres").value(hasItem(DEFAULT_ADRES.toString())))
            .andExpect(jsonPath("$.[*].doluDaireSayisi").value(hasItem(DEFAULT_DOLU_DAIRE_SAYISI)));
    }

    @Test
    void getApartman() throws Exception {
        // Initialize the database
        apartmanRepository.save(apartman);

        // Get the apartman
        restApartmanMockMvc
            .perform(get(ENTITY_API_URL_ID, apartman.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(apartman.getId()))
            .andExpect(jsonPath("$.ad").value(DEFAULT_AD))
            .andExpect(jsonPath("$.katSayisi").value(DEFAULT_KAT_SAYISI))
            .andExpect(jsonPath("$.daireSayisi").value(DEFAULT_DAIRE_SAYISI))
            .andExpect(jsonPath("$.adres").value(DEFAULT_ADRES.toString()))
            .andExpect(jsonPath("$.doluDaireSayisi").value(DEFAULT_DOLU_DAIRE_SAYISI));
    }

    @Test
    void getNonExistingApartman() throws Exception {
        // Get the apartman
        restApartmanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewApartman() throws Exception {
        // Initialize the database
        apartmanRepository.save(apartman);

        int databaseSizeBeforeUpdate = apartmanRepository.findAll().size();

        // Update the apartman
        Apartman updatedApartman = apartmanRepository.findById(apartman.getId()).get();
        updatedApartman
            .ad(UPDATED_AD)
            .katSayisi(UPDATED_KAT_SAYISI)
            .daireSayisi(UPDATED_DAIRE_SAYISI)
            .adres(UPDATED_ADRES)
            .doluDaireSayisi(UPDATED_DOLU_DAIRE_SAYISI);

        restApartmanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedApartman.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedApartman))
            )
            .andExpect(status().isOk());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeUpdate);
        Apartman testApartman = apartmanList.get(apartmanList.size() - 1);
        assertThat(testApartman.getAd()).isEqualTo(UPDATED_AD);
        assertThat(testApartman.getKatSayisi()).isEqualTo(UPDATED_KAT_SAYISI);
        assertThat(testApartman.getDaireSayisi()).isEqualTo(UPDATED_DAIRE_SAYISI);
        assertThat(testApartman.getAdres()).isEqualTo(UPDATED_ADRES);
        assertThat(testApartman.getDoluDaireSayisi()).isEqualTo(UPDATED_DOLU_DAIRE_SAYISI);
    }

    @Test
    void putNonExistingApartman() throws Exception {
        int databaseSizeBeforeUpdate = apartmanRepository.findAll().size();
        apartman.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApartmanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, apartman.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apartman))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchApartman() throws Exception {
        int databaseSizeBeforeUpdate = apartmanRepository.findAll().size();
        apartman.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartmanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(apartman))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamApartman() throws Exception {
        int databaseSizeBeforeUpdate = apartmanRepository.findAll().size();
        apartman.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartmanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(apartman)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateApartmanWithPatch() throws Exception {
        // Initialize the database
        apartmanRepository.save(apartman);

        int databaseSizeBeforeUpdate = apartmanRepository.findAll().size();

        // Update the apartman using partial update
        Apartman partialUpdatedApartman = new Apartman();
        partialUpdatedApartman.setId(apartman.getId());

        partialUpdatedApartman.adres(UPDATED_ADRES).doluDaireSayisi(UPDATED_DOLU_DAIRE_SAYISI);

        restApartmanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApartman.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApartman))
            )
            .andExpect(status().isOk());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeUpdate);
        Apartman testApartman = apartmanList.get(apartmanList.size() - 1);
        assertThat(testApartman.getAd()).isEqualTo(DEFAULT_AD);
        assertThat(testApartman.getKatSayisi()).isEqualTo(DEFAULT_KAT_SAYISI);
        assertThat(testApartman.getDaireSayisi()).isEqualTo(DEFAULT_DAIRE_SAYISI);
        assertThat(testApartman.getAdres()).isEqualTo(UPDATED_ADRES);
        assertThat(testApartman.getDoluDaireSayisi()).isEqualTo(UPDATED_DOLU_DAIRE_SAYISI);
    }

    @Test
    void fullUpdateApartmanWithPatch() throws Exception {
        // Initialize the database
        apartmanRepository.save(apartman);

        int databaseSizeBeforeUpdate = apartmanRepository.findAll().size();

        // Update the apartman using partial update
        Apartman partialUpdatedApartman = new Apartman();
        partialUpdatedApartman.setId(apartman.getId());

        partialUpdatedApartman
            .ad(UPDATED_AD)
            .katSayisi(UPDATED_KAT_SAYISI)
            .daireSayisi(UPDATED_DAIRE_SAYISI)
            .adres(UPDATED_ADRES)
            .doluDaireSayisi(UPDATED_DOLU_DAIRE_SAYISI);

        restApartmanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApartman.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApartman))
            )
            .andExpect(status().isOk());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeUpdate);
        Apartman testApartman = apartmanList.get(apartmanList.size() - 1);
        assertThat(testApartman.getAd()).isEqualTo(UPDATED_AD);
        assertThat(testApartman.getKatSayisi()).isEqualTo(UPDATED_KAT_SAYISI);
        assertThat(testApartman.getDaireSayisi()).isEqualTo(UPDATED_DAIRE_SAYISI);
        assertThat(testApartman.getAdres()).isEqualTo(UPDATED_ADRES);
        assertThat(testApartman.getDoluDaireSayisi()).isEqualTo(UPDATED_DOLU_DAIRE_SAYISI);
    }

    @Test
    void patchNonExistingApartman() throws Exception {
        int databaseSizeBeforeUpdate = apartmanRepository.findAll().size();
        apartman.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApartmanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, apartman.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apartman))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchApartman() throws Exception {
        int databaseSizeBeforeUpdate = apartmanRepository.findAll().size();
        apartman.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartmanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(apartman))
            )
            .andExpect(status().isBadRequest());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamApartman() throws Exception {
        int databaseSizeBeforeUpdate = apartmanRepository.findAll().size();
        apartman.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApartmanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(apartman)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Apartman in the database
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteApartman() throws Exception {
        // Initialize the database
        apartmanRepository.save(apartman);

        int databaseSizeBeforeDelete = apartmanRepository.findAll().size();

        // Delete the apartman
        restApartmanMockMvc
            .perform(delete(ENTITY_API_URL_ID, apartman.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Apartman> apartmanList = apartmanRepository.findAll();
        assertThat(apartmanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
