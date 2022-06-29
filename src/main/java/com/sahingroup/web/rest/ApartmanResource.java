package com.sahingroup.web.rest;

import com.sahingroup.domain.Apartman;
import com.sahingroup.repository.ApartmanRepository;
import com.sahingroup.service.ApartmanService;
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
 * REST controller for managing {@link com.sahingroup.domain.Apartman}.
 */
@RestController
@RequestMapping("/api")
public class ApartmanResource {

    private final Logger log = LoggerFactory.getLogger(ApartmanResource.class);

    private static final String ENTITY_NAME = "apartman";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApartmanService apartmanService;

    private final ApartmanRepository apartmanRepository;

    public ApartmanResource(ApartmanService apartmanService, ApartmanRepository apartmanRepository) {
        this.apartmanService = apartmanService;
        this.apartmanRepository = apartmanRepository;
    }

    /**
     * {@code POST  /apartmen} : Create a new apartman.
     *
     * @param apartman the apartman to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new apartman, or with status {@code 400 (Bad Request)} if the apartman has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/apartmen")
    public ResponseEntity<Apartman> createApartman(@RequestBody Apartman apartman) throws URISyntaxException {
        log.debug("REST request to save Apartman : {}", apartman);
        if (apartman.getId() != null) {
            throw new BadRequestAlertException("A new apartman cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Apartman result = apartmanService.save(apartman);
        return ResponseEntity
            .created(new URI("/api/apartmen/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /apartmen/:id} : Updates an existing apartman.
     *
     * @param id the id of the apartman to save.
     * @param apartman the apartman to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apartman,
     * or with status {@code 400 (Bad Request)} if the apartman is not valid,
     * or with status {@code 500 (Internal Server Error)} if the apartman couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/apartmen/{id}")
    public ResponseEntity<Apartman> updateApartman(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Apartman apartman
    ) throws URISyntaxException {
        log.debug("REST request to update Apartman : {}, {}", id, apartman);
        if (apartman.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apartman.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apartmanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Apartman result = apartmanService.update(apartman);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apartman.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /apartmen/:id} : Partial updates given fields of an existing apartman, field will ignore if it is null
     *
     * @param id the id of the apartman to save.
     * @param apartman the apartman to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated apartman,
     * or with status {@code 400 (Bad Request)} if the apartman is not valid,
     * or with status {@code 404 (Not Found)} if the apartman is not found,
     * or with status {@code 500 (Internal Server Error)} if the apartman couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/apartmen/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Apartman> partialUpdateApartman(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Apartman apartman
    ) throws URISyntaxException {
        log.debug("REST request to partial update Apartman partially : {}, {}", id, apartman);
        if (apartman.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, apartman.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!apartmanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Apartman> result = apartmanService.partialUpdate(apartman);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, apartman.getId())
        );
    }

    /**
     * {@code GET  /apartmen} : get all the apartmen.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of apartmen in body.
     */
    @GetMapping("/apartmen")
    public List<Apartman> getAllApartmen() {
        log.debug("REST request to get all Apartmen");
        return apartmanService.findAll();
    }

    /**
     * {@code GET  /apartmen/:id} : get the "id" apartman.
     *
     * @param id the id of the apartman to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the apartman, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/apartmen/{id}")
    public ResponseEntity<Apartman> getApartman(@PathVariable String id) {
        log.debug("REST request to get Apartman : {}", id);
        Optional<Apartman> apartman = apartmanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(apartman);
    }

    /**
     * {@code DELETE  /apartmen/:id} : delete the "id" apartman.
     *
     * @param id the id of the apartman to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/apartmen/{id}")
    public ResponseEntity<Void> deleteApartman(@PathVariable String id) {
        log.debug("REST request to delete Apartman : {}", id);
        apartmanService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
