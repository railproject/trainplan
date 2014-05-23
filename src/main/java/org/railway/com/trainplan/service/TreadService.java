package org.railway.com.trainplan.service;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.javasimon.aop.Monitored;
import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.service.task.DayTrainPlanTask;
import org.railway.com.trainplan.service.task.DaytaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Monitored
public class TreadService {

	@Autowired
	private TrainInfoService trainInfoService ;
	/**
	 * @param runDate 格式yyyy-mm-dd
	 * @param chartId
	 * @param dayCount
	 * @param operation
	 * @param totalCount
	 * @return
	 */
	public int actionDayWork(DaytaskDto reqDto,int dayCount){
		//System.err.println("trainInfoService111===" + trainInfoService);
		//TODO 通过查询数据库得到totalCount
		int totalCount = 100;
		ExecutorService threadPool = Executors.newFixedThreadPool(dayCount);
		CompletionService<String> pool = new ExecutorCompletionService<String>(threadPool);
		
		for(int i =1;i<=dayCount;i++){
			String currentRunDate = DateUtil.getDateByDay(reqDto.getRunDate(), -i);	
			pool.submit(new DayTrainPlanTask(currentRunDate,reqDto.getChartId(),reqDto.getOperation(),totalCount,trainInfoService));
		}
		
		for(int i =1;i<=dayCount;i++){
			try {
				String rundate = pool.take().get();
				System.err.println("rundate==" + rundate);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
}
