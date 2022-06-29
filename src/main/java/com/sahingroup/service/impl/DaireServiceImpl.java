package com.sahingroup.service.impl;

import com.sahingroup.domain.Apartman;
import com.sahingroup.domain.Daire;
import com.sahingroup.repository.DaireRepository;
import com.sahingroup.service.DaireService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Daire}.
 */
@Service
public class DaireServiceImpl implements DaireService {

    private final Logger log = LoggerFactory.getLogger(DaireServiceImpl.class);

    private final DaireRepository daireRepository;


    private ApartmanServiceImpl apartmanService;

    public DaireServiceImpl(DaireRepository daireRepository, ApartmanServiceImpl apartmanService) {
        this.daireRepository = daireRepository;
        this.apartmanService = apartmanService;
    }

    @Override
    public Daire save(Daire daire) throws Exception {
        log.debug("Request to save Daire : {}", daire);
        if(daire.getApartmanid().getDaireSayisi() == daire.getApartmanid().getDoluDaireSayisi()){
            throw new Exception("Aparman daireleri dolu");
        }
        else {
            Apartman apartman = daire.getApartmanid();

            apartman.setDoluDaireSayisi(daire.getApartmanid().getDoluDaireSayisi()+1);

            apartmanService.partialUpdate(apartman);
            System.out.println(daire.getApartmanid().getDoluDaireSayisi());
            return daireRepository.save(daire);
        }



    }

    @Override
    public Daire update(Daire daire) {
        log.debug("Request to save Daire : {}", daire);
        return daireRepository.save(daire);
    }

    @Override
    public Optional<Daire> partialUpdate(Daire daire) {
        log.debug("Request to partially update Daire : {}", daire);

        return daireRepository
            .findById(daire.getId())
            .map(existingDaire -> {
                if (daire.getNo() != null) {
                    existingDaire.setNo(daire.getNo());
                }

                return existingDaire;
            })
            .map(daireRepository::save);
    }

    @Override
    public List<Daire> findAll() {
        log.debug("Request to get all Daires");
        return daireRepository.findAllWithEagerRelationships();
    }

    public Page<Daire> findAllWithEagerRelationships(Pageable pageable) {
        return daireRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    public Optional<Daire> findOne(String id) {
        log.debug("Request to get Daire : {}", id);
        return daireRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Daire : {}", id);
        Optional<Daire> daire = daireRepository.findById(id);
        Apartman apartman = daire.get().getApartmanid();
        apartman.setDoluDaireSayisi(daire.get().getApartmanid().getDoluDaireSayisi() - 1);
        apartmanService.partialUpdate(apartman);
        daireRepository.deleteById(id);
    }


}
