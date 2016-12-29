package com.fxp.ourpiece.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import com.fxp.ourpiece.service.GameService;
@Controller
public class WebSocketConnectedHandler implements ApplicationListener<SessionConnectedEvent> {
	@Autowired
	private GameService gameService;

	@Override
	public void onApplicationEvent(SessionConnectedEvent event) {
		System.out.println("onApplicationEvent:"+event);
		
//		MessageHeaders headers = event.getMessage().getHeaders();
//		String simpSessionId = SimpMessageHeaderAccessor.getSessionId(headers);
		
		
	}

}
