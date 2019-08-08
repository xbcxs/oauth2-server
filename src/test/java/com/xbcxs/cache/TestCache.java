package com.xbcxs.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by xiaosh on 2019/8/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class TestCache {
    @Resource
    private CacheManager cacheManager;

    @Test
    public void cacheTest() {
        // 显示所有的Cache空间
        System.out.println(cacheManager.getCacheNames());
        Cache cache = cacheManager.getCache("userCache");
        cache.put("key", "123");
        System.out.println("缓存成功");
        String res = cache.get("key", String.class);
        System.out.println(res);
    }
   /* AuthCache
    code
    code_id    默认过期时间 10S   dataType(code,access_token,refresh_token)  授权ID
    atoken_id   默认过期时间 2小时  对应的rtoken
    rtoken_id  默认过期时间 30天*/

}

