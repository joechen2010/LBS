<?
      /**
	Requires the files that needs so as to implement the functions.
      */
      require_once('/usr/share/php/nusoap/nusoap.php'); // include the SOAP classes
      require_once('auth.php');
      require_once('userinfo.php');
      require_once('position.php');
      
      /**
	So as to use the functions we must define the path to server
	application ($wsdl), then define the parameter arrays and deal with the
	complex types,by mapping ($classmap , $features). Finally we create 
	client object with "new soapclient($this->wsdl,$features)" and we make
	the call to the service (e.g $client->login($param)).

      */
      class ToServer{
	    private $wsdl='http://localhost:8080/fflServer/wsdl/FFLocationAPI.wsdl'; // define path to server application
	    private $auth = null;
	    private $userInfo= null;
	    public function loginWP($nick,$password) {
		  if($nick == null || $password == null) return 'Error you have not completed both fields for nick and password!'; 
		  $password = sha1($password);
		  $classmap = array('Auth' => 'Auth');
		  $features = array( 'classmap' => $classmap);		  
		  try{
			$client = new soapclient($this->wsdl,$features);
			$param=array('nick' => $nick, 'pw' => $password);
			$response = $client->loginWP($param);
			if($response->loginWPReturn != null) 
			      $this->auth = $response->loginWPReturn;
			else
			      return 'Wrong combination of password and nickname!';
		  }
		  catch (SoapFault $fault) {  
			return 'Error while trying to login! The server is unvailabe right now! Try again later!';
		  }
	    }

	    public function unloggin(){
		  $this->auth=null;
	    }
		
	    public function logged(){
		  return $this->auth!=null;
	    }
	    
	    public function exists($nick){
		  $classmap = array('Auth' => 'Auth','UserInfo' => 'UserInfo');
		  $features = array( 'classmap' => $classmap);
		  try{
			$client = new soapclient($this->wsdl,$features);  //creation of client object
			$param=array('nick' => $nick);
			$response = $client->exists($param); // call to the service
			return $response->existsReturn;
		  }
		  catch (SoapFault $fault) {  
			return;
		  }
	    }

	    public function userInformation(){
		  if($this->auth == null) throw new Exception("Not logged");
		  if($this->userInfo == null){
			$a = $this->auth;
			$classmap = array('Auth' => 'Auth','UserInfo' => 'UserInfo', 'Position' => 'Position');
			$features = array( 'classmap' => $classmap);
			try{
			      $client = new soapclient($this->wsdl,$features);  
			      $param=array('a' => $a);
			      $response = $client->myUser($param);
			      $this->userInfo = $response->myUserReturn;
			}
			catch (SoapFault $fault) {  
			      return;
			}
		  }
		  return $this->userInfo;
	    }

	    public function getPositions($id, $c){
		  if($this->auth == null) throw new Exception("Not logged");
		  $a=$this->auth;
		  $classmap = array('Auth' => 'Auth','UserInfo' => 'UserInfo','Position' => 'Position');
		  $features = array( 'classmap' => $classmap);
		  try{
			$client = new soapclient($this->wsdl,$features);  
			$param=array('a' => $a, 'id' => $id, 'c' => $c);
			$response = $client->getPositions($param);
			if( !isset($response->getPositionsReturn) or $response->getPositionsReturn == null ){
			      return null;
			}
			else if( is_array($response->getPositionsReturn) )
			      return $response->getPositionsReturn;
			else
			      return array($response->getPositionsReturn);
		  }
		  catch (SoapFault $fault) {  
			return;
		  }
	    }

	    public function getFriendsPage($count, $page){
		  if($this->auth == null) throw new Exception("Not logged");
		  $a=$this->auth;
		  $classmap = array('Auth' => 'Auth','UserInfo' => 'UserInfo','Position' => 'Position');
		  $features = array( 'classmap' => $classmap);
		  try{
			$client = new soapclient($this->wsdl,$features);  
			$param=array('a' => $a, 'count' => $count, 'page' => $page);
			$response = $client->getFriendsPage($param);
			if( !isset($response->getFriendsPageReturn) or $response->getFriendsPageReturn == null ){
			      return null;
			}
			else if( is_array($response->getFriendsPageReturn) )
			      return $response->getFriendsPageReturn;
			else
			      return array($response->getFriendsPageReturn);
		  }
		  catch (SoapFault $fault) {  
			return;
		  }
	    }  

	    public function getUsers($count, $page){
		  if($this->auth == null) throw new Exception("Not logged");
		  $a=$this->auth;
		  $classmap = array('Auth' => 'Auth','UserInfo' => 'UserInfo','Position' => 'Position');
		  $features = array( 'classmap' => $classmap);
		  try{
			$client = new soapclient($this->wsdl,$features);  
			$param=array('a' => $a, 'count' => $count, 'page' => $page);			
			$response = $client->getUsers($param);
			if( !isset($response->getUsersReturn) or $response->getUsersReturn == null ){
			      return null;
			}
			else if( is_array($response->getUsersReturn) )
			      return $response->getUsersReturn;
			else
			      return array($response->getUsersReturn);
		  }
		  catch (SoapFault $fault) {  
			return;
		  }
	    }

	    public function newUser($ui, $pw){
		  $pw = sha1($pw);
		  $classmap = array('Auth' => 'Auth');
		  $features = array( 'classmap' => $classmap);		  
		  try{
			$client = new soapclient($this->wsdl,$features);  
			$param=array('ui' => $ui, 'pw' => $pw);
			$response = $client->newUser($param);
			if($response != null) 
				$this->auth = $response->newUserReturn;
		  }
		  catch (SoapFault $fault) {  
			return;
		  }
	    } 

	    public function newAdmin($ui, $pw){
		  if($this->auth == null) throw new Exception("Not logged");
		  $pw = sha1($pw);
		  $a=$this->auth;	  
		  $classmap = array('Auth' => 'Auth', 'UserInfo' => 'UserInfo');
		  $features = array( 'classmap' => $classmap);		  
		  try{
			$client = new soapclient($this->wsdl,$features);  
			$param=array('a' => $a, 'ui' => $ui, 'pw' => $pw);
			$response = $client->newAdmin($param);
			if($response != null){ 
			      return $response->newAdminReturn; 
			}
		  }
		  catch (SoapFault $fault) {  
			return;
		  }
	    } 

	    public function searchFriend($nick, $name, $surname, $country){
		  if($this->auth == null) throw new Exception("Not logged");
		  $a=$this->auth;
		  $classmap = array('Auth' => 'Auth','UserInfo' => 'UserInfo', 'Position' => 'Position');
		  $features = array( 'classmap' => $classmap);
		  try{
			$client = new soapclient($this->wsdl,$features);  
			$param=array('a' => $a,'nick'=>$nick,'name'=>$name,'surname'=>$surname,'country'=>$country);
			$response = $client->searchFriend($param);
			if( !isset($response->searchFriendReturn) or $response->searchFriendReturn == null ){
			      return null;
			}
			else if( is_array($response->searchFriendReturn)){
			      return $response->searchFriendReturn;
			}else{
			      return array($response->searchFriendReturn);
			}
		  }
		  catch (SoapFault $fault) {  
			return;
		  }
	    }

	    public function changeUser($ui, $pw){
		  if($this->auth == null) throw new Exception("Not logged");
		  $pw = sha1($pw);
		  $a=$this->auth;
		  $classmap = array('Auth' => 'Auth','UserInfo' => 'UserInfo','Position' => 'Position');
		  $features = array( 'classmap' => $classmap);
		  try{
			$client = new soapclient($this->wsdl,$features);  
			$param=array('a' => $a, 'ui' => $ui, 'pw' => $pw);
			$response = $client->changeUser($param);
			$this->userInfo=null;
			return $response->changeUserReturn;
		  }
		  catch (SoapFault $fault) {  
			return;
		  }
	    }

	    public function delUser($id){
		  if($this->auth == null) throw new Exception("Not logged");
		  $a=$this->auth;
		  $classmap = array('Auth' => 'Auth','UserInfo' => 'UserInfo','Position' => 'Position');
		  $features = array( 'classmap' => $classmap);
		  try{
			$client = new soapclient($this->wsdl,$features);  
			$param=array('a' => $a, 'id' => $id);
			$response = $client->delUser($param);
			return $response->delUserReturn;
		  }
		  catch (SoapFault $fault) {  
			return;
		  }
	    }
      }
?>
