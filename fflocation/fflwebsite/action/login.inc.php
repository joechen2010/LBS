<?php
      /**
	  Checks if the $_POST['login'] is set. If yes, then stores to the session array the info
	  and the users logins, else destroys the session and then returns the user to the login page.
      */
      if( isset($_POST['login']) ){
	    $nick = $_POST['nick'];
	    $pw = $_POST['pw'];
	    
	    $_SESSION['nick'] = $nick;

	    $user = new ToServer();
	    $logged_in = $user->loginWP($nick,$pw);
	    if( $user->logged() ){
		    $_SESSION['userID'] = $user;
	    }
	    else{
		    echo '<script language=javascript>alert("'.$logged_in.'")</script>';
	    }
      }else if( isset($_POST['logout']) ){
	    session_destroy();
	    header('Location: index.php');
	    die();
      }
?>