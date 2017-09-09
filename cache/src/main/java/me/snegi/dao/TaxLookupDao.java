package me.snegi.dao;

import me.snegi.model.Address;

/**
 */
public interface TaxLookupDao {

	Double getSalesTax(Address address);
}
