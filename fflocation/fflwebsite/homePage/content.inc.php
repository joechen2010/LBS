<?php 
      /**
	Checks if the $content is equals to one of these strings, 
	if yes then calls the file wich has the same name with the string 
      */
      if( $content == "main" or $content == "googleMapPositions" or $content == "showFriendUserAccount" or
	  $content == "searchFriendUser" or $content == "searchFriendUserTable" or $content == "showAllUsersTable" or
	  $content == "showMyAccount" or $content == "newAdmin" or $content == "submitChanges" or $content == "deleteUser")
	    require_once("content/".$content.".inc.php");
?>
