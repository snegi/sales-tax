package me.snegi.salestax;

import me.snegi.model.Address;

/**
 * Interface for a Facade for looking up sales tax
 * Implementation of this service hides whether the rate is picked up from the cache
 * or database
 */
public interface SalesTaxLookupService {

	Double getSalesTaxRate(Address address);
}
