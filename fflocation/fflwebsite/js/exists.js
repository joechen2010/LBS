var xmlrobject;

function answer() {
	if (xmlrobject.readyState == 4) { //request finished and response is ready
		if (xmlrobject.status == 200) { // is "OK", otherwise some error code is returned, 404 for example.
			//If the response from the server is XML, and you want to parse it as an XML object, 
			//then use the responseXML property
			var xml = xmlrobject.responseXML;  // Assign the XML file to a var
			var root = xml.documentElement;
			var r=root.getElementsByTagName("exists")[0].firstChild.data; // Read the first element
			var div = document.getElementById("exists");
			if(r=='yes'){
				div.setAttribute('bgcolor','red');    
			}else{
				div.setAttribute('bgcolor','green');	
			};
		
		};
	};
};

function question(id) {
	xmlrobject.open("POST", "action/exists.php", true); //Specifies the type of request, the URL, 
							   //and if the request should be handled asynchronously or not.
	var values = "nick=" + document.getElementById(id).value;
	//Adds HTTP headers to the request.
	xmlrobject.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xmlrobject.setRequestHeader("Content-length", values.length);
	xmlrobject.setRequestHeader("Connection", "close");
	
	xmlrobject.onreadystatechange = answer; //Stores a function (or the name of a function) to be 
						//called automatically each time the readyState property changes

	xmlrobject.send(values); //Sends the request off to the server.
};

function init() {
	xmlrobject = createxmlro();
};

function createxmlro() {  
   var txmlro = null;  
   if (window.XMLHttpRequest) {  
      try {  // code for IE7+, Firefox, Chrome, Opera, Safari
        txmlro = new XMLHttpRequest();  //creating an XMLHttpRequest object
      }  
      catch(e) {}  
   }  
   else if (window.ActiveXObject) {  
      try {  // code for IE6, IE5
         txmlro = new ActiveXObject("Msxm12.XMLHTTP");  //creating an XMLHttpRequest object
      }  
      catch (e){  
         try{  
            txmlro = new ActiveXObject("Microsoft.XMLHTTP");  //creating an XMLHttpRequest object
         }  
         catch (e) {}  
     }  
   }  
   return txmlro;  
}
