package com.welfare.service.init.merchant;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.service.AbstractMerchantInitOperator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/20 12:43 下午
 */
@Component
public class MerchantInitOperatorFactory {

    @Autowired
    private List<AbstractMerchantInitOperator> operators;

    public List<AbstractMerchantInitOperator> operators(List<String> industryTags){
        List<AbstractMerchantInitOperator> result = new ArrayList<>();
        for(AbstractMerchantInitOperator operator : operators){
            if (CollectionUtils.isNotEmpty(industryTags) && industryTags.contains(operator.industryTag().code())) {
                result.add(operator);
            }
        }
        return result;
    }
}
