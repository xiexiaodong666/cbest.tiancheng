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
        String departmentCode = sequenceService.nextNo(WelfareConstant.SequenceType.DEPARTMENT_CODE.code()).toString();
        department.setMerCode(merCode);
        department.setDepartmentCode(departmentCode);
        department.setDepartmentName("默认部门");
        department.setDepartmentPath(merCode + "-" + departmentCode);
        department.setDepartmentLevel(2);
        department.setDepartmentParent(merCode);
        department.setDepartmentType(DepartmentTypeEnum.DEPARTMENT.getType());
        return Lists.newArrayList(department);
    }

    @Override
    public List<MerchantAccountType> initMerchantAccountType(String merCode) {
//        MerchantAccountType merchantAccountType2 = new MerchantAccountType();
//        merchantAccountType2.setMerAccountTypeName(WelfareConstant.MerAccountTypeCode.WHOLESALE_PROCUREMENT.desc());
//        merchantAccountType2.setMerAccountTypeCode(WelfareConstant.MerAccountTypeCode.WHOLESALE_PROCUREMENT.code());
//        merchantAccountType2.setMerCode(merCode);
//        merchantAccountType2.setShowStatus(1);
//        merchantAccountType2.setDeductionOrder(887);
//        return Lists.newArrayList(merchantAccountType2);
        return Lists.newArrayList();
    }

    @Override
    public List<SupplierStore> initSupplierStore(String merCode) {
        return new ArrayList<>();
    }

    @Override
    public WelfareConstant.IndustryTag industryTag() {
        return WelfareConstant.IndustryTag.COMMUNITY_HOSPITAL;
    }
}
