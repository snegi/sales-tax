package me.snegi.salestax;

import me.snegi.cache.CacheConfig;
import me.snegi.cache.CacheImpl;
import me.snegi.cache.CacheReapingStrategy;
import me.snegi.dao.TaxLookupDaoImpl;
import me.snegi.model.Address;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 */
public class SalesTaxLookupServiceImplTest {

	private SalesTaxLookupServiceImpl salesTaxLookupService;

	private Random random = new Random();

	@Before
	public void setup() {
		salesTaxLookupService =
			new SalesTaxLookupServiceImpl();

		CacheConfig cacheConfig =
			new CacheConfig.Builder()
				.reapingStrategy(CacheReapingStrategy.LRU)
				.ttlSeconds(500)
				.maxSize(1000L)
				.build();

		salesTaxLookupService.withCache(
			new CacheImpl<Address, Double>(cacheConfig));

		salesTaxLookupService.withDao(
			new TaxLookupDaoImpl()
		);
	}

	@Test
	public void getSalesTaxRate() throws Exception {
		salesTaxLookupService.getSalesTaxRate(getRandomAddress());
	}


	private Address getRandomAddress() {
		return
			new Address(
				random.nextInt(100),
				"",
				"Wilshire Blvd",
				"Los Angeles",
				"CA",
				90001
			);
	}
}