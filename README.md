
Implements a generic, in-memory, thread-safe cache that supports LRU expiration policy. Can be extended to LFU ( Least Frequently Used )

Implements a SalesTaxLookupService that exposes a Facade for doing the tax rate lookups. SalesTaxLookupService hides the implementation specific details and whether rate is picked from cache of another persistent repository ( aka database )

A few notes -

1. At first i assumbed that sales tax rate would be same in the same zipcode. But thats not true. Same zipcode can have multiple sales tax rates.


2. The caller of SalesTaxLookupService will have to pass a parsed and normalized address ( Address POJO ) in order to look up for the sales tax

3. Cache implementation is thread safe. Refer to the tests CacheImplTest.java and SalesTaxLookupServiceImplTest.java. There is a test that performs load testing in a multithreaded fashion. 100 threads each looking up for 1000 Addresses at the same time.



