<?
      /**
	This class is needed so as to communicate 
	with the server side. Instead of the two sides
	sending strings they send Objects type "Position"
	for sending and getting the position of the users.
      */
      class Position{
	    private $longitude;
	    private $latitude;
	    private $date;
		
	    public function toStringPosition(){
		  return $this->date + "(" + $this->longitude + "," + $this->latitude + ")";
	    }
		
	    public function __construct(){
		  $this->longitude=null;
		  $this->latitude=null;
		  $this->date=null;
	    }

	    public function setLatitude($latitude1) {
		  $this->latitude=$latitude1;
	    }

	    public function getLatitude() {
		  return $this->latitude;
	    }
		
	    public function setLongitude($longitude1) {
		  $this->longitude=$longitude1;
	    }

	    public function getLongitude() {
		  return $this->longitude;
	    }
		
	    public function setDate($dt) {
		  $this->date=$dt;
	    }
		
	    public function getDate() {
		  return $this->date;
	    }
      }
?>