<!-- Here are set the links that the Administrator can have in his menu -->
<br/><br/>
<a href="?c=main"
      <?php if($content=='main'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Home Page</a><br /><br />

<a href="?c=googleMapPositions"
      <?php if($content=='googleMapPositions'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Users Last Position</a><br /><br />

<a href="?c=searchFriendUser"
      <?php if($content=='searchFriendUser'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Search User</a><br /><br />

<a href="?c=showAllUsersTable"
      <?php if($content=='showAllUsersTable'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Show All Users</a><br /><br />

<a href="?c=showMyAccount"
      <?php if($content=='showMyAccount'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Show/Change Account</a><br /><br />

<a href="?c=newAdmin"
      <?php if($content=='newAdmin'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Create new Admin</a><br /><br /><br />