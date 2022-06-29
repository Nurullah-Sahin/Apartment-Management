package com.sahingroup.service.impl;

import com.sahingroup.domain.Apartman;
import com.sahingroup.repository.ApartmanRepository;
import com.sahingroup.repository.UserRepository;
import com.sahingroup.service.ApartmanService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Apartman}.
 */
@Service
public class ApartmanServiceImpl implements ApartmanService {

    private final Logger log = LoggerFactory.getLogger(ApartmanServiceImpl.class);

    private final ApartmanRepository apartmanRepository;

    private final UserRepository userRepository;

    public ApartmanServiceImpl(ApartmanRepository apartmanRepository, UserRepository userRepository) {
        this.apartmanRepository = apartmanRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Apartman save(Apartman apartman) {
        log.debug("Request to save Apartman : {}", apartman);
        apartman.setDoluDaireSayisi(0);
        return apartmanRepository.save(apartman);
    }

    @Override
    public Apartman update(Apartman apartman) {
        log.debug("Request to save Apartman : {}", apartman);
        return apartmanRepository.save(apartman);
    }

    @Override
    public Optional<Apartman> partialUpdate(Apartman apartman) {
        log.debug("Request to partially update Apartman : {}", apartman);

        return apartmanRepository
            .findById(apartman.getId())
            .map(existingApartman -> {
                if (apartman.getAd() != null) {
                    existingApartman.setAd(apartman.getAd());
                }
                if (apartman.getKatSayisi() != null) {
                    existingApartman.setKatSayisi(apartman.getKatSayisi());
                }
                if (apartman.getDaireSayisi() != null) {
                    existingApartman.setDaireSayisi(apartman.getDaireSayisi());
                }
                if (apartman.getAdres() != null) {
                    existingApartman.setAdres(apartman.getAdres());
                }
                if (apartman.getDoluDaireSayisi() != null) {
                    existingApartman.setDoluDaireSayisi(apartman.getDoluDaireSayisi());
                }

                return existingApartman;
            })
            .map(apartmanRepository::save);
    }

    @Override
    public List<Apartman> findAll() {
        log.debug("Request to get all Apartmen");

        return apartmanRepository.findAll();
    }

    @Override
    public Optional<Apartman> findOne(String id) {
        log.debug("Request to get Apartman : {}", id);
        return apartmanRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Apartman : {}", id);
        apartmanRepository.deleteById(id);
    }
}
