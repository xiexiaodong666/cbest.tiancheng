package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.dao.DictDao;
import com.welfare.persist.entity.Dict;
import com.welfare.service.DictService;
import com.welfare.service.converter.DictConverter;
import com.welfare.service.dto.DictDTO;
import com.welfare.service.dto.DictReq;
import com.welfare.service.dto.FieldMethodDTO;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典服务接口实现
 *
 * @author hao.yin
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
        QueryWrapper<Dict> q=QueryHelper.getWrapper(req);
        q.orderByAsc(Dict.SORT);
        return dictConverter.toD(dictDao.list(q));
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

    public static void main(String[] args) {

    }
    @Override
    public  <T> void trans(  Class c,String className,boolean nameFlag,
                             T... objs) {
        if(EmptyChecker.isEmpty(objs)){
            return;
        }
        List<T> list=Arrays.asList(objs);

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
            String methodName=String.valueOf(field.charAt(0)).toUpperCase() + field.substring(1);
            String methodNameGet = "get" + methodName;
            String methodNameSet = "set" + methodName+(nameFlag?"Name":"");

            // 通过方法名取得方法对象
            try {
                fieldMethodList.add(FieldMethodDTO.builder()
                        .dictType(dictType)
                        .gMethod(c.getMethod(methodNameGet))
                        .sMethod(c.getMethod(methodNameSet,type))
                        .type(type)
                        .build());
            } catch (NoSuchMethodException e) {
                throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "枚举转换错误", e);
            }
        }
        for (T t : list) {
            for (FieldMethodDTO fieldMethod : fieldMethodList) {
                try {
                    Map<String, List<Dict>> dictMap = map.get(fieldMethod.getDictType());
                    Object dictCode = fieldMethod.getGMethod().invoke(c.cast(t));
                    if (EmptyChecker.isEmpty(dictCode)) {
                        continue;
                    }
                    String[] dictCodeArr = dictCode.toString().split(",");
                    List<String> dictNameList = new ArrayList();
                    for (String item : dictCodeArr) {
                        List<Dict> dicts = dictMap.get(item);
                        if (EmptyChecker.isEmpty(dicts)) {
                            continue;
                        }
                        dictNameList.add(dicts.get(0).getDictName());
                    }
                    String dictName = StringUtils.join(dictNameList,",");
                    if(EmptyChecker.notEmpty(dictName)&&fieldMethod.getType().equals(Integer.class)){
                        fieldMethod.getSMethod().invoke(c.cast(t),Integer.parseInt(dictName));
                    }else{
                        fieldMethod.getSMethod().invoke(c.cast(t), dictName);
                    }
                } catch (IllegalAccessException e) {
                    throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "枚举转换错误", e);
                } catch (InvocationTargetException e) {
                    throw new BizException(ExceptionCode.UNKNOWON_EXCEPTION, "枚举转换错误", e);
                }
            }

        }

    }

}