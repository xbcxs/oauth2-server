package com.xbcxs;

import java.util.*;

/**
 * @author xiaosh
 * @date 2019/9/10
 */
public class BuildTreeTest extends HashMap{

    @Override
    public Object put(Object key, Object value) {

        return super.put(key, value);
    }



    final String a = "1";

    public static void main(String[] args){
        // 所有节点(不含有顶层节点)
        List<Map> resultData = null;
        // 顶层节点
        List<Map> topNode = null;
        for(Map parentMap: topNode){
            BuildTreeTest.buildTree(parentMap, resultData);
        }
    }

    public static void buildTree(Map parentMap, List<Map> resultData){
        String parentId = String.valueOf(parentMap.get("id"));
        Iterator<Map> iter = resultData.iterator();
        while (iter.hasNext()) {
            Map sonMap = iter.next();
            // 找到子节点
            if(parentId.equals(sonMap.get("parentId"))){
                List<Map> childrenList;
                if(parentMap.get("children") == null){
                    childrenList = new ArrayList<Map>();
                    parentMap.put("children", childrenList);
                } else {
                    childrenList = (List<Map>)parentMap.get("children");
                }
                // Map sonMapCopy = sonMap
                /*childrenList.add(sonMapCopy);
                buildTree(sonMapCopy,  resultData);
                iter.remove();*/
            }
        }

    }
}
