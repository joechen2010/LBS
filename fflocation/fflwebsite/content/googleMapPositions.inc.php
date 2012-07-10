<!--We write a JavaScript function to create a "map" object.-->
<script type="text/javascript">     

      function initialize() {
	    var latlng = new google.maps.LatLng(55.65734340292977, 12.5518798828125);	
	    //creating a Map options object to contain map initialization variables.
	    var myOptions = {zoom: 8,center: latlng, mapTypeId: google.maps.MapTypeId.ROADMAP};
            //creating a single Map.
	    var map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
	    <?php
		  /**
		    If the $_POST['showHistory'] is set then we get the last $num (here 10) poisitons
		    of one user and we put the in the map, and we show the with markers.
		    Finally the $_POST['showHistory'] is unset.
		  */
		  if( isset($_POST['showHistory']) ){
			$lastpositions = $_SESSION['userID']->getPositions($_POST['id_history'],100);
			$array_length = count($lastpositions);;
			if( $array_length != 0 ){
			      // creating a marker in the user's position
			      for( $i = 0; $i < $array_length; $i++ ) {  
				    echo ' var marker = new google.maps.Marker({ animation: google.maps.Animation.DROP,
					   position: new google.maps.LatLng(';
				    echo $lastpositions[$i]->getLatitude();
				    echo ',';
				    echo $lastpositions[$i]->getLongitude();
				    echo '), title:"';
				    echo $lastpositions[$i]->getDate();
				    echo '"});
				    marker.setMap(map);';   
			      }
			}
		  }
		  else if( !isset($_POST['showHistory']) ){    /**
			      Else depending on if he is an Administrator, we take all the users
			      positions, or a simple user, we take all his friends positions, and we
			      put the in the map, and we show the with markers.
			    */
			if($_SESSION['userID']->userInformation()->isAdministrator()){
			      $allusers = $_SESSION['userID']->getUsers(100,0);
			}else{
			      $allusers = $_SESSION['userID']->getFriendsPage(100,0);
			}
			$array_length = count($allusers);
			if ( $array_length != 0 ){
			      for( $i=0; $i < $array_length; $i++) {
				    if( $allusers[$i]->getPosition() != null){
					  echo ' var marker = new google.maps.Marker({ animation: google.maps.Animation.DROP,
						  position: new google.maps.LatLng(';
					  echo $allusers[$i]->getPosition()->getLatitude();
					  echo ',';
					  echo $allusers[$i]->getPosition()->getLongitude();
					  echo '), title:"';
					  echo $allusers[$i]->getNick();
					  echo '"});
					  marker.setMap(map);';
				    }
			      }
			}
		  }
	    ?>
      }
</script>

<div>
      <p class="con" style="font-size:xx-large;"><i>User History and Positions</i></p> 
      <hr class="con"/>
</div>

<!--For the map to display on the web page-->
<div id="map_canvas" style="width: 860px; height: 460px; clear:both;"></div><br />