//this program needs to be run after server.js is run
var HOST = '208.117.34.161';    //your server ipaddress where the nodej program runs
var PORT = 8081; //your preferred port
var net = require('net');
var app = require('http').createServer(handler)
  , io = require('socket.io').listen(app)
  , fs = require('fs');

var lastDegree=0;
app.listen(80);

console.log("before handler");
function handler (req, res) {
  fs.readFile(__dirname + '/public/index.html',	
  function (err, data) {
    if (err) {
      res.writeHead(500);
      return res.end('Error loading index.html');
    }
    res.writeHead(200);
    res.end(data);
  });
  console.log("end handler");
}
var done=false;
var soc;
io.sockets.on('connection', function (socket) {
	try{
		soc=socket;
		setInterval(function(){
			if(lastDegree!=0){
				soc.emit('data',lastDegree);				
				lastDegree=0;
				console.log(lastDegree);			
			}			
		},10);		
	} 
	catch(e){
		console.log("in connection excpetion");
	}
	console.log("on connection");
});


var client = new net.Socket();
client.setEncoding('utf8');
client.connect(8081, '208.117.34.161', function() {
	try{
		console.log('CONNECTED TO: ' + '208.117.34.161' + ':' + '8081');
	} catch(e){
		console.log("error while connecting");
	}	
});
var str="";
client.on('data',function(data) {    	
  try{
	console.log(data);
	lastDegree = data;
  } catch(e){}
  console.log("on data");
});
client.on('end',function(data) {
  try{		
	console.log(data);
	lastDegree= data;
  } catch(e){}
});

/**********************************************************************************************/

process.on('uncaughtException', function (err) {
		console.log("global error");
		console.log(err);
});






