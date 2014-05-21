package org.railway.com.trainplan.service.message;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.railway.com.trainplan.service.dto.Quote;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SendMsgService {
	private static final Logger logger = Logger.getLogger(SendMsgService.class);
	
	/**
	 * 消息推送
	 * @param message 消息内容
	 * @param page 页面url
	 * @param jsFuncName 页面js方法名称
	 */
	public void sendMessage(final String message, final String pageUrl, final String jsFuncName) {
		System.err.println("~~~~向页面page："+pageUrl +"  推送消息内容："+message +" jsFuncName:"+jsFuncName);
		
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				if ((pageUrl).equals(session.getPage())) {
					return true;
				} else {
					return false;
				}
			}
		}, new Runnable() {
			private ScriptBuffer script = new ScriptBuffer();

			public void run() {
				script.appendCall(jsFuncName, message);
				System.err.println("!!!! script: "+script);
				Collection<ScriptSession> sessions = Browser
						.getTargetSessions();
				for (ScriptSession scriptSession : sessions) {
					scriptSession.addScript(script);
				}
			}
		});
	}
}
