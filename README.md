Turn your android device into a joystick for your HTML5 game , This app opens a socket to server and polls orientation of the deivice. 
hostname and port are configurable. 
A node server program is provided for testing.

Check list:  






For *server-nodejs-program*

1) app.js   rename host ip address to appropriate ip address of your server

2) server.js rename host ip address to appropriate ip address of your server

3) Please run server.js first and then app.js   to run on node   

   a) node server.js
   
   b) node app.js
   
4) in public/index.html  change the ip address to your server IP address  

    	var socket = io.connect('http://208.117.34.161');   -- this line 
            
5) node modules that need to be installed
	a)  socket.io   (npm install socket.io)








For *client-android-application*	

1) make sure you run server which listens on port 8081 (by default) before using the android application

2) android client sends data to server as follows


	For auth :
	{"Action":"Auth","User":"tesrusername","Pwd":"testpwd"}
	
	For device orientation change:
	{"Action":"Angle","Angle":"246.57813"}

I will be happy to help for further assistance , 9chakri@gmail.com	


