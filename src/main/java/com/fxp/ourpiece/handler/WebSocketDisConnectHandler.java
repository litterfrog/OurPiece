package com.fxp.ourpiece.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import com.fxp.ourpiece.service.GameService;
@Controller
public class WebSocketDisConnectHandler implements ApplicationListener<SessionDisconnectEvent> {
	@Autowired
	private GameService gameService;
	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		System.out.println("onApplicationEvent:"+event);
//		MessageHeaders headers = event.getMessage().getHeaders();
//		String simpSessionId = SimpMessageHeaderAccessor.getSessionId(headers);
		System.out.println("onApplicationEvent-removeUser:"+event.getUser().getName());
		gameService.removeHero(event.getUser().getName());
	}

}
