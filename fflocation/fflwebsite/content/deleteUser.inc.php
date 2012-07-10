<?php
    /**
      Deletes the user and unsets the $_POST['delete'], and finally 
      calls a javascript to show that the deletion was succesful.
    */
    if(isset($_POST['delete'])){
	$_SESSION['userID']->delUser($_POST['id']);
	unset($_POST['delete']);
	echo '<script type="text/javascript">alert ("User deleted succsesfully!!")</script>'; 
    }
    else{
	echo '<script type="text/javascript">alert ("Error while tryng to delete")</script>'; 
    }
    require_once("searchFriendUser.inc.php");
?>
 