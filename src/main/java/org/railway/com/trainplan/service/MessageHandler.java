package org.railway.com.trainplan.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.json.types.JsonObject;
import org.railway.com.trainplan.common.utils.StringUtil;
import org.railway.com.trainplan.service.message.SendMsgService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageHandler implements MessageListener{

	private static Log logger = LogFactory.getLog(MessageHandler.class.getName());
	@Autowired
	private SendMsgService sendMsgService;
	@Autowired
	private PlanTrainStnService planTrainStnService;
	@Override
	public void onMessage(Message message) {
		
		logger.debug("----------------------------------------------");
		logger.debug("response==" + new String(message.getBody()));
		System.out.println("=================" + new String(message.getBody()));
		//JSONObject json = new JSONObject();
		//JSONObject map = json.fromObject(object)
		ObjectMapper mapper = new ObjectMapper();
		JSONObject msg = null;
		try {
//			 msg= mapper.readValue(message.getBody(), Map.class);
			 msg = JSONObject.fromObject(new String(message.getBody()));
			 logger.debug("msg======" + msg);
			 //System.err.println("msg======" + msg);
			 if(msg != null ){ 
				 
				 JSONObject result1 = msg.getJSONObject("result");
				 //System.err.println("result1==" + result1);
				// Map<String,Object> result1 = mapper.readValue(resultString, Map.class);
				 if(result1 != null){
					 String code = StringUtil.objToStr(result1.get("code"));
					 JSONObject paramObj = result1.getJSONObject("userparam");
					 JSONObject result = result1.getJSONObject("result");
					 
					 //成功
					 if("0".equals(code)){
						 Map<String,Object> reqMap = new HashMap<String,Object>();
						 String daylyPlanId = StringUtil.objToStr(result.get("id"));
						 String planTrainId = StringUtil.objToStr(result.get("planTrainId"));
						 reqMap.put("planTrainId", planTrainId);
						 reqMap.put("daylyPlanId",daylyPlanId );
						 //更新表plan_train中字段DAILYPLAN_FLAG值为0
						 System.out.println("==================" + reqMap);
						 planTrainStnService.updatePlanTrainDaylyPlanFlag(reqMap);
						 
						 if(paramObj.containsKey("msgReceiveUrl")){
							JSONObject jsonMsg = new JSONObject();
							jsonMsg.put("planTrainId", planTrainId);
							jsonMsg.put("createFlag", 1);
							 sendMsgService.sendMessage(jsonMsg.toString(), paramObj.getString("msgReceiveUrl"), "updateTrainRunPlanStatus");
						 }
					 }else{
						   JSONObject jsonMsg = new JSONObject();
						   jsonMsg.put("planTrainId", paramObj.getString("planTrainId"));
						   jsonMsg.put("createFlag", 0);
						   sendMsgService.sendMessage(jsonMsg.toString(), paramObj.getString("msgReceiveUrl"), "updateTrainRunPlanStatus");
					 }
				 }	 
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("----------------------------------------------");
		
	}


}
