package com.fxp.ourpiece.handler;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Controller
public class ChatHandler extends AbstractWebSocketHandler {
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("afterConnectionEstablished");
	}
	@Override
		public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("afterConnectionClosed");
	}
	@MessageMapping("/sendToAll")
	public String sendToAll(Principal p,String payload){
		
		return "["+p.getName()+"]:"+payload+"\n";
	}

}
