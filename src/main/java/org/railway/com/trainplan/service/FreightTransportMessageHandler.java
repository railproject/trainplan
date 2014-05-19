package org.railway.com.trainplan.service;

import net.sf.json.JSONObject;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

//import com.railwayplan.common.util.SpringContextUtil;

public class FreightTransportMessageHandler implements MessageListener{
	
  
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("----------------------------------------------");
		System.out.println("response==" + new String(message.getBody()));
//		QuoteService quoteService = (QuoteService)SpringContextUtil.getBean("quoteService");
		JSONObject result = JSONObject.fromObject(new String(message.getBody()));
		 
		if("0".equals(result.getJSONObject("result").getString("code"))){
			System.out.println(JSONObject.fromObject(result.getJSONObject("result").getString("result")).toString());
			JSONObject json = JSONObject.fromObject(result.getJSONObject("result").getString("result"));
			json.put("requestId", result.getString("reuqestId"));
//			quoteService.sendStatus(json.toString());
		}else{
//			quoteService.sendStatus("{\"code\":\"-1\", \"requestId\":\"" + result.getString("reuqestId") + "\"}");
		} 
	}

}
