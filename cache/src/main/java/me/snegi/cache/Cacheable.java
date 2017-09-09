package me.snegi.cache;

import org.joda.time.DateTime;

/**
 */
public class Cacheable<K> {

	private K key;
	private DateTime lastLooked;
	private Long totalLookupCount;

	public Cacheable(
		K key,
		DateTime lastLooked,
		Long totalLookupCount
	) {
		this.key				= key;
		this.lastLooked			= lastLooked;
		this.totalLookupCount	= totalLookupCount;
	}

	public Cacheable touch() {
		return
			new Cacheable(
				key,
				DateTime.now(),
				totalLookupCount+1
			);
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return key.equals(o);
	}

	public K getKey() {
		return key;
	}

	public DateTime getLastLooked() {
		return lastLooked;
	}

	public Long getTotalLookupCount() {
		return totalLookupCount;
	}
}
