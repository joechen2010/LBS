<?php
      /**
	Depending if the user is an Administrator or a simple user 
	it requires the right file to show.
      */
      if( $_SESSION['userID']->userInformation()->isAdministrator() ){
	    require_once('homePage/admin_menu.inc.php');
      }else{
	    require_once('homePage/user_menu.inc.php');
      }
?>