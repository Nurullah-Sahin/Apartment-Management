package com.sahingroup.repository;

import com.sahingroup.domain.Mesaj;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Mesaj entity.
 */
@Repository
public interface MesajRepository extends MongoRepository<Mesaj, String> {
    @Query("{}")
    Page<Mesaj> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Mesaj> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Mesaj> findOneWithEagerRelationships(String id);
}
