package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.dao.DictDao;
import com.welfare.persist.entity.Dict;
import com.welfare.service.converter.DictConverter;
import com.welfare.service.dto.DictDTO;
import com.welfare.service.dto.DictReq;
import com.welfare.service.dto.FieldMethodDTO;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.DictService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DictServiceImpl implements DictService {
    private final DictDao dictDao;
    private final DictConverter dictConverter;

    @Override
    public List<DictDTO> getByType(DictReq req) {
        return dictConverter.toD(dictDao.list(QueryHelper.getWrapper(req)));
    }

    @Override
    public Map<String, Map<String, List<Dict>>> getDictMap(List<String> typeList) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(Dict.DICT_TYPE, typeList);
        List<Dict> dicts = dictDao.list(queryWrapper);
        if (EmptyChecker.isEmpty(dicts)) {
            return null;
        }
        //List<Dict>集合，业务上来说只有一个。如果不止一个，请修改code
        Map<String, Map<String, List<Dict>>> map = dicts.stream().collect(Collectors.groupingBy(Dict::getDictType, Collectors.groupingBy(Dict::getDictCode)));
        return map;
    }
    @Override
    public  <T> void trans(  Class c,String className,boolean nameFlag,
                             List<T>objs) {
        if(EmptyChecker.isEmpty(className)){
            className=c.getSimpleName();
        }
        Field[] fields = c.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>(fields.length);
        for (int i = 0; i < fields.length; i++) {
            fieldNames.add(className + "." + fields[i].getName());
        }
        Map<String, Map<String, List<Dict>>> map = getDictMap(fieldNames);

        if (EmptyChecker.isEmpty(map)) {
            return;
        }
        List<FieldMethodDTO> fieldMethodList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            // 迭代每一个属性
            String field=fields[i].getName();
            String dictType = className + "."+fields[i].getName();
            Class type=fields[i].getType();
            if (!map.keySet().contains(dictType)) {
                continue;
            }
            // 拼装出get方法名
            String methodName=String.valueOf(field.charAt(0)).toUpperCase() + field.substring(1)+(nameFlag?"Name":"");
            String methodNameGet = "get" + methodName;
            String methodNameSet = "set" + methodName;

            // 通过方法名取得方法对象
            try {
                fieldMethodList.add(FieldMethodDTO.builder()
                        .dictType(dictType)
                        .gMethod(c.getMethod(methodNameGet))
                        .sMethod(c.getMethod(methodNameSet,type))
                        .type(type)
                        .build());
            } catch (NoSuchMethodException e) {
                throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "枚举转换错误", e);
            }
        }
        for (T t : objs) {
            for (FieldMethodDTO fieldMethod : fieldMethodList) {
                try {
                    Object dictCode = fieldMethod.getGMethod().invoke(t);
                    String dictName= null;
                    try {
                        dictName = map.get(fieldMethod.getDictType()).get(dictCode).get(0).getDictName();
                    } catch (Exception e) {
                        log.info("type=【{}】,code=【{}】该字典码不存在",fieldMethod.getDictType(),dictCode);
                        continue;
                    }
                    if(EmptyChecker.notEmpty(dictName)&&fieldMethod.getType().equals(Integer.class)){
                        fieldMethod.getSMethod().invoke(t,Integer.parseInt(dictName));
                    }else{
                        fieldMethod.getSMethod().invoke(t, dictName);
                    }
                } catch (IllegalAccessException e) {
                    throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "枚举转换错误", e);
                } catch (InvocationTargetException e) {
                    throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "枚举转换错误", e);
                }
            }

        }

    }

}