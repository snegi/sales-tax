package me.snegi.cache;

import me.snegi.model.Address;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A ThreadSafe implementation of {@link Cache}
 * This implementation optimizes for both the fetch
 * as well as the Cache expiration/reap
 */
public class CacheImplTest {

	private Random random = new Random();

	private Cache<Address, Double> cache;

	@Before
	public void setup() {
		cache =
			new CacheImpl<Address, Double>(
				new CacheConfig.Builder()
					.reapingStrategy(CacheReapingStrategy.LRU)
					.ttlSeconds(500)
					.maxSize(1000L)
					.build());
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

	private Double getRandomTaxRate(Address address) {
		//Between [0 - 10)
		return 10 * random.nextDouble();
	}

	@Test
	public void get() throws Exception {
		cache.get(getRandomAddress(), (address) -> getRandomTaxRate(address));

		((CacheImpl)cache).dump();
	}

	@Test
	public void loadTest() throws Exception {
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		//Starting 100 threads each looking up 1000 Addresses
		for(int task=0; task < 100; task++) {
			Long startTs = System.currentTimeMillis();
			executorService.submit(() -> {
				for (int i = 0; i < 1000; i++) {
					cache.get(getRandomAddress(), (address) -> getRandomTaxRate(address));
				}
			});
			System.out.println("Task "+task+ " took "+(System.currentTimeMillis() - startTs) + "ms");
		}

		Thread.sleep(2000);
		((CacheImpl)cache).dump();
	}

	@Test
	public void evict() throws Exception {

	}

	@Test
	public void contains() throws Exception {

	}

	@Test
	public void set() throws Exception {

	}

	@Test
	public void touch() throws Exception {

	}

}