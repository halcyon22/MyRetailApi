package org.hierax.myretail.repository;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import org.hierax.myretail.model.Price;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomPriceRepositoryImpl implements CustomPriceRepository {

	private final MongoTemplate mongoTemplate;
	
	@Override
	public Optional<Price> findByProductIdAndCurrency(long productId, LocalDate asOfDate, Currency currency) {
		Query query = new Query();
		query.addCriteria(Criteria.where("productId").is(productId));
		query.addCriteria(Criteria.where("currency").is(currency));
		query.addCriteria(Criteria.where("startDate").lt(asOfDate));
		query.with(new Sort(Sort.Direction.DESC, "startDate"));
		query.limit(1);
		
		List<Price> prices = mongoTemplate.find(query, Price.class);
		Optional<Price> currentPrice = prices.size() > 0 ? Optional.of(prices.get(0)) : Optional.empty();
		
		return currentPrice;
	}

}
