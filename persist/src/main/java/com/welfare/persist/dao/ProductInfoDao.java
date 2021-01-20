package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.ProductInfo;
import com.welfare.persist.mapper.ProductInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 字典(dict)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class ProductInfoDao extends ServiceImpl<ProductInfoMapper, ProductInfo> {

    public String select(List<String> codeList){
        StringBuffer sb = new StringBuffer();
        if (codeList != null && codeList.size() > 1){
            QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.in(ProductInfo.PRODUCT_CODE , codeList);
            List<ProductInfo> productInfos = list(queryWrapper);
            productInfos.forEach(item->{
                sb.append(item.getProductName()).append(" ");
            });
        }
        return sb.toString();
    }

}