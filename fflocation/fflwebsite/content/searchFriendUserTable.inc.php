<div>
      <p class="con" style="font-size:xx-large;"><i> Searched Users</i></p>
      <hr class="con"/>
</div>

<div style="margin-top:30px;"> 
      <p class="con" style="float:left">
      <?php
	    /**
	      If the $_GET['c'] equals to "actionFriend user" then the searchFriend service
	      called so as to return all the users who match with the info given. Then the 
	      users account information are going to be shown in a map where the admin can
	      make some actions like:
	      Deleting a user if he is not an Administrator
	      Showing the whole account information of a user
	      Show the History of the last positions of a user in a map.
	    */
	    if ($_GET['c'] == "searchFriendUserTable"){
		  $search_nick = $_POST['search_nick'];
		  $search_name = $_POST['search_name'];
		  $search_surname = $_POST['search_surname'];
		  $search_country = $_POST['search_country'];
		  $counter = 0;
		  $searchResponse = $_SESSION['userID']->searchFriend($search_nick, $search_name, $search_surname, $search_country);
		  $_SESSION['userid_response'] = $searchResponse;
		  $array_length = count($searchResponse);

		  if($array_length == 0){
			echo 'No matches found with these details';
		  }else{
		  echo '<table id="sfrienduser" style="width:100%;" border="1" bgcolor="#fff" cellspacing="3" cellpadding="3">
			      <tr style="height:100%;" bgcolor="red">		     
				    <td>#</td>
				    <td>Nickname</td>
				    <td>Name</td>
				    <td>Surname</td>
				    <td>Country</td>
				    <th colspan=3 >Button Actions</th> 
			      </tr>';
			for( $i = 0; $i < $array_length; $i++ ) {
				$value = $searchResponse[$i];  
			echo '<tr style="height:100%;">
				    <td>'.(++$counter).'</td>
				    <td>'.$value->getNick().'</td>
				    <td>'.$value->getName().'</td>
				    <td>'.$value->getSurname().'</td>
				    <td>'.$value->getCountry().'</td>
				    <td>';
				    echo '<form name="actionfriend" action="?c=deleteUser" method="POST">';
						if( ( !$value->isAdministrator() ) ){
						    echo '<input style="text=align:left;" name="delete" type="submit" value="Delete"/>';
						}
						else{
						    echo '<input style="text=align:left;" disabled="disabled" name="delete" 
													type="submit" value="Delete"/>';
						}
						echo '<input name="id" value="'.$value->getID().'" type="hidden"/>
					  </form>
				    </td>
				    <td text-align:left">
					      <form name="positionfriend" action="?c=showFriendUserAccount" method="POST">
						    <input style="text=align:left;" name="showInfo" type="submit" value="Info"/>
						    <input name="id_info" value="'.$value->getID().'" type="hidden"/>
					      </form>
				    </td><td text-align:left">
					      <form name="positionfriend" action="?c=googleMapPositions" method="POST">';
						  if( ( !$value->isAdministrator() ) ){
						     echo '<input style="text=align:left;" name="showHistory" type="submit" value="History"/>';
						  }
						  else{
						     echo '<input style="text=align:left;" disabled="disabled" name="showHistory" 
													type="submit" value="History"/>';
						  }
						  echo '<input name="id_history" value="'.$value->getID().'" type="hidden"/>
					      </form>
				    </td>
			      </tr>';
			}
			unset($value);
			echo '<tr bgcolor="red">
				    <td></td>
				    <td></td>
				    <td></td>
				    <td></td>
				    <td></td>
				    <th colspan=3 ></th> 
			      </tr>
			</table>';
		  }
	    }
      ?>
      </p><br />
</div>