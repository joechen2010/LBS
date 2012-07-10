<div>
      <p class="con" style="font-size:xx-large;"><i>Create new Administrator</i></p>
      <hr class="con"/>
</div>

<div style='float:left;width=50%;margin-right:10%;margin-top:30px;'>
			      <p class="con"></p>
</div>

<div style='float:left; width=30%;  margin-right:20%; margin-top:30px;'>
      <p class="con" style="font-size:x-large; text-decoration: underline;">
	    <b>Create Admin:</b><br />
      </p>
      <p class="con">
	    Create a new administrator by<br />
	    filling the form on the <br />
	    right with the new admin's<br />
	    account information. <br />
      </p>
</div>
<div style="margin-top:30px; float:left; width=50%;  margin-right:5%">
      <p class="con" style="float:left">
	    <form method='post' action='?c=newAdmin' id='newAdminForm' style='text-align:left;'>
		  <table border='0' cellpadding='2' cellspacing='2'>
			<tr id='exists' bgcolor=''>
			      <td>Nick:</td>
			      <td><input type="text" maxlength="100" id="a_nick" name="a_nick" 
						    onkeyup="question('a_nick')" autocomplete="off"/></td>
			</tr><tr>
			      <td>Password:</td>
			      <td><input type='password' name='a_password' value='' /></td>
			</tr><tr>
			      <td>Name:</td>
			      <td><input type='text' name='a_name' value='' /></td>
			</tr><tr>
			      <td>Surname:</td>
			      <td><input type='text' name='a_surname' value='' /></td>
			</tr><tr>
			      <td>Email:</td>
			      <td><input type='text' name='a_email' value='' /></td>
			</tr><tr>
			      <td>Phone:</td>
			      <td><input type='text' name='a_phone' value='' /></td>
			</tr><tr>
			      <td>Country:</td>
			      <td><input type='text' name='a_country' value='' /></td>
			</tr><tr>
			      <td>Address:</td>
			      <td><input type='text' name='a_address' value='' /></td>
			</tr><tr>
			      <td></td>						
			      <td><input type='submit' name='a_newAdmin' value='Create Admin' /></td>
			</tr>
		  </table>
	    </form>
      </p><br />
</div> 
