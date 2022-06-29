package com.sahingroup.web.rest;

import com.sahingroup.domain.Mesaj;
import com.sahingroup.repository.MesajRepository;
import com.sahingroup.service.MesajService;
import com.sahingroup.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sahingroup.domain.Mesaj}.
 */
@RestController
@RequestMapping("/api")
public class MesajResource {

    private final Logger log = LoggerFactory.getLogger(MesajResource.class);

    private static final String ENTITY_NAME = "mesaj";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MesajService mesajService;

    private final MesajRepository mesajRepository;

    public MesajResource(MesajService mesajService, MesajRepository mesajRepository) {
        this.mesajService = mesajService;
        this.mesajRepository = mesajRepository;
    }

    /**
     * {@code POST  /mesajs} : Create a new mesaj.
     *
     * @param mesaj the mesaj to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mesaj, or with status {@code 400 (Bad Request)} if the mesaj has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mesajs")
    public ResponseEntity<Mesaj> createMesaj(@RequestBody Mesaj mesaj) throws URISyntaxException {
        log.debug("REST request to save Mesaj : {}", mesaj);
        if (mesaj.getId() != null) {
            throw new BadRequestAlertException("A new mesaj cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mesaj result = mesajService.save(mesaj);
        return ResponseEntity
            .created(new URI("/api/mesajs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /mesajs/:id} : Updates an existing mesaj.
     *
     * @param id the id of the mesaj to save.
     * @param mesaj the mesaj to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesaj,
     * or with status {@code 400 (Bad Request)} if the mesaj is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mesaj couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mesajs/{id}")
    public ResponseEntity<Mesaj> updateMesaj(@PathVariable(value = "id", required = false) final String id, @RequestBody Mesaj mesaj)
        throws URISyntaxException {
        log.debug("REST request to update Mesaj : {}, {}", id, mesaj);
        if (mesaj.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesaj.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesajRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Mesaj result = mesajService.update(mesaj);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mesaj.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /mesajs/:id} : Partial updates given fields of an existing mesaj, field will ignore if it is null
     *
     * @param id the id of the mesaj to save.
     * @param mesaj the mesaj to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mesaj,
     * or with status {@code 400 (Bad Request)} if the mesaj is not valid,
     * or with status {@code 404 (Not Found)} if the mesaj is not found,
     * or with status {@code 500 (Internal Server Error)} if the mesaj couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mesajs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Mesaj> partialUpdateMesaj(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Mesaj mesaj
    ) throws URISyntaxException {
        log.debug("REST request to partial update Mesaj partially : {}, {}", id, mesaj);
        if (mesaj.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mesaj.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mesajRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Mesaj> result = mesajService.partialUpdate(mesaj);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mesaj.getId()));
    }

    /**
     * {@code GET  /mesajs} : get all the mesajs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mesajs in body.
     */
    @GetMapping("/mesajs")
    public List<Mesaj> getAllMesajs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Mesajs");
        return mesajService.findAll();
    }

    /**
     * {@code GET  /mesajs/:id} : get the "id" mesaj.
     *
     * @param id the id of the mesaj to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mesaj, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mesajs/{id}")
    public ResponseEntity<Mesaj> getMesaj(@PathVariable String id) {
        log.debug("REST request to get Mesaj : {}", id);
        Optional<Mesaj> mesaj = mesajService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mesaj);
    }

    /**
     * {@code DELETE  /mesajs/:id} : delete the "id" mesaj.
     *
     * @param id the id of the mesaj to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mesajs/{id}")
    public ResponseEntity<Void> deleteMesaj(@PathVariable String id) {
        log.debug("REST request to delete Mesaj : {}", id);
        mesajService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
