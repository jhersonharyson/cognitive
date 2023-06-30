package com.cdd.service;


import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import java.time.Duration;

public class CacheManagerService {

    private static final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
    static {
        cacheManager.init();
    }

    public static Cache<String, Object> getCache(String cacheName) {
        Cache<String, Object> cache = cacheManager.getCache(cacheName, String.class, Object.class);
        if (cache == null) {
            cache = cacheManager.createCache(cacheName,
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Object.class,
                            ResourcePoolsBuilder.heap(100)).withExpiry(
                            ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(5))));
        }
        return cache;
    }

    public static void close() {
        cacheManager.close();
    }
}