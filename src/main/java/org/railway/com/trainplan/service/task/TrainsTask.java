package org.railway.com.trainplan.service.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.railway.com.trainplan.service.TrainInfoService;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;

/**
 * 处理列车信息
 * Map中放的字段：
 * chartId,operation,rownumstart,rownumend
 * @author join
 *
 */
public class TrainsTask implements Callable<Integer>{
	
	private TrainInfoService trainInfoService;
	private DaytaskDto dayTaskDto;
	public TrainsTask(DaytaskDto dayTaskDto,TrainInfoService trainInfoService){
		
		this.dayTaskDto = dayTaskDto;
		this.trainInfoService = trainInfoService;
	}
	@Override
	public Integer call() {
		List<TrainlineTemplateDto>  list = new ArrayList<TrainlineTemplateDto>();
		try{
			System.err.println("startNum==" + dayTaskDto.getRownumstart());
			System.err.println("endNum==" + dayTaskDto.getRownumend());
			System.err.println("************************");
			//1、查询数据，并解析
			list = trainInfoService.getTrainsAndTimesForList(dayTaskDto);
			System.err.println("list.size==" + list.size());
			//2、插入数据库
			// 保存数据
			trainInfoService.addTrainPlanLine(list);	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list.size();
	}

}
