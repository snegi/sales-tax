package me.snegi.salestax;

import me.snegi.cache.Cache;
import me.snegi.dao.SalesTaxRateLookupDao;
import me.snegi.model.Address;

/**
 */
public class SalesTaxLookupServiceImpl
	implements SalesTaxLookupService {

	private Cache<Address, Double> cache;
	private SalesTaxRateLookupDao dao;


	public Double getSalesTaxRate(Address address) {
		return
			cache.get(
				address,
				(addr) -> dao.getSalesTax(addr));
	}

	public void withCache(Cache cache) {
		this.cache = cache;
	}

	public void withDao(SalesTaxRateLookupDao dao) {
		this.dao = dao;
	}
}
