/**
 * 
 */
var stomp=null;

function connect(){
	var urlPath="/OurPiece/ourpiece";
	var url=null;
	url=urlPath;
    var ws=new SockJS(url);
    stomp = Stomp.over(ws);
    
    
    var connect_callback = function() {
    	$("#div1").html('<p style="color:green">***** 与服务器连接完成  *****</p>');  
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
	
	if(stomp==null){
		alert("未建立连接!");
		return;
	}
	var message=$("#message").val();
	if(message==null||message==""){
		alert("信息不能为空!");
	}else{
		stomp.send("/app/sendToAll", {priority: 9}, message);
		$("#message").val("");
	}
}