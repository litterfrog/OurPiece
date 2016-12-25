<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>chat</title>
<script type="text/javascript" src="scripts/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="scripts/sockjs.js"></script>
<script type="text/javascript" src="scripts/stomp.js"></script>
<script type="text/javascript">
var stomp=null;
$(connect())

function connect(){
	var urlPath="${pageContext.request.contextPath }/ourpiece";
	var url=null;
	url=urlPath;
    var ws=new SockJS(url);
    stomp = Stomp.over(ws);
    
    
    var connect_callback = function() {
    	$("#div1").html('***** 与服务器连接完成  *****');  
    	$("#title").html("可以说话了");
        stomp.subscribe("/topic/sendToAll", handleMessage);
        //stomp.subscribe("/user/topic/handleMessage", handleMessage);
        //stomp.send("/app/singleuser", {priority: 9}, "Hello, STOMP");
        //stomp.send("/app/sendToAll", {priority: 9}, "Hello, STOMPgetMessage");
    }
    var error_callback = function(error) {
        alert(error);
        stomp=null;
      };
      var headers = {
    	      login: 'login',
    	      passcode: 'passcode'
    	    };
    stomp.connect({},connect_callback,error_callback);   
      
    function handleMessage(message){
    	var text=message.body;
    	$("#messageContent").append(text);
    }
	$("#message").focus();
}
function sendMessage(){
	var message=$("#message").val();
	if(stomp==null){
		alert("未建立连接!");
		return;
	}
	if(message==null||message==""){
		alert("信息不能为空!");
	}else{
		stomp.send("/app/sendToAll", {priority: 9}, message);
		$("#message").val("");
	}
}
</script>
</head>
<body>
	<div id="div1"></div>
	<h3 id="title"> </h3>	
	<div id="console">
		<textarea id="messageContent" rows="15" cols="60" readonly="readonly" ></textarea><br/>
		<input type="text" id="message"><input type="button" value="发送" onclick="sendMessage()">
	</div>
</body>
</html>