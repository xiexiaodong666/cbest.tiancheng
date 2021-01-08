package com.welfare.servicesettlement.task;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 分布式调度时分片处理任务逻辑
 * @param <T>
 */
@Slf4j
public abstract class SettlementWorkXxlJobTaskBean<T> extends IJobHandler {
	/**
	 * 分片数量
	 */
	protected Integer taskItemNum;
	/**
	 * 单次处理列表量
	 */
	protected Integer eachFetchDataNum = 500;
	/**
	 * 命中的分片序号
	 */
	protected Integer task_item;

	@Override
	public ReturnT<String> execute(String pramas){
		log.info("============"+this.getClass().getName()+"任务执行开始===================");
		ShardingUtil.ShardingVO shardingVo = ShardingUtil.getShardingVo();
		taskItemNum = shardingVo.getTotal();
		task_item = shardingVo.getIndex();

		List<T> dealList;

		boolean result;

		do {
			//循环获取处理数据，分批处理
			dealList = selectTasks(taskItemNum, taskItemNum, eachFetchDataNum);
			result = this.execute(dealList);
		}while(!dealList.isEmpty() && result);
		log.info("============"+this.getClass().getName()+"任务执行结束===================");
		if(result){
			return ReturnT.SUCCESS;
		}else{
			return ReturnT.FAIL;
		}
	}

	/**
	 * 查询获取待执行任务数据
	 * @param taskItemNum 分片数量
	 * @param taskItem 分片参数
	 * @param eachFetchDataNum 每次处理数量 默认500
	 * @return
	 */
	public abstract List<T> selectTasks(Integer taskItemNum, Integer taskItem, Integer eachFetchDataNum);

	/**
	 * 处理查询到的数据
	 * @param list
	 * @return
	 */
	public abstract boolean execute(List<T> list);

}
