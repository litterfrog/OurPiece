<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>chat</title>
<style type="text/css">
#divchat{
	float:left;
}
#divgame{
	margin:0px 0px 0px 10px;
	float:left;
}
#gcanvas{
	margin:0px 0px 5px 0px;
	background:black;
}
</style>
<script type="text/javascript" src="scripts/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="scripts/jCanvaScript.1.5.18.js"></script>
<script type="text/javascript" src="scripts/sockjs.js"></script>
<script type="text/javascript" src="scripts/stomp.js"></script>
<script type="text/javascript" src="scripts/chat.js"></script>
<script type="text/javascript">
//{"id":1,"simpSessionId":"ke43hrme","hexColor":"#e6aed6","direction":"NONE","location":{"x":120,"y":160}},{"id":2,"simpSessionId":"rbpkngd0","hexColor":"#e6c8ca","direction":"NONE","location":{"x":140,"y":320}}


	var Game = {};
	Game.urlPath="/OurPiece/game";
	Game.context=null;
	Game.direction = 'none';
	Game.gridSize = 20;
	Game.heros=null;
	Game.socket=null;
	Game.stomp=null;
	Game.maxX=660;
	Game.maxY=400;
	Game.setDirection=null;
	Game.initialize=null;
	Game.connect=null;
	Game.setKeyListener=null;
	Game.startGameLoop=null;
	Game.stopGameLoop=null;
	Game.run=null;
	Game.nextFrame=null;
	Game.draw=null;
	
//	function Hero(){
//		this.
//	}
//    Hero.prototype.draw = function(context) {
    	
//    }
	
	
	Game.initialize=function(){
        if (!($("#gcanvas")[0].getContext)) {
            alert('Error: 2d canvas not supported by this browser.');
            return;
        }
		Game.context=$("#gcanvas")[0].getContext('2d');
		//设置字体样式
	    Game.context.font = "10px Courier New";
		Game.setKeyListener();
		Game.connect();
	}
	Game.connect=function(){
		 Game.socket = new SockJS(Game.urlPath);
		 Game.stomp = Stomp.over(Game.socket);
		 var connect_callback = function() {
		   	Game.stomp.subscribe("/topic/gamebeam", handleGameBeam);
		   	Game.startGameLoop();
		 }
		 var error_callback = function(error) {
			Game.stopGameLoop();
		    alert(error);
			Game.stomp=null;
		 };	
		 function handleGameBeam(message){
			 var heros=JSON.parse(message.body);
			 //更新heros
			 Game.heros=heros;
		 }
		 Game.stomp.connect({},connect_callback,error_callback);

	}
	Game.startGameLoop=function(){
		window.requestAnimationFrame = window.requestAnimationFrame || window.mozRequestAnimationFrame || window.webkitRequestAnimationFrame || window.msRequestAnimationFrame;
		Game.nextFrame = function () {
			requestAnimationFrame(Game.run);
        };
        if (Game.nextFrame != null) {
            Game.nextFrame();
        }
	}
	Game.run=function(){
        Game.draw();
        if (Game.nextFrame != null) {
            Game.nextFrame();
        }
	}
	Game.draw=function(){
		this.context.clearRect(0, 0, 660, 400);
        for (var id in this.heros) {
           // this.heros[id].draw(this.context);
           	
        	this.context.fillStyle=this.heros[id].hexColor;
        	this.context.fillText(this.heros[id].speakWhat, this.heros[id].location.x, this.heros[id].location.y-3);
        	this.context.fillRect(this.heros[id].location.x, this.heros[id].location.y, Game.gridSize, Game.gridSize);
        }
	}
	Game.stopGameLoop=function(){
		Game.nextFrame = null;
	}
	Game.setKeyListener=function(){
		$(window).keydown(function(e){
			var code = e.keyCode;
			if (code > 36 && code < 41) {
				switch(code){
				case 37://left
					if(Game.direction!='west') Game.setDirection('west');
					//jc('#hero').animate({x:0},1000);
					break;
				case 38://up
					if(Game.direction!='north') Game.setDirection('north');
					break;
				case 39://right
					if(Game.direction!='east') Game.setDirection('east');
					break;
				case 40://down
					if(Game.direction!='south') Game.setDirection('south');
					break;				
				}
			}
			
		});
		$(window).keyup(function(e){
			var code = e.keyCode;
			if (code > 36 && code < 41) {
				Game.setDirection('none');
			}
		});
	}

	
	Game.setDirection=function(direction){
		Game.direction=direction;
		var message={direction:direction};
		Game.stomp.send("/app/movedirection", {priority: 9}, JSON.stringify(message));
	}
	

	
//	var gc=$("#gcanvas")[0].getContext('2d');
//	jc.start('gcanvas',true);//第二个参数重绘
//	jc.rect(50,200,20,20,'#ff0099',1).click(function(){
//		alert("I am you hero");
//	}).id('hero');
//	jc.start('gcanvas');
//	var mx=100;
//	$("#ophero").click(function(){
//		jc('#hero').animate({x:mx,y:200},1000);
//		if(mx==100){
//			mx=50;
//		}else{
//			mx=100;
//		}
		
//	});

	function startgame(){
		if(Game.stomp==null){
			Game.initialize();
		}
		
	}
	function speak(){
		if(Game.stomp==null){
			alert("游戏还未开始或游戏链接已断开，请重新连接。");
			return;
		}
		var message=$("#speakwhat").val();
		if(message==null||message==""){
			alert("内容不能为空!");
		}else{
			Game.stomp.send("/app/speak", {priority: 9}, JSON.stringify({message:message}));
			$("#speakwhat").val("");
		}
		
	}
</script>
</head>
<body>
<div id="divchat">
	<div id="div1">
		<input type="button" value="连接聊天服务" onclick="connect()"/>
	</div>
	<h3 id="title"> </h3>	
	<div id="console">
		<textarea id="messageContent" rows="15" cols="60" readonly="readonly" ></textarea><br/>
		<input type="text" id="message"><input type="button" value="发送" onclick="sendMessage()">
	</div>
</div>
<div id="divgame">
	<canvas id="gcanvas" width="660" height="400">
		<span>不支持canvas</span>
	</canvas><br/>
	<input type="button" id="startgame" value="开始游戏" onclick="startgame()">
	<input type="text" id="speakwhat">
	<input type="button" id="speak" value="讲话" onclick="speak()">
</div>
</body>
</html>