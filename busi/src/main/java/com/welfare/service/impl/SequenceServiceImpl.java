package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.reflect.Reflection;
import com.welfare.persist.dao.SequenceDao;
import com.welfare.persist.entity.Sequence;
import com.welfare.service.SequenceService;
import com.welfare.service.sequence.MaxSequenceExceedHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.ReflectionUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    public Long nextNo(String sequenceType) {
        Sequence next = next(sequenceType);
        return next.getSequenceNo();
    }

    @Override
    public String nextFullNo(String sequenceType) {
        Sequence next = next(sequenceType);
        return next.getPrefix() + next.getSequenceNo();
    }

    private Sequence next(String sequenceType){
        RLock lock = redissonClient.getFairLock(SEQUENCE_GENERATE + ":" + sequenceType);
        lock.lock();
        try{
            QueryWrapper<Sequence> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(Sequence.SEQUENCE_TYPE,sequenceType);
            Sequence sequence = sequenceDao.getOne(queryWrapper);
            long targetSequenceNo = sequence.getSequenceNo() + 1;
            if(targetSequenceNo > sequence.getMaxSequence()){
                sequence = handleWhenMaxSeqExceed(sequence);
            }else{
                sequence.setSequenceNo(targetSequenceNo);
            }
            sequenceDao.updateById(sequence);
            return sequence;
        }finally {
            lock.unlock();
        }
    }

    @SneakyThrows
    private Sequence handleWhenMaxSeqExceed(Sequence sequence) {
        Class<?> clazz = ReflectionUtil.forName(sequence.getHandlerForMax());
        MaxSequenceExceedHandler handler = (MaxSequenceExceedHandler)clazz.newInstance();
        return handler.handle(sequence);
    }
}
