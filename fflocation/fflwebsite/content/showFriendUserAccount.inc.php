<div>
      <?php
	    if( $_SESSION['userID']->userInformation()->isAdministrator() ){		  
		  echo '<p class="con" style="font-size:xx-large;"><i>User Account Information</i></p>';
	    }
	    else{
		  echo '<p class="con" style="font-size:xx-large;"><i>Friend Account Information</i></p>';
	    }
      ?>
      <hr class="con"/>
</div>

<div style='float:left; width=10%;  margin-right:10%'>
      <p class="con"></p>
</div>

<div style='margin-top:30px; float:left; width=100%;  margin-right:15%'> 
      <p class="con" style="float:left">
      <?php
	    /**
	      If the $_POST['showInfo'] is set then searches the users so as
	      to find the user with the specific userID, which he has chosen,
	      and finally the user's whole account information are shown in a
	      table. If the user who has logged in is Administrator is also going to 
	      show information about if the searched user is an admin or not.
	    */
	    if( isset($_POST['showInfo']) ){
		  $searchResponse1 = $_SESSION['userid_response'];
		  $array_length1 = count($searchResponse1);

		  for($i=0; $i<$array_length1; $i++) {
			if($searchResponse1[$i]->getID() == $_POST['id_info'])
			      $user_info = $searchResponse1[$i];  
		  }
		  echo '<table id="showuserinfo" style="width:100%;" border="1" bgcolor="#fff" cellspacing="3" cellpadding="3">
			      <tr bgcolor="red">
				    <td></td>
				    <td>User Information</td>
			      </tr><tr>
				    <td>User Id :</td>
				    <td>';
					  if($user_info->getID()==null){ echo '  -';}
					  else{ echo '  '.$user_info->getID();} echo '</td>
			      </tr><tr>
				    <td>NickName :</td>
				    <td>';
					  if($user_info->getNick()==null){ echo '  -';}
					  else{ echo '  '.$user_info->getNick();} echo '</td>
			      </tr><tr>
				    <td>Name :</td>
				    <td>';
					  if($user_info->getName()==null){ echo '  -';}
					  else{ echo '  '.$user_info->getName();} echo '</td>
			      </tr><tr>
				    <td>Surname :</td>
				    <td>';
					  if($user_info->getSurname()==null){ echo '  -';}
					  else{ echo '  '.$user_info->getSurname();} echo '</td>
			      </tr><tr>
				    <td>Email :</td>
				    <td>';
					  if($user_info->getEmail()==null){ echo '  -';}
					  else{ echo '  '.$user_info->getEmail();} echo '</td>
			      </tr>
			      </tr><tr>
				    <td>Phone :</td>
				    <td>';
					  if($user_info->getPhone()==null){ echo '  -';}
					  else{ echo '  '.$user_info->getPhone();} echo '</td>
			      </tr>
			      </tr><tr>
				    <td>Country :</td>
				    <td>';
					  if($user_info->getCountry()==null){ echo '  -';}
					  else{ echo '  '.$user_info->getCountry();} echo '</td>
			      </tr>
			      </tr><tr>
				    <td>Address :</td>
				    <td>';
					  if($user_info->getAddress()==null){ echo '  -';}
					  else{ echo '  '.$user_info->getAddress();} echo '</td>
			      </tr>';
				    if($_SESSION['userID']->userInformation()->isAdministrator()){
			      echo '<tr>
				      <td >Administrator :</td>
				      <td>';
				      if($user_info->isAdministrator()){ echo '  Yes';}
				      else{ echo '  No';} echo '</td>
				    </tr>';
				    }
			      echo '<tr bgcolor="red"><th colspan="2"></th></tr>
			</table>';
		    unset($_POST['showInfo']);
	      }
	?>
</p><br /><br /><br />
</div>

<div style='float:left; width=30%; margin-top:30px'>
      <p class="con">
	    <img src="images/latitude.jpeg" alt="latitude" style="width:300px; height: 250px;">   
      </p>
</div>