package org.railway.com.trainplan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.railway.com.trainplan.common.utils.DateUtil;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.railway.com.trainplan.service.message.SendMsgService;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TrainPlanThread extends Thread {


	private List<TrainlineTemplateDto> list;
	private String startDate;
	private String dayCount ;
	private PlanTrainStnService planTrainStnService;
	private QuoteService quoteService;
	
	public TrainPlanThread(List<TrainlineTemplateDto> list,String startDate,String dayCount,
			PlanTrainStnService planTrainStnService,QuoteService quoteService){
		this.list = list;
		this.startDate = startDate ;
		this.dayCount = dayCount;
		this.planTrainStnService = planTrainStnService;
		this.quoteService = quoteService;
	}
	
	public void run(){
		System.err.println("*****************");
		try{
			
		
		long starttime = System.currentTimeMillis();
		 ObjectMapper objectMapper = new ObjectMapper();
		for (int i = 0; i < Integer.valueOf(dayCount); i++) {
			 int daycount = i + 1;
			 String tempStartDate = DateUtil.getDateByDay(
					startDate, -i);
			//logger.info("startDate==" + tempStartDate);
            Map<String,Object> messageMap = new HashMap<String,Object>();
            
			if (list != null && list.size() > 0) {
				messageMap.put("startDate", tempStartDate);
				messageMap.put("daycount", daycount);
				messageMap.put("trainsCount", 0);
				String message = objectMapper.writeValueAsString(messageMap);
				// 推送开始某天记录的信息
				
				quoteService.sendQuotes(tempStartDate,daycount, 0, "plan.day.begin");
				// 保存数据
				planTrainStnService.addTrainPlanLine(list, tempStartDate);
				
				// 推送结束某天记录信息
				quoteService.sendQuotes(tempStartDate,daycount, list.size(), "plan.day.end");

			}
		 }
		System.err.println("插入数据所花时间：" + (System.currentTimeMillis()-starttime)/1000);
		// 推送全部结束的信息
		quoteService.sendQuotes("", 0, 0, "plan.end");
		
		System.err.println("*****************");
	  
	}catch(Exception e){
		
	}
  }
}
