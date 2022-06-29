package com.sahingroup.repository;

import com.sahingroup.domain.Daire;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Daire entity.
 */
@Repository
public interface DaireRepository extends MongoRepository<Daire, String> {
    @Query("{}")
    Page<Daire> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Daire> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Daire> findOneWithEagerRelationships(String id);
}
