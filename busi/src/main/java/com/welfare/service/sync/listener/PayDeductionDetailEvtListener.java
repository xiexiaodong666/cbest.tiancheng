package com.welfare.service.sync.listener;

import com.welfare.service.sync.event.AccountEvt;
import com.welfare.service.sync.event.PayDeductionDetailEvt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/29/2021
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PayDeductionDetailEvtListener {

    @Autowired(required = false)
    private PersistentBus persistentBus;
    private final DataSource dataSource;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void onAdd(PayDeductionDetailEvt evt) throws PersistentBus.EventBusException {
        persistentBus.postFromTransaction(evt, DataSourceUtils.getConnection(dataSource));
    }

}
