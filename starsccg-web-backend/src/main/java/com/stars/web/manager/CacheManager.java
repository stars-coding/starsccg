package com.stars.web.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理器
 *
 * @author stars
 * @version 1.0.0
 */
@Component
public class CacheManager {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 本地缓存
     */
    Cache<String, Object> localCache = Caffeine.newBuilder()
            .expireAfterWrite(100, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        this.localCache.put(key, value);
        this.redisTemplate.opsForValue().set(key, value, 100, TimeUnit.MINUTES);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        // 先从本地缓存中获取
        Object value = this.localCache.getIfPresent(key);
        if (value != null) {
            return value;
        }

        // 本地缓存未命中，尝试从 Redis 获取
        value = this.redisTemplate.opsForValue().get(key);
        if (value != null) {
            // 将 Redis 的值写入到本地缓存
            this.localCache.put(key, value);
        }

        return value;
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public void delete(String key) {
        this.localCache.invalidate(key);
        this.redisTemplate.delete(key);
    }
}
