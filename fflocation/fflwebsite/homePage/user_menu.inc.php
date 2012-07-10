<!-- Here are set the links that the simple user can have in his menu -->
<br/><br/>
<a href="?c=main"
      <?php if($content=='main'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Home Page</a><br /><br />

<a href="?c=googleMapPositions"
      <?php if($content=='googleMapPositions'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Friends Last Position</a><br /><br />

<a href="?c=showAllUsersTable"
      <?php if($content=='showAllUsersTable'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Show All Friends</a><br /><br /> 

<a href="?c=showMyAccount"
      <?php if($content=='showMyAccount'){echo 'id="mn_selected" ';} else {echo 'class="mn_item"';} ?>
      >Show/Change Account</a><br /><br /><br />