package com.fxp.ourpiece.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebBrokerConfig extends AbstractWebSocketMessageBrokerConfigurer/*AbstractSecurityWebSocketMessageBrokerConfigurer*/ {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ourpiece").withSockJS();
		registry.addEndpoint("/game").withSockJS();

	}
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic/", "/queue/");
		registry.setApplicationDestinationPrefixes("/app");
	}
//    @Override 
//    protected boolean sameOriginDisabled() { 
//        return true; 
//    }
}
