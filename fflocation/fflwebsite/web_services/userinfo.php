<?
      /**
	This class is needed so as to communicate 
	with the server side. Instead of the two sides
	sending strings they send Objects type "UserInfo"
	for getting the user's information.
      */
      class UserInfo {
	    private $id;
	    private $nick;
	    private $name;
	    private $surname;
	    private $email;
	    private $phone;
	    private $country;
	    private $address;
	    private $administrator;
	    private $position;
		
	    public function toStringUserInfo(){
		  return $this->id . "/" . $this->nick . ":" .
			"[" . $this->name . " " . $this->surname . "]" .
			"{" . $this->email . " " . $this->phone . "}" . 
			"(" . $this->country . " " . $this->address . ") " .
			"admin=" . $this->administrator . " ¿" . $this->position . "?";
	    }
		
	    public function __construct() {
		  $this->id = 0;
		  $this->nick = null;
		  $this->name = null;
		  $this->surname = null;
		  $this->email = null;
		  $this->phone = 0;
		  $this->country = null;
		  $this->address = null;
		  $this->administrator = false;
		  $this->position=null;
	    }
		
	    public function setNick($nick1) {
		  $this->nick=$nick1;
	    }
		
	    public function getNick() {
		  return $this->nick;
	    }
		
	    public function setId($id1) {
		  $this->id=$id1;
	    }
		
	    public function getId() {
		  return $this->id;
	    }
		
	    public function setName($name1) {
		  $this->name=$name1;
	    }
		
	    public function getName() {
		  return $this->name;
	    }
		
	    public function setEmail($email1) {
		  $this->email=$email1;
	    }
		
	    public function getEmail() {
		  return $this->email;
	    }
		
	    public function setSurname($surname1) {
		  $this->surname=$surname1;
	    }
		
	    public function getSurname() {
		  return $this->surname;
	    }
	    
	    public function setPhone($phone1) {
		  $this->phone=$phone1;
	    }
		
	    public function getPhone() {
		  return $this->phone;
	    }
		
	    public function setCountry($country1) {
		  $this->country=$country1;
	    }
		
	    public function getCountry() {
		  return $this->country;
	    }
		
	    public function setAddress($address1) {
		  $this->address=$address1;
	    }
		
	    public function getAddress() {
		  return $this->address;
	    }
		
	    public function setAdministrator($isAdministrator1) {
		  $this->administrator=$isAdministrator1;
	    }
		
	    public function isAdministrator() {
			
		  return $this->administrator;
	    }
		
	    public function getPosition() {
		  return $this->position;
	    }
		
	    public function setPosition($p1) {
		  $this->position=$p1;
	    }
      }
?>
