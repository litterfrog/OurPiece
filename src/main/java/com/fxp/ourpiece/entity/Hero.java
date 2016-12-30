package com.fxp.ourpiece.entity;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import com.fxp.ourpiece.service.GameService;
import com.fxp.ourpiece.util.GameUtils;

public class Hero {
	private Integer id;
	private String userName;
	private String speakWhat="";
	private Timer speakTimer=new Timer(GameService.class.getSimpleName() + " Timer"+id);
//	颜色不可变
	private final String hexColor;
//	方向位置可变
	private Direction direction;
	private Location location;
	
	public Hero(Integer id, String userName) {
		super();
		this.id = id;
		this.userName = userName;
		this.hexColor = GameUtils.getRandomHexColor();
		resetState();
	}

	private void resetState() {
		this.setDirection(Direction.NONE);		
		this.location = GameUtils.getRandomLocation();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHexColor() {
		return hexColor;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
    public synchronized void update(Collection<Hero> heros) throws Exception {
        Location nextLocation = location.getAdjacentLocation(direction);

        if (nextLocation.x >= GameUtils.PLAYFIELD_WIDTH-(Location.GRID_SIZE-Location.STEP)) {
            //nextLocation.x = 0;
        	nextLocation=location;
        }
        if (nextLocation.y >= GameUtils.PLAYFIELD_HEIGHT-(Location.GRID_SIZE-Location.STEP)) {
            //nextLocation.y = 0;
        	nextLocation=location;
        }
        if (nextLocation.x < 0) {
            //nextLocation.x = GameUtils.PLAYFIELD_WIDTH;
        	nextLocation=location;
        }
        if (nextLocation.y < 0) {
            //nextLocation.y = GameUtils.PLAYFIELD_HEIGHT;
        	nextLocation=location;
        }
      //避免碰撞,如果新位置将会发生碰撞则不前往新位置
        if(!willCrash(nextLocation,heros)){
        	location = nextLocation;
        }
        
//        if (direction != Direction.NONE) {
//        	location = nextLocation;
        	
//        }

//        handleCollisions(heros);
    }

	private boolean willCrash(Location nextLocation, Collection<Hero> heros) {
		for(Hero hero:heros){
			if(hero.getId()!=id&&Math.abs(hero.getLocation().x-nextLocation.x)<Location.GRID_SIZE&&Math.abs(hero.getLocation().y-nextLocation.y)<Location.GRID_SIZE){
				return true;
			}
		}
		return false;
	}

	public String getSpeakWhat() {
		return speakWhat;
	}

	public void setSpeakWhat(String speakWhat) {
		this.speakWhat = speakWhat;
	}
	private transient TimerTask task;
	public void setTimedSpeakWhat(String speakWhat) {
		this.speakWhat = speakWhat;
		if(task!=null){
			task.cancel();			
		}
		task=new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("hero speak task");
				Hero.this.speakWhat="";
				
			}
		};
		speakTimer.schedule(task, 3000);
	}

}
