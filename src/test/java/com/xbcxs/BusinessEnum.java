package com.xbcxs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaosh
 * @date 2019/9/6
 */
public enum BusinessEnum {

    A("1", "小一", "14"), B("1", "小一", "14"), C("1", "小一", "14") ;

    private static Logger log = LoggerFactory.getLogger(BusinessEnum.class);

    private String id;
    private String name;
    private String age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    BusinessEnum(String id, String name, String age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public static void main(String[] args){
        // 去枚举对象值，极其属性
        log.info(BusinessEnum.A.name);
        log.info(BusinessEnum.valueOf("A").name);
    }
}
