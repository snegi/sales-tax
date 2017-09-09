package me.snegi.salestax;

import me.snegi.cache.Cache;
import me.snegi.dao.TaxLookupDao;
import me.snegi.model.Address;

/**
 */
public class SalesTaxLookupServiceImpl
	implements SalesTaxLookupService {

	private Cache<Address, Double> cache;
	private TaxLookupDao dao;


	public Double getSalesTaxRate(Address address) {
		return
			cache.get(
				address,
				(addr) -> dao.getSalesTax(addr));
	}

	public void withCache(Cache cache) {
		this.cache = cache;
	}

	public void withDao(TaxLookupDao dao) {
		this.dao = dao;
	}
}
