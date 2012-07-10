<?php 
      /**
	  Checks if the $_POST['newUser'] is set and the other $_POST lengths are more than 2.
	  If it's true then an new Object of UserInfo is made and there are set the information
	  of the new user.
      */
      if( isset($_POST['newUser']) and ( strlen($_POST['nick']) >= 3 ) and ( strlen($_POST['pw']) >= 4 ) ){
	    $nu = new UserInfo();

	    $nu->setNick($_POST['nick']);
	    $nu->setName($_POST['name']);
	    $nu->setEmail($_POST['email']);
	    $nu->setSurname($_POST['surname']);
	    $nu->setPhone($_POST['phone']);
	    $nu->setCountry($_POST['country']);
	    $nu->setAddress($_POST['address']);
	    $nu->setAdministrator(false);

	    $_SESSION['userID'] = new ToServer(); //creation of the ToServer object.
	    if( !$_SESSION['userID']->exists($_POST['nick']) ){  // Checks if user with the same "nick" alreay exists if
								 // not then creates the user and logins to the application 
		  $_SESSION['userID']->newUser($nu, $_POST['pw']);
		  if( $_SESSION['userID']->logged() ){
			$_SESSION['nick'] = $_POST['nick'];
			echo '<script language=javascript>alert("Your account has been created!.")</script>';
		  }
	    }else{
		  unset($_SESSION['userID']); //unsets the $_SESSION['userID'] when a user already exists with these info
		  echo '<script language=javascript>alert("User with these info already exists!.")</script>';
	    }	
	    unset($_POST['newUser']);
	    unset($_POST['nick']);
	    unset($_POST['pw']);
      }else if( isset($_POST['newUser']) and ( strlen($_POST['nick']) < 3 ) and ( strlen($_POST['pw']) < 4 )){	
	    echo '<script language=javascript>alert("Please fill at least nick with more than 3 characters and password
											      with more than 4 characters.")</script>';
	    unset($_POST['newUser']);
      }
      else if( isset($_POST['newUser']) ){
	    echo '<script language=javascript>alert("Please fill at least both nick and password")</script>';
	    unset($_POST['a_newAdmin']);
      }
?>




