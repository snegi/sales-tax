package me.snegi.dao;

import me.snegi.model.Address;

/**
 */
public interface SalesTaxRateLookupDao {

	Double getSalesTax(Address address);
}
