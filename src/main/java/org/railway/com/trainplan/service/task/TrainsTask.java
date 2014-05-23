package org.railway.com.trainplan.service.task;

import java.util.List;
import java.util.concurrent.Callable;

import org.railway.com.trainplan.service.TrainInfoService;
import org.railway.com.trainplan.service.dto.TrainlineTemplateDto;
import org.springframework.beans.factory.annotation.Autowired;

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
		System.err.println("startNum==" + dayTaskDto.getRownumstart());
		System.err.println("endNum==" + dayTaskDto.getRownumend());
		System.err.println("************************");
		this.dayTaskDto = dayTaskDto;
		this.trainInfoService = trainInfoService;
	}
	@Override
	public Integer call() throws Exception {
		//System.err.println("trainInfoService=="+trainInfoService);
		//1、查询数据，并解析
		List<TrainlineTemplateDto>  list = trainInfoService.getTrainsAndTimesForList(dayTaskDto);
		System.err.println("list.size==" + list);
		//2、插入数据库
		return null;
	}

}
