package com.welfare.service;

import com.welfare.common.constants.WelfareConstant;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/16 11:34 上午
 */
public interface AccountSpecialOperation {

    void operate();

    WelfareConstant.IndustryTag industryTag();
}
