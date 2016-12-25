package com.fxp.ourpiece.handler;

import java.security.Principal;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatHandler {
	@MessageMapping("/sendToAll")
	public String sendToAll(Principal p,String payload){
//		System.out.println(p);
		
		return "["+p.getName()+"]:"+payload+"\n";
	}
}
