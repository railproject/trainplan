package org.railway.com.trainplan.service.message;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.springframework.stereotype.Service;

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
		logger.debug("~~~~向页面page："+pageUrl +"  推送消息内容："+message);
		
		Browser.withAllSessionsFiltered(new ScriptSessionFilter() {
			public boolean match(ScriptSession session) {
				System.err.println("pageUrl:"+pageUrl+"   ~~~~~~session.getPage()=="+session.getPage());
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
				Collection<ScriptSession> sessions = Browser
						.getTargetSessions();
				for (ScriptSession scriptSession : sessions) {
					scriptSession.addScript(script);
				}
			}
		});
	}
}
