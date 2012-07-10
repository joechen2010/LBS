<%@include file="/resources/common/include.jsp"%>
<html>
  <head>
    <title>System Configuration - Loan Management System</title>
    <link rel="stylesheet" type="text/css" href="resources/css/style.css">
  </head>
  <body>
   		<%@include file="/resources/common/header.jsp"%>
		<%@include file="/resources/common/leftpanel.jsp"%>
    	<div id="content">
    		<form action="" method="post">
    			<table>
    				<tr>
    					<td><label>Penalty Rate:</label></td>
    					<td>
    						<input type="text" name="firstName" value="">
    					</td>
    				</tr>
    				<tr>
    					<td><label>Loan Rate:</label></td>
    					<td><input type="text" name="middleName" value=""></td>
    				</tr>
    				<tr>
    					<td><label>Premium Paid Date:</label></td>
    					<td><input type="text" name="premiumDate" value=""></td>
    				</tr>
    				<tr>
    					<td><label>Loan Paid Date:</label></td>
    					<td><input type="text" name="loanDate" value=""></td>
    				</tr>
    				<tr>
    					<td>&nbsp;</td>
    					<td>
    						<input type="submit" name="btnSubmit" value="Setup Rule">
    					</td>
    				</tr>
    			</table>
    		</form>
    	</div>
    	<%@include file="/resources/common/footer.jsp"%>
  </body>
</html>