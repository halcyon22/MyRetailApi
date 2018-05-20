package org.hierax.myretail.repository;

import org.hierax.myretail.model.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * CRUD for product prices.
 */
public interface PriceRepository extends MongoRepository<Price, Long>, CustomPriceRepository {

}
