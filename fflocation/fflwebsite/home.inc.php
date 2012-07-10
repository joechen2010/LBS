<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
      <head>
	    <meta content="text/html; charset=ISO-8859-1" http-equiv="content-type"/>
	    <META NAME="ROBOTS" CONTENT="NOARCHIVE"> 

	    <title>Home Page</title>
	    <script type="text/javascript" src="js/exists.js"></script>

	    <link rel="shortcut icon" href="../favicon.ico">
	    <link rel="stylesheet" type="text/css" href="style/style.css">
	    <link rel="stylesheet" type="text/css" href="style/struct.css">

	    <?php 
		  /**
		      Depending on the $_GET['c'], the $content is going to have this value, otherwise "main"
		  */
		  if (!isset($_GET['c'])){
			$content = "main";
		  }else if ($_GET['c'] == "googleMapPositions"){
			$content = "googleMapPositions";
		  }else if($_GET['c'] == "searchFriendUser"){
			$content = "searchFriendUser";
		  }else if($_GET['c'] == "searchFriendUserTable"){
			$content = "searchFriendUserTable";
		  }else if($_GET['c'] == "showAllUsersTable"){
			$content = "showAllUsersTable";
		  }else if(($_GET['c'] == "showFriendUserAccount")){
			$content = "showFriendUserAccount";
		  }else if(($_GET['c'] == "deleteUser")){
			$content = "deleteUser";
		  }else if(($_GET['c'] == "submitChanges")){
			$content = "submitChanges";
		  }else if($_GET['c'] == "newAdmin"){
			$content = "newAdmin";
		  }else if($_GET['c'] == "showMyAccount"){
			$content = "showMyAccount";
		  }else{
			$content = "main";
		  }
	    ?>

	    <!-- style  and Javascript which are needed for the Google Map -->
	    <style type="text/css"> 
		html { height: 100% }
		body { height: 100%; margin: 0px; padding: 0px }
		#map_canvas { height: 50% }
	    </style>

	    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
      </head>
      
      <!--To ensure that our map is placed on the page after the page has fully loaded, 
         we only execute the function which constructs the Map object once the <body> element of the HTML page receives an onload event-->
      <body onload='if(document.getElementById("map_canvas") != null) initialize(); init();'>
	    <div id="div_head">	
		  <br /><img src="images/logo.png" alt="logo"></p>
		  <hr id="hr_head"/>
	    </div>	

	    <div id="div_main">
		  <div id="div_menu">
			<p class="user">Welcome User: <?php echo $_SESSION['userID']->userInformation()->getNick(); ?><br /></p>
			<?php require_once("homePage/menu.inc.php");?>
			<p class="con">
			      <form name="give_login" action="." method="POST">
			      <input name='logout' value="logout" type="submit" />
			      </form>
			</p>			    
		  </div>

		  <div id="div_content">
			<?php require_once("homePage/content.inc.php");?>	
		  </div>	
	    </div>

	    <div id="div_tail" >	
		  <hr id="hr_head"/>
		  <h4><bold> Copyright 2010 Erasmus Students of Computer Science | Roskilde University</bold></h4> 	
	    </div>
      </body>
</html>
