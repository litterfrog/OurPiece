package com.fxp.ourpiece.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fxp.ourpiece.entity.Hero;

@Service
public class GameService implements ApplicationListener<BrokerAvailabilityEvent>  {
	@Autowired
	public SimpMessageSendingOperations messagingTemplate;
	
	private AtomicBoolean brokerAvailable = new AtomicBoolean();
	
	private  Timer gameTimer = null;
	
	private volatile Object lock=new Object();
	
	private ObjectMapper mapper = new ObjectMapper();

	private  long TICK_DELAY=100;
	private  long PERIOD=100;
	
    private  final ConcurrentHashMap<String, Hero> heros = new ConcurrentHashMap<String, Hero>();
    
    public   void addHero(Hero hero) {
    	synchronized (lock) {
            if (heros.size() == 0) {
                startTimer();
            }
		}
        heros.put(hero.getUserName(), hero);
    }



	public  ConcurrentHashMap<String, Hero> getHeros() {
		
		return this.heros;
    }
	public Collection<Hero> getHerosCollection(){
		//只读
		return Collections.unmodifiableCollection(heros.values());
	}

    public  void removeHero(String userName) {
    	heros.remove(userName);
    	synchronized (lock) {
            if (heros.size() == 0) {
                stopTimer();
            }
		}
    }


	private  void stopTimer() {
		System.out.println("stopTimer");
        if (gameTimer != null) {
            gameTimer.cancel();
        }
		
	}


    private  void startTimer() {
    	System.out.println("startTimer");
        gameTimer = new Timer(GameService.class.getSimpleName() + " Timer");
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    tick();
                } catch (Throwable e) {
                    //此处以后改成log4j
                	System.out.println("***error***:"+e.getMessage());
                }
            }


        }, TICK_DELAY, PERIOD);
		
	}
    
	private  void tick() throws Exception {
		if (this.brokerAvailable.get()) {
			for(Hero hero:getHerosCollection()){
				hero.update(getHerosCollection());
			}
			this.messagingTemplate.convertAndSend("/topic/gamebeam", getMapper().writeValueAsString(getHerosCollection()));
		}
		
	}



	public SimpMessageSendingOperations getMessagingTemplate() {
		return messagingTemplate;
	}



	public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}



	@Override
	public void onApplicationEvent(BrokerAvailabilityEvent event) {
		this.brokerAvailable.set(event.isBrokerAvailable());
		
	}



	public ObjectMapper getMapper() {
		return mapper;
	}   
}
