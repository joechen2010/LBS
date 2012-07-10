<?php
      /**
	Sends a content that is xml and determines to not cache the answer.
      */
      header("Content-Type: text/xml");
      header("Cache-Control: no-cache, must-revalidate");
?>
<root>
      <?php
	    require_once('../web_services/toServer.php');
	    if( isset($_POST['nick'])){
		  $value = new ToServer();
		  if( $value->exists($_POST['nick']) ){
			  echo "<exists>yes</exists>";
		  }else{
			  echo "<exists>no</exists>";
		  }
	    }
      ?>
</root>
