package com.sahingroup.repository;

import com.sahingroup.domain.Apartman;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Apartman entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApartmanRepository extends MongoRepository<Apartman, String> {}
