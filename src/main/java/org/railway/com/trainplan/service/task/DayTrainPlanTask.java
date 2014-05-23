package org.railway.com.trainplan.service.task;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.railway.com.trainplan.service.TrainInfoService;
import org.railway.com.trainplan.service.TreadService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Map中的值有：
 * runDate,totalCount
 * @author join
 *
 */
public class DayTrainPlanTask implements Callable<String>{

	
	private String runDate;
	private String chartId;
	private String operation;
	private int totalCount;
	private int pageSize = 10;
	private int threadCount;
	private TrainInfoService trainInfoService;
	public DayTrainPlanTask(String runDate,String chartId,String operation,int totalCount,TrainInfoService trainInfoService){
		this.runDate = runDate;
		this.totalCount = totalCount;
		this.chartId = chartId;
		this.operation = operation;
		this.trainInfoService = trainInfoService;
		if (this.totalCount % pageSize >0) {
			threadCount = this.totalCount / pageSize + 1;
		} else {
			threadCount = this.totalCount / pageSize;
		}
		
		
	}
	/**
	 * 返回的是runDay 格式yyyy-mm-dd
	 */
	@Override
	public String call() throws Exception {
		DaytaskDto dto = new DaytaskDto();
		dto.setChartId(chartId);
		dto.setOperation(operation);
		dto.setRunDate(runDate);
		//对所有数据进行分页
		ExecutorService service =Executors.newFixedThreadPool(20);
		CompletionService<Integer> completion = new ExecutorCompletionService<Integer>(service);
		int temp = threadCount;
		while(threadCount >0){
			
			dto.setRownumend(threadCount*pageSize);
			dto.setRownumstart((threadCount-1)*pageSize+1);
			completion.submit(new TrainsTask(dto,trainInfoService));
			threadCount --;
		}
		
		for(int i =1;i<=temp;i++){
			try {
				int cout = completion.take().get();
				System.err.print("cout==" + cout);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		service.shutdown();
		return runDate;
	}

	
	
}
