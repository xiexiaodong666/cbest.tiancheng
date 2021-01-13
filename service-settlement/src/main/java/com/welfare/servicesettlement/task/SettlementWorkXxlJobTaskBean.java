package com.welfare.servicesettlement.task;


import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 调度处理任务逻辑
 * @param <T>
 */
@Slf4j
public abstract class SettlementWorkXxlJobTaskBean<T> extends IJobHandler {


	@Override
	public ReturnT<String> execute(String pramas){
		log.info("============"+this.getClass().getName()+"任务执行开始===================");

		//处理数据
		log.info("请求处理参数：{}", pramas);
		List<T> dealList = selectTasks(pramas);
		if(!dealList.isEmpty()){
			log.debug("准备执行任务数据：{}", JSON.toJSONString(dealList));
			try {
				dealList.forEach(o->{
					this.execute(o);
				});
			} catch (Exception e) {
				log.info("============"+this.getClass().getName()+"任务执行【异常】===================");
				log.info("任务执行异常,异常信息：{}", e);
				return ReturnT.FAIL;
			}
		}

		log.info("============"+this.getClass().getName()+"任务执行【完成】===================");
		return ReturnT.SUCCESS;
	}

	/**
	 * 查询获取待执行任务数据
	 * @param params 调度请求参数
	 * @return
	 */
	public abstract List<T> selectTasks(String params);

	/**
	 * 处理查询到的数据
	 * @return
	 */
	public abstract void execute(T t);

}
