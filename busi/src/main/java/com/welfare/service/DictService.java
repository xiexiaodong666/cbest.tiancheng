package com.welfare.service;


import com.welfare.persist.entity.Dict;
import com.welfare.service.dto.DictDTO;
import com.welfare.service.dto.DictReq;

import java.util.List;
import java.util.Map;

/**
 * 字典服务接口
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface DictService {

    List<DictDTO> getByType(DictReq dictType);
    public Map<String, Map<String,List<Dict>>> getDictMap(List<String> typeList);

    /**
     *  通用字典码转字典值方法（注意：dictType必须为实体类名.属性名，例如 Merchant.merIdentity 代表商户身份属性）
     * @param c 待转换类Class对象
     * @param className 如果待转换的类不是实体类，需要传入实体类的类名
     * @param nameFlag 默认转换后的值会覆盖之前的字段，如果要用新的字段来接受，字段名约定为原字段名+Name,且nameFlag=true
     * @param <T> 待转换的类
     * @param objs 待转换类
     */
     <T> void trans(  Class c,String className,boolean nameFlag,T... objs);
}
