package org.railway.com.trainplan.service;

import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;

public class TrainPlanThread extends Thread {

	private List<TrainlineTemplateDto> list;
	private String startDate;
	private String dayCount ;
	private PlanTrainStnService planTrainStnService;
	public TrainPlanThread(List<TrainlineTemplateDto> list,String startDate,String dayCount,PlanTrainStnService planTrainStnService){
		this.list = list;
		this.startDate = startDate ;
		this.dayCount = dayCount;
		this.planTrainStnService = planTrainStnService;
	}
	
	public void run(){
		System.err.println("*****************");
		long starttime = System.currentTimeMillis();
		for (int i = 0; i < Integer.valueOf(dayCount); i++) {
			final int daycount = i + 1;
			final String tempStartDate = DateUtil.getDateByDay(
					startDate, -i);
			//logger.info("startDate==" + tempStartDate);

			if (list != null && list.size() > 0) {
				// 推送开始某天记录的信息
				//quoteService.sendQuotes(tempStartDate,daycount, 0, "plan.day.begin");
				// 保存数据
				planTrainStnService.addTrainPlanLine(list, tempStartDate);
				
				// 推送结束某天记录信息
				//quoteService.sendQuotes(tempStartDate,daycount, list.size(), "plan.day.end");

			}
		 }
		System.err.println("插入数据所花时间：" + (System.currentTimeMillis()-starttime)/1000);
		// 推送全部结束的信息
		//quoteService.sendQuotes("", 0, 0, "plan.end");
		//planTrainStnService.insertTrainPlanTest(dataMap);
		System.err.println("*****************");
	}
}
