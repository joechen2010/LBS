<?
      /**
	This class is needed so as to communicate 
	with the server side. Instead of the two sides
	sending strings they send Objects type "Auth"
	for Authentication.
      */
      class Auth{
	    private $userID=null;
	    private $session=null;

	    public function __construct(){} 

	    public function setSession($localsession){
		  $this->session=$localsession;
	    }
	    
	    public function getSession() {
		  return $this->session;
	    }

	    public function setUserID($localuserID) {
		  $this->userID=$localuserID;
	    }

	    public function getUserID() {
		  return $this->userID;
	    }

	    public function toStringAuth() {
		  return "Auth->".$this->userID." with ".$this->session."!";
	    }
      }
?>
