package org.railway.com.trainplan.service;

import java.util.Map;

import org.railway.com.trainplan.common.utils.StringUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.fasterxml.jackson.databind.ObjectMapper;
//@Service
public class MessageHandler implements MessageListener{

	//@Autowired
	//private PlanTrainStnService planTrainStnService;
	//@Autowired
	//private JdbcDao jdbcDao;
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("----------------------------------------------");
		System.out.println("response==" + new String(message.getBody()));
		//JSONObject json = new JSONObject();
		//JSONObject map = json.fromObject(object) 
		
	}

}
