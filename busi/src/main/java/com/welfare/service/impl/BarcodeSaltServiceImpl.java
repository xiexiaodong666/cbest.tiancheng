package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.dao.BarcodeSaltDao;
import com.welfare.persist.entity.BarcodeSalt;
import com.welfare.service.BarcodeSaltService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BarcodeSaltServiceImpl implements BarcodeSaltService {
    private final BarcodeSaltDao barcodeSaltDao;
    @Override
    public List<BarcodeSalt> query(Long fromValidPeriodNumeric) {
        QueryWrapper<BarcodeSalt> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(BarcodeSalt.VALID_PERIOD_NUMERIC,fromValidPeriodNumeric);
        return barcodeSaltDao.list(queryWrapper);
    }
}
