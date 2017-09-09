package me.snegi.cache;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Implementation of a WriteBack Cache
 * Cache allows expiration based on TTL and can be restricted to a max size
 *
 */
public class CacheImpl<K, V>
	implements Cache<K, V> {

	private static final Logger logger = Logger.getLogger("Cache");

	private final AtomicBoolean reapInProgress = new AtomicBoolean(false);

	private final CacheReapingStrategy strategy;
	//Using this sorted set will optimize the time it takes to do the reaping
	//Having Sorted Set increases the memory footprint of this implementation
	private SortedSet<Cacheable> cacheableSet;

	//A Map used to do the Key -> Cacheable lookup
	private Map<K, Cacheable> cacheableMap = new HashMap<K, Cacheable>();
	private Map<K, V> cache = new HashMap<K, V>();

	//TTL to expire a K:V pair
	//TTL is based on the last touch time for that K:V pair
	private final Integer ttlSeconds;

	//The maximum number of K:V pairs in the cache
	private final Long maxSize;

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	private final Lock writeLock = lock.writeLock();

	//Single thread is enough for reaping
	private final ScheduledExecutorService reaperService = Executors.newSingleThreadScheduledExecutor();

	public CacheImpl(
		CacheConfig config
	) {
		this.strategy			= config.getStrategy();
		this.ttlSeconds			= config.getTtlSeconds();
		this.maxSize			= config.getMaxSize();

		switch (this.strategy) {
			case LRU:

				//If its LRU, we will sort the ordered by the last look up time
				this.cacheableSet =
					new TreeSet<Cacheable>(
						new Comparator<Cacheable>() {
							public int compare(Cacheable o1, Cacheable o2) {
								return
									o1.getLastLooked().compareTo(o2.getLastLooked());
							}
						}
					);
				break;

			case LFU:
				throw new RuntimeException("Not Implemented");
			default:
				throw new RuntimeException("Not supported");
		}

		//Schedule a task to periodically reap for the expired entries
		reaperService.scheduleAtFixedRate(
			() -> readExpired(),
			0,
			1,
			TimeUnit.SECONDS
		);
	}

	public V get(K key) {
		V value = cache.get(key);

		if(value != null) {
			touch(key);
		}
		return value;
	}

	public V get(
		K key,
		Function<K, V> loadingFunction
	) {

		V value = get(key);

		if(value != null) {
			return value;
		}

		value = loadingFunction.apply(key);

		if(value != null) {
			set(key, value);
		}

		return value;
	}

	public void evict(K key) {
		writeLock.lock();
		try {
			cache.remove(key);
			Cacheable cacheable = cacheableMap.remove(key);
			cacheableSet.remove(cacheable);
		} finally {
			writeLock.unlock();
		}
	}

	public boolean contains(K key) {
		return
			cache.containsKey(key);
	}

	public V set(K key, V value) {
		touch(key);
		cache.put(key, value);
		checkSizeAndReap();
		return value;
	}

	public void touch(K key) {

		writeLock.lock();

		try {
			Cacheable cacheable = cacheableMap.get(key);
			if (cacheable == null) {
				cacheable =
					new Cacheable(
						key,
						DateTime.now(),
						1L
					);
			} else {
				cacheable = cacheable.touch();
			}

			cacheableMap.put(
				key,
				cacheable
			);
			cacheableSet.add(cacheable);
		} finally {
			writeLock.unlock();
		}
	}

	private void checkSizeAndReap() {
		if(cache.size() > maxSize) {
			reap(cache.size() - maxSize);
		}
	}

	private Long readExpired() {
		DateTime staleTimestamp = DateTime.now().minusSeconds(ttlSeconds);
		Collection<Cacheable<K>> entriesToExpire = new ArrayList<>();

		Iterator<Cacheable> itr = cacheableSet.iterator();
		while(itr.hasNext()) {
			Cacheable<K> cacheable = itr.next();
			if(!cacheable.getLastLooked().isBefore(staleTimestamp)) {
				break;
			}
			entriesToExpire.add(cacheable);
		}

		entriesToExpire.stream()
			.forEach(e -> evict(e.getKey()));

		return
			new Long(entriesToExpire.size());
	}

	private void reap(Long count) {
		if(reapInProgress.compareAndSet(false, true)) {

			readLock.lock();

			try {
				Long expired = readExpired();

				if (expired >= count) {
					return;
				}

				Iterator<Cacheable> itr = cacheableSet.iterator();
				Collection<Cacheable<K>> entriesToExpire = new ArrayList<>();

				Long remaining = count - expired;

				while (itr.hasNext() && remaining > 0) {
					entriesToExpire.add(itr.next());
					remaining--;
				}
				entriesToExpire.stream()
					.forEach(e -> evict(e.getKey()));
			} finally {
				readLock.unlock();
			}
		}
	}

	public void dump() {
		cache.entrySet().stream()
			.forEach(e -> {
				System.out.println("Address "+ e.getKey().toString() + " Tax Rate "+e.getValue());
			});
	}
}
