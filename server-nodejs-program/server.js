var net = require('net');
var socks=[];
var server = net.createServer(function (socket) {	
	socket.addListener("connect", function () {
		try{
			console.log("Connection from " + socket.remoteAddress);
			socks.push(socket);			
			socket.emit('doConnect','test');	
		} catch(e){}
	});
	socket.addListener("data", function (data) {       
	   try{
		console.log("data from " + data);	   	  
		broadCast(data);
	   } catch(e){}
    });
	socket.addListener("end", function () {
      try{
		console.log('end of connection');
		this.end();
	  } catch(e){}
    });
	socket.on('message', function (msg) {
		try{
			console.log(msg + "msg from " + socket.remoteAddress);
		} catch(e){}
    });	
});
function broadCast(msg){
	try{
		for(var k in socks){
			socks[k].write(msg);
		}
	} catch(e){}
}
server.listen(8081, '208.117.34.161');
console.log("after bind 8081");
process.on('uncaughtException', function (err) {
		console.log("global error");
		console.log(err);
});
