package com.xbcxs.server.authorize;

import java.util.Hashtable;

/**
 * Created by xiaosh on 2019/8/5.
 */
public interface CodeService {

    Hashtable save(String id);

    boolean remove(String id);

    boolean exist(String id);

    boolean expires();

}
