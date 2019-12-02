package com.khorfox.ehcache;

import java.io.File;
import java.time.Duration;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

public class EhCacheManager {

	protected static final String CACHE_PERSISTENCE = "." 
				+ File.separator 
				+ "LocalCache" 
				+ File.separator
				+ "persistance";

	@SuppressWarnings("rawtypes")
	public static Cache buildInMemoryCache(String cacheName, Class<?> ketType, Class<?> valueType){
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .withCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(ketType, valueType,
        ResourcePoolsBuilder.heap(100)))
        .build();

        cacheManager.init();
        return cacheManager.getCache(cacheName, ketType, valueType);
        }
	
	@SuppressWarnings("rawtypes")
	public static Cache buildPersistenceStorageCache(String cacheName, Class<?> ketType, Class<?> valueType){
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .with(CacheManagerBuilder.persistence(CACHE_PERSISTENCE))
        .withCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(ketType, valueType,
        ResourcePoolsBuilder.heap(100)
        .disk(10, MemoryUnit.MB)
        ))
        .build();

        cacheManager.init();
        return cacheManager.getCache(cacheName, ketType, valueType);
        }
	  
	@SuppressWarnings("rawtypes")
	public static Cache buildExpiryEnabledInMemoryCache(String cacheName, Class<?> ketType, Class<?> valueType, int expiryPeriod){
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
        .withCache(cacheName,
        CacheConfigurationBuilder.newCacheConfigurationBuilder(ketType, valueType, ResourcePoolsBuilder.heap(100))
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(expiryPeriod))))
        .build();

        cacheManager.init();
        return cacheManager.getCache(cacheName, ketType, valueType);
    } 
}
