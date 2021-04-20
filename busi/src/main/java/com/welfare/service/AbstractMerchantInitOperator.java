package com.welfare.service;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.AccountTypeDao;
import com.welfare.persist.dao.DepartmentDao;
import com.welfare.persist.dao.MerchantAccountTypeDao;
import com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.persist.entity.SupplierStore;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/16 11:34 上午
 */
public abstract class AbstractMerchantInitOperator {

    @Autowired
    protected AccountTypeDao accountTypeDao;
    @Autowired
    protected DepartmentDao departmentDao;
    @Autowired
    protected MerchantAccountTypeDao merchantAccountTypeDao;
    @Autowired
    protected SupplierStoreDao supplierStoreDao;
    @Autowired
    protected ApplicationContext applicationContext;


    @Transactional(rollbackFor = Exception.class)
    public void init(String merCode) {
        doInitAccountType(merCode);
        doInitDepartment(merCode);
        doInitMerchantAccountType(merCode);
        doInitSupplierStore(merCode);
    }

    private void doInitSupplierStore(String merCode) {
        List<SupplierStore> supplierStores = initSupplierStore(merCode);
        if (CollectionUtils.isNotEmpty(supplierStores)) {
            supplierStoreDao.saveBatch(supplierStores);
        }
    }

    private void doInitAccountType(String merCode) {
        List<AccountType> accountTypes = initAccountType(merCode);
        if (CollectionUtils.isNotEmpty(accountTypes)) {
            accountTypeDao.saveBatch(accountTypes);
        }
    }

    private void doInitDepartment(String merCode) {
        List<Department> department = initDepartment(merCode);
        if (CollectionUtils.isNotEmpty(department)) {
            departmentDao.saveBatch(department);
        }
    }

    private void doInitMerchantAccountType(String merCode) {
        List<MerchantAccountType> merchantAccountTypes = initMerchantAccountType(merCode);
        if (CollectionUtils.isNotEmpty(merchantAccountTypes)) {
            merchantAccountTypeDao.saveBatch(merchantAccountTypes);
        }
    }

    /**
     * 需要初始化的员工类型
     * @param merCode
     * @return
     */
    public abstract List<AccountType> initAccountType(String merCode);

    /**
     * 需要初始化的组织机构
     * @param merCode
     * @return
     */
    public abstract List<Department> initDepartment(String merCode);

    /**
     * 需要初始化的福利类型
     * @param merCode
     * @return
     */
    public abstract List<MerchantAccountType> initMerchantAccountType(String merCode);

    /**
     * 需要初始化的门店
     * @param merCode
     * @return
     */
    public abstract List<SupplierStore> initSupplierStore(String merCode);

    public abstract WelfareConstant.IndustryTag industryTag();
}
