package me.snegi.cache;

import java.util.function.Function;

/**
 * A generic Cache Interface
 * Possible implementation of a Cache could use
 * --> Least Recently Used reaping
 * --> Leader Frequently Used reaping
 * --> etc
 */
public interface Cache<K,V> {

	/**
	 * Fetches value for a key from the cache
	 * @param key cache key
	 * @return Returns the value or null
	 */
	V get(K key);

	/**
	 * Fetches value for a key from the cache
	 * If the key is not present in the cache,
	 * then the provided loadingFunction is used to
	 * fetch the value. Value fetched from the loadingFunction
	 * is written back to the cache
	 * @param key
	 * @param loadingFunction
	 * @return
	 */
	V get(K key, Function<K, V> loadingFunction);

	void evict(K key);

	boolean contains(K key);

	V set(K key, V value);

}
