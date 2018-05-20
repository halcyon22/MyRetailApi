package org.hierax.myretail.repository;

import java.util.Date;
import java.util.List;

import org.hierax.myretail.model.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PriceRepository extends MongoRepository<Price, Long>, CustomPriceRepository {

	@Query
	public List<Price> findByProductIdAndStartDateLessThan(Date startDate);
	
}
