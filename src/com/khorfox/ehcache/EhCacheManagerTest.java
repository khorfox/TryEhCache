package com.khorfox.ehcache;


import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;

import org.ehcache.Cache;
import org.junit.jupiter.api.Test;

class EhCacheManagerTest {

	@SuppressWarnings("unchecked")
	@Test
	void whenAccessCacheWithKeyThenGetTheValue() {
		Cache<Integer,String> myCache = EhCacheManager.buildInMemoryCache("InMemoryCache", Integer.class, String.class);
		myCache.put(1, "Try2RecoverThis");
		assertThat((String) myCache.get(1), is("Try2RecoverThis"));
	}

	@SuppressWarnings("unchecked")
	@Test
	void whenAccessPersistentCacheWithKeyThenGetTheValue() {
		Cache<Integer,String> myCache = EhCacheManager.buildPersistenceStorageCache("PersistentCache", Integer.class, String.class);
        IntStream.range(1,5000).boxed().forEach((i) -> {
        myCache.put(i, "Try-"+i);
        });
        assertThat((String) myCache.get(100), is("Try-"+100));
	}
	
	@SuppressWarnings("unchecked")
	@Test
    public void whenAccessInmemoryCacheWithExpiryThenDataShouldExpire() throws InterruptedException{
		int durationOfCache = 2; //are seconds
		Cache<Integer,String> myCache = EhCacheManager.buildExpiryEnabledInMemoryCache("InMemoryTempCache", Integer.class, String.class, durationOfCache);
        myCache.put(1, "Fail2RecoverThis");
        assertThat((String) myCache.get(1), is("Fail2RecoverThis"));
        TimeUnit.SECONDS.sleep(4);
        assertNull(myCache.get(1));
    }


}
