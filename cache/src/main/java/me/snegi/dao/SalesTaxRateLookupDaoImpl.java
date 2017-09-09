package me.snegi.dao;

import me.snegi.model.Address;

import java.util.Random;

/**
 */
public class SalesTaxRateLookupDaoImpl
	implements SalesTaxRateLookupDao {

	private final Random random = new Random();

	public Double getSalesTax(Address address) {
		//Expensive look up
		return
			getRandomTaxRate();
	}

	private Double getRandomTaxRate() {
		//Between [0 - 10)
		return 10 * random.nextDouble();
	}
}
