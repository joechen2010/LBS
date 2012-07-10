<?php
    /**
      If the user presse submit the $_POST['submit_id'] is going to be set
      and the user information are going to be changed depending on what 
      the user has changed to his account information, when the service
      "changeUser" is going to be called.Finally a Javascript is going to 
      be called so as to inform that the submition is succesful.
    */
    if( isset($_POST['submit_id']) ){
	  if( ( strlen($_POST['ch_password']) == 0 or strlen($_POST['ch_password']) >= 4 ) ){
		$userTemp = $_SESSION['userID']->userInformation();
                $userTemp->setName($_POST['name']);
		$userTemp->setSurname($_POST['surname']);
		$userTemp->setCountry($_POST['country']);
		$userTemp->setEmail($_POST['email']);
		$userTemp->setPhone($_POST['phone']);
		$userTemp->setAddress($_POST['address']);
		
		$pass = $_POST['ch_password'];
		$_SESSION['userID']->changeUser($userTemp,$pass);
		echo '<script type="text/javascript"> alert ("Submition completed succsesfully!")</script> ';
	  }
	  else{
		echo '<script type="text/javascript">alert ("Password did not change!! It should be more than 3 characters")</script>'; 
	  }
	  require_once("showMyAccount.inc.php");
    }
?>