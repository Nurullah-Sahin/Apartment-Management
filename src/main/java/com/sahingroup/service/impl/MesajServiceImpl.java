package com.sahingroup.service.impl;

import com.sahingroup.domain.Mesaj;
import com.sahingroup.repository.MesajRepository;
import com.sahingroup.service.MesajService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Mesaj}.
 */
@Service
public class MesajServiceImpl implements MesajService {

    private final Logger log = LoggerFactory.getLogger(MesajServiceImpl.class);

    private final MesajRepository mesajRepository;

    public MesajServiceImpl(MesajRepository mesajRepository) {
        this.mesajRepository = mesajRepository;
    }

    @Override
    public Mesaj save(Mesaj mesaj) {
        log.debug("Request to save Mesaj : {}", mesaj);
        return mesajRepository.save(mesaj);
    }

    @Override
    public Mesaj update(Mesaj mesaj) {
        log.debug("Request to save Mesaj : {}", mesaj);
        return mesajRepository.save(mesaj);
    }

    @Override
    public Optional<Mesaj> partialUpdate(Mesaj mesaj) {
        log.debug("Request to partially update Mesaj : {}", mesaj);

        return mesajRepository
            .findById(mesaj.getId())
            .map(existingMesaj -> {
                if (mesaj.getMesajIcerik() != null) {
                    existingMesaj.setMesajIcerik(mesaj.getMesajIcerik());
                }
                if (mesaj.getAktif() != null) {
                    existingMesaj.setAktif(mesaj.getAktif());
                }

                return existingMesaj;
            })
            .map(mesajRepository::save);
    }

    @Override
    public List<Mesaj> findAll() {
        log.debug("Request to get all Mesajs");
        return mesajRepository.findAllWithEagerRelationships();
    }

    public Page<Mesaj> findAllWithEagerRelationships(Pageable pageable) {
        return mesajRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    public Optional<Mesaj> findOne(String id) {
        log.debug("Request to get Mesaj : {}", id);
        return mesajRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Mesaj : {}", id);
        mesajRepository.deleteById(id);
    }
}
