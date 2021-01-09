package com.welfare.service.impl;

import com.welfare.service.DepositService;
import com.welfare.service.dto.Deposit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/9/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DepositServiceImpl implements DepositService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deposit(Deposit deposit) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deposit(List<Deposit> deposits) {

    }
}
