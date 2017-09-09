package me.snegi.cache;

/**
 * Config for the {@link Cache}
 */
public class CacheConfig {

	private CacheReapingStrategy strategy;

	//TTL to expire a K:V pair
	//TTL is based on the last touch time for that K:V pair
	private Integer ttlSeconds;

	//The maximum number of K:V pairs in the cache
	private Long maxSize;

	public CacheReapingStrategy getStrategy() {
		return strategy;
	}

	public Integer getTtlSeconds() {
		return ttlSeconds;
	}

	public Long getMaxSize() {
		return maxSize;
	}

	private boolean isValid() {
		return
			ttlSeconds !=  null && ttlSeconds > 0
			&& maxSize != null && maxSize > 0;
	}

	public static class Builder {
		private CacheConfig config;

		public Builder() {
			config = new CacheConfig();
		}

		public Builder ttlSeconds(Integer val) {
			config.ttlSeconds = val;
			return this;
		}

		public Builder maxSize(Long val) {
			config.maxSize = val;
			return this;
		}
		public Builder reapingStrategy(CacheReapingStrategy reapingStrategy) {
			config.strategy  = reapingStrategy;
			return this;
		}
		public CacheConfig build() {
			if(!config.isValid()) {
				throw new RuntimeException("Invalid Config");
			}
			return config;
		}

	}
}
