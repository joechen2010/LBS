<?php
      /**
	Checks if the $_POST['a_newAdmin'] is set and the other $_POST lengths are more than 2.
	If it's true then an new Object of UserInfo is made and there are set the information
	of the new admin.
      */
      if( isset($_POST['a_newAdmin']) and ( strlen($_POST['a_nick']) >= 3 ) and ( strlen($_POST['a_password']) >= 4 ) ){
	    $nu = new UserInfo();

	    $nu->setNick($_POST['a_nick']);
	    $nu->setName($_POST['a_name']);
	    $nu->setEmail($_POST['a_email']);
	    $nu->setSurname($_POST['a_surname']);
	    $nu->setPhone($_POST['a_phone']);
	    $nu->setCountry($_POST['a_country']);
	    $nu->setAddress($_POST['a_address']);
	    $nu->setAdministrator(true);

	    if( !$_SESSION['userID']->exists($_POST['a_nick']) ){// Checks if a user with the same "nick" alreay exists if
								 // not then creates the admin.
		  if( $_SESSION['userID']->newAdmin($nu, $_POST['a_password']) )
			echo '<script language=javascript>alert("Admin account has been created!.")</script>';
		  else{
			echo '<script language=javascript>alert("Admin account could not be created!.")</script>';
		  }
	    }else{
		  echo '<script language=javascript>alert("Person with these info already exists!.")</script>';
	    }	
	    unset($_POST['a_newAdmin']);

      }else if( isset($_POST['a_newAdmin']) and ( strlen($_POST['a_nick']) < 3 ) and ( strlen($_POST['a_password']) < 4 )){	
	    echo '<script language=javascript>alert("Please fill at least nick with more than 3 characters and password
											      with more than 4 characters.")</script>';
	    unset($_POST['a_newAdmin']);
      }
      else if( isset($_POST['a_newAdmin']) ){
	    echo '<script language=javascript>alert("Please fill at least both nick and password")</script>';
	    unset($_POST['a_newAdmin']);
      }
?>




 
