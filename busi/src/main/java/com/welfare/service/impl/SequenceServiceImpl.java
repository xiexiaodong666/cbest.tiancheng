package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.dao.SequenceDao;
import com.welfare.persist.entity.Sequence;
import com.welfare.service.SequenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.welfare.common.constants.RedisKeyConstant.SEQUENCE_GENERATE;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SequenceServiceImpl implements SequenceService {
    private final SequenceDao sequenceDao;
    private final RedissonClient redissonClient;

    @Override
    public Long next(String sequenceType) {
        RLock lock = redissonClient.getFairLock(SEQUENCE_GENERATE + ":" + sequenceType);
        lock.lock();
        try{
            QueryWrapper<Sequence> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(Sequence.SEQUENCE_TYPE,sequenceType);
            Sequence sequence = sequenceDao.getOne(queryWrapper);
            sequence.setSequenceNo(sequence.getSequenceNo() + 1);
            sequenceDao.updateById(sequence);
            return sequence.getSequenceNo();
        }finally {
            lock.unlock();
        }
    }
}
