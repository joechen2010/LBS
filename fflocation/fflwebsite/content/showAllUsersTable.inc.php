<div>
      <?php
	    if( $_SESSION['userID']->userInformation()->isAdministrator() ){		  
		  echo '<p class="con" style="font-size:xx-large;"><i>List of all the users in the application</i></p>';
	    }
	    else{
		  echo '<p class="con" style="font-size:xx-large;"><i>List of all the friends</i></p>';
	    }
      ?>
      <hr class="con"/>
</div>

<div style="margin-top:30px;">
      <p class="con" style="float:left">
      <?php
	    /**
	      The first time this file is going to be required the $page is going to be 0
	      because the $_GET['page'] has not been set. The depending on the user 
	      (simple user or Administrator) is going to call different services
	      (getFriendsPage or getUsers respectively). To these services are going to be given
	      as parameters the number of users to be shown on each page and the number of the page.
	      The users are going to be shown in a table with some of their account information.
	      The person who has logged in, is going to be given the ability:
	      To see the whole account information of a user.
	      Show the History of the last positions of a user in a map.
	      Finally,the value of $page can be changed if the users presses "next" or "previous"
	      depending on how many users are shown and in which page the logged in user is.
	    */
	    if(isset($_GET['page'])){ 
		  $page = $_GET['page'];
	    }else{
		  $page = 0;
	    }
	    $counter = 10*$page; //is needed so as to show a counter left of the users account. 

	    if($_GET['c'] == "showAllUsersTable"){
		  if( $_SESSION['userID']->userInformation()->isAdministrator() ){
			$searchResponse = $_SESSION['userID']->getUsers(10,$page);
		  }
		  else{
			$searchResponse = $_SESSION['userID']->getFriendsPage(10,$page);
		  }
		  $_SESSION['userid_response'] = $searchResponse;
		  $array_length = count($searchResponse);
		  if($array_length == 0){
			echo 'No people found';
		  }else{
		  echo '<table id="showuser" style="width:100%;" border="1" bgcolor="#fff" cellspacing="3" cellpadding="3">
			      <tr bgcolor="red">
				    <td>Num</td>
				    <td>Nickname</td>
				    <td>Name</td>
				    <td>Surname</td>
				    <td>Country</td>
				    <th colspan=2 >Button Actions</th> 
			      </tr>';
			for($i=0; $i<$array_length;$i++) {
				$value = $searchResponse[$i];	 
			echo '<tr>
				    <td>'.(++$counter).'</td>
				    <td>'.$value->getNick().'</td>
				    <td>'.$value->getName().'</td>
				    <td>'.$value->getSurname().'</td>
				    <td>'.$value->getCountry().'</td>
				    <td>';
				    echo '<form name="actionfriend" action="?c=showFriendUserAccount" method="POST">';
					echo '<input style="text=align:left;" name="showInfo" type="submit" value="Info"/>
					      <input name="id_info" value="'.$value->getID().'" type="hidden"/>
					  </form>
				    </td>
				    <td text-align:left">
					  <form name="actionfriend" action="?c=googleMapPositions" method="POST">';
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
				  <th colspan=2 >';
				      if($page == 0){	    
					    echo '<input style="text=align:left;" type="button" disabled="disabled" value= "previous" />'; 
					  if( $array_length < 10 ) {	    
					    echo '<input style="text=align:right;" type="button" disabled="disabled" value= "next"/>'; 
					  }else{
					      echo '<input style="text=align:right;" type="button" value= "next" 
						onClick="document.location.href=\'?c=showAllUsersTable&page='.($page+1).'\'"/>';
					  }
				      }else if($page != 0){	 
					    echo '<input style="text=align:left;" type="button" value= "previous" 
					      onClick="document.location.href=\'?c=showAllUsersTable&page='.($page-1).'\'"/>';
					if( $array_length < 10 ) {	    
					    echo '<input style="text=align:right;" type="button" disabled="disabled" value= "next"/>'; 
					}else {
					    echo '<input style="text=align:right;" type="button" value= "next" 
					      onClick="document.location.href=\'?c=showAllUsersTable&page='.($page+1).'\'"/>';
					}
				      }
				  echo '</th> 
			      </tr>
			</table>';
		  }	
	    }
      ?>
</p> 





 
