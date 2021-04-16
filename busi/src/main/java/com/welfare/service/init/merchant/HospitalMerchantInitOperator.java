package com.welfare.service.init.merchant;

import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.DepartmentTypeEnum;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.AbstractMerchantInitOperator;
import com.welfare.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/16 4:16 下午
 */
@Component
@Slf4j
public class HospitalMerchantInitOperator extends AbstractMerchantInitOperator {

    @Autowired
    private SequenceService sequenceService;

    @Override
    public List<AccountType> initAccountType(String merCode) {
        AccountType accountType1 = new AccountType();
        accountType1.setMerCode(merCode);
        accountType1.setTypeCode(WelfareConstant.AccountType.DOCTOR.code());
        accountType1.setTypeName(WelfareConstant.AccountType.DOCTOR.desc());

        AccountType accountType2 = new AccountType();
        accountType2.setMerCode(merCode);
        accountType2.setTypeCode(WelfareConstant.AccountType.PURCHASER.code());
        accountType2.setTypeName(WelfareConstant.AccountType.PURCHASER.desc());

        AccountType accountType3 = new AccountType();
        accountType3.setMerCode(merCode);
        accountType3.setTypeCode(WelfareConstant.AccountType.COMMON_USER.code());
        accountType3.setTypeName(WelfareConstant.AccountType.COMMON_USER.desc());

        AccountType accountType4 = new AccountType();
        accountType4.setMerCode(merCode);
        accountType4.setTypeCode(WelfareConstant.AccountType.PATIENT.code());
        accountType4.setTypeName(WelfareConstant.AccountType.PATIENT.desc());
        return Lists.newArrayList(accountType1, accountType2, accountType3, accountType4);
    }

    @Override
    public List<Department> initDepartment(String merCode) {
        Department department = new Department();
        department.setMerCode(merCode);
        department.setMerCode("卫计委默认部门");
        String departmentCode = sequenceService.nextNo(WelfareConstant.SequenceType.DEPARTMENT_CODE.code()).toString();
        department.setDepartmentCode(departmentCode);
        department.setDepartmentParent(merCode);
        department.setDepartmentLevel(2);
        department.setDepartmentPath(merCode + "-" +departmentCode);
        department.setDepartmentType(DepartmentTypeEnum.DEPARTMENT.getType());
        return Lists.newArrayList(department);
    }

    @Override
    public List<MerchantAccountType> initMerchantAccountType(String merCode) {
        MerchantAccountType merchantAccountType1 = new MerchantAccountType();
        merchantAccountType1.setMerAccountTypeName(WelfareConstant.MerAccountTypeCode.MALL_POINT.name());
        merchantAccountType1.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        merchantAccountType1.setMerCode(merCode);
        merchantAccountType1.setShowStatus(1);
        merchantAccountType1.setDeductionOrder(888);

        MerchantAccountType merchantAccountType2 = new MerchantAccountType();
        merchantAccountType2.setMerAccountTypeName(WelfareConstant.MerAccountTypeCode.WHOLESALE.name());
        merchantAccountType2.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.WHOLESALE.code());
        merchantAccountType2.setMerCode(merCode);
        merchantAccountType2.setShowStatus(1);
        merchantAccountType2.setDeductionOrder(887);
        return Lists.newArrayList(merchantAccountType1, merchantAccountType2);
    }

    @Override
    public List<SupplierStore> initSupplierStore(String merCode) {
        // 自营门店
        return new ArrayList<>();
    }

    @Override
    public List<WelfareConstant.IndustryTag> industryTag() {
        return Lists.newArrayList(WelfareConstant.IndustryTag.COMMUNITY_HOSPITAL);
    }
}
