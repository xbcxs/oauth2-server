package com.xbcxs.server.authorize;

import org.springframework.stereotype.Component;

import java.util.Hashtable;

/**
 * Created by xiaosh on 2019/8/5.
 */
@Component
public class CodeServiceImpl implements CodeService {

    private Hashtable map = new Hashtable();

    /**
     *  CODE超期时间
     */
    private int EXPIRES_TIME = 10 * 60 * 1000;

    @Override
    public Hashtable save(String id) {
        map.put(id, id);
        return map;
    }

    @Override
    public boolean remove(String id) {
        map.remove(id);
        return true;
    }

    @Override
    public boolean exist(String id) {
        return map.containsKey(id);
    }

    @Override
    public boolean expires() {
        // TODO 手自动刷新并Evict过期对象
        return false;
    }

}
