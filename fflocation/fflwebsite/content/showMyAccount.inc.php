<div>
      <p class="con" style="font-size:xx-large;"><i>Show/Change Account</i></p>
      <hr class="con"/>
</div>

<div style='float:left; width=50%;  margin-right:10%'>
      <p class="con"></p>
</div>

<div style='margin-top:30px; float:left; width=100%;  margin-right:10%'>
      <p class="con" style="float:left">
      <?php
	    /**
	      Shows the account information of the logged in user where he has also the 
	      possibility to change his information account and then to press "Submit"
	      so as to be submited.
	    */	
	    $user_account = $_SESSION['userID']->userInformation();
      echo '<form method="post" action="?c=submitChanges" id="UserFormAccount"">
		  <table id="showmyaccount" style="width:100%;" border="1" bgcolor="#fff" cellspacing="3" cellpadding="3">
			<tr bgcolor="red">
			      <td></td>
			      <td>User Information</td>
			</tr><tr>
			      <td>Nick :</td>
			      <td><input type="text" name="nick" disabled="disabled" value="'.$user_account->getNick().'" /></td>
			</tr><tr>
			      <td>Password :</td>
			      <td><input type="password" name="ch_password" value="" /></td>
			</tr><tr>
			      <td>Name :</td>
			      <td>';
				    if($user_account->getName()==null){
					  echo '<input type="text" name="name" value="" />';
				    }else{
					  echo '<input type="text" name="name" value="'.$user_account->getName().'" />';
				    } echo '</td>
			</tr><tr>
			      <td>Surname :</td>
			      <td>';
				    if($user_account->getSurname()==null){
					  echo '<input type="text" name="surname" value="" />';
				    }else{
					  echo '<input type="text" name="surname" value="'.$user_account->getSurname().'" />';
				    } echo '</td>
			</tr><tr>
			      <td>Email :</td>
			      <td>';
				    if($user_account->getEmail()==null){
					  echo '<input type="text" name="email" value="" />';
				    }else{
					  echo '<input type="text" name="email" value="'.$user_account->getEmail().'" />';
				    } echo '</td>
			</tr><tr>
			      <td>Phone Number :</td>
			      <td>';
				    if($user_account->getPhone()==null){
					  echo '<input type="text" name="phone" value="" />';
				    }else{
					  echo '<input type="text" name="phone" value="'.$user_account->getPhone().'" />';
				    } echo '</td>
			</tr><tr>
			      <td>Country :</td>
			      <td>';
				    if($user_account->getCountry()==null){
					  echo '<input type="text" name="country" value="" />';
				    }else{
					  echo '<input type="text" name="country" value="'.$user_account->getCountry().'" />';
				    } echo '</td>
			</tr><tr>
			      <td>Address :</td>
			      <td>';
				    if($user_account->getAddress()==null){
					  echo '<input type="text" name="address" value="" />';
				    }else{
					  echo '<input type="text" name="address" value="'.$user_account->getAddress().'" />';
				    } echo '</td>		      
			</tr><tr bgcolor="red">
			      <td></td>
			      <td>
				    <input style="text=align:left;" name="back_submit" type="button" value="Back"
								      onClick="document.location.href=\'?c=main\'"/>
				    <input style="text=align:left;" name="submit_id" type="submit" value="Submit"/>
				    <input name="submition_id" value="'.$user_account->getID().'" type="hidden"/>
			      </td>
			</tr>
		  </table>
	    </form>';
      ?>
</p><br /><br /><br />
</div> 

<div style='float:left; width=50%; margin-top:30px'>
      <p class="con">
	    <img src="images/latitude.jpeg" alt="latitude" style="width:300px; height: 250px;">   
      </p>
</div>