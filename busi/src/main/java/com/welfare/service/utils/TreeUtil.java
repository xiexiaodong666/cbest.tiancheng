package com.welfare.service.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.welfare.common.util.EmptyChecker;
import com.welfare.service.dto.Tree;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 构造树形结构通用工具类
 * Created by hao.yin on 2021/1/9.
 */
public class TreeUtil<T extends Tree> {
    private List<T> rootList; //根节点对象存放到这里
    private List<T> bodyList;//其它节点，包含根节点

    /**
     *
     * @param bodyList
     * @param rootParentCode 根节点的父节点值
     */
    public TreeUtil(List<T> bodyList,String rootParentCode) {
        if(rootParentCode==null){
            this.rootList = bodyList.stream().filter(item->item.getParentCode()==null).collect(Collectors.toList());
        }else{
            this.rootList = bodyList.stream().filter(item->rootParentCode.equals(item.getParentCode())).collect(Collectors.toList());
        }
        this.bodyList = bodyList;
    }

    public List<T> getTree(){   //调用的方法入口

        if(bodyList != null && !bodyList.isEmpty()){
            //声明一个map，用来过滤已操作过的数据
            Map<String,String> map = Maps.newHashMapWithExpectedSize(bodyList.size());
            rootList.forEach(beanTree -> getChild(beanTree,map));//传递根对象和一个空map
            return rootList;
        }
        return null;
    }

    public void getChild(T beanTree, Map<String,String> map){
        List<T> childList = Lists.newArrayList();
        bodyList.stream()
                .filter(c -> !map.containsKey(c.getCode()))//map内不包含子节点的code
                .filter(c ->c.getParentCode().equals(beanTree.getCode()))//子节点的父id==根节点的code 继续循环
                .forEach(c ->{
                    map.put(c.getCode(),c.getParentCode());//当前节点code和父节点id
                    getChild(c,map);//递归调用
                    childList.add(c);
                });
        if(EmptyChecker.notEmpty(childList)){
            beanTree.setChildren(childList);
        }
    }
}
