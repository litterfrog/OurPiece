package com.fxp.ourpiece.handler;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxp.ourpiece.entity.Direction;
import com.fxp.ourpiece.entity.Hero;
import com.fxp.ourpiece.service.GameService;
/*
 * *******headers***********
 * {simpMessageType=MESSAGE, 
 * stompCommand=SEND, 
 * nativeHeaders={
 * 	priority=[9], 
 * 	destination=[/app/movedirection], 
 * 	content-length=[21]}, 
 * simpSessionAttributes={}, 
 * simpHeartbeat=[J@591e8cc, 
 * simpUser=org.springframework.security.authentication.UsernamePasswordAuthenticationToken@bbe16a3a: Principal: org.springframework.security.core.userdetails.User@18dde: Username: fxp; Password: [PROTECTED]; Enabled: true; AccountNonExpired: true; credentialsNonExpired: true; AccountNonLocked: true; Granted Authorities: ROLE_USER; Credentials: [PROTECTED]; Authenticated: true; Details: org.springframework.security.web.authentication.WebAuthenticationDetails@fffde5d4: RemoteIpAddress: 0:0:0:0:0:0:0:1; SessionId: B0CA9796EF8B2047F9AA35AEE7502340; Granted Authorities: ROLE_USER,
 * lookupDestination=/movedirection, 
 * simpSessionId=vcyxlvtb, 
 * simpDestination=/app/movedirection}
 * */
@Controller
public class GameHandler {
	@Autowired
	private GameService gameService;
	private ObjectMapper mapper = new ObjectMapper();
	
	@MessageMapping("/movedirection")
	public void handleMoveDirection(String payload,Principal principal) throws JsonParseException, JsonMappingException, IOException{
		System.out.println("principal:"+principal.getName());
		
		//useful code	
		//JSON String->Object	
		HashMap map = mapper.readValue(payload,HashMap.class);
		System.out.println("handleMoveDirection-direction"+map.get("direction"));
		
		String direction=(String) map.get("direction");
		if(null!=map&&null!=direction){
			if(gameService.getHeros().containsKey(principal.getName())){
				switch(direction){
				case "west":
					gameService.getHeros().get(principal.getName()).setDirection(Direction.WEST);
					break;
				case "north":
					gameService.getHeros().get(principal.getName()).setDirection(Direction.NORTH);
					break;
				case "east":
					gameService.getHeros().get(principal.getName()).setDirection(Direction.EAST);
					break;
				case "south":
					gameService.getHeros().get(principal.getName()).setDirection(Direction.SOUTH);
					break;
				case "none":
					gameService.getHeros().get(principal.getName()).setDirection(Direction.NONE);
					break;					
				default:
					;	
				}
			}

		}
		
		//test code
		System.out.println("gameService:"+gameService.getMessagingTemplate());
		String writeValueAsString = mapper.writeValueAsString(gameService.getHeros());
		System.out.println("***************");
		System.out.println(writeValueAsString);
		System.out.println("***************");
	}
	@MessageMapping("/speak")
	public void handleSpeak(String payload,Principal principal) throws JsonParseException, JsonMappingException, IOException{
		//JSON String->Object	
		HashMap map = mapper.readValue(payload,HashMap.class);
		System.out.println("handleSpeak-message:"+map.get("message"));
		
		String message=(String) map.get("message");
		if(null!=map&&null!=message){
			if(gameService.getHeros().containsKey(principal.getName())){
				gameService.getHeros().get(principal.getName()).setTimedSpeakWhat(message);
			}
		}
	}
}
