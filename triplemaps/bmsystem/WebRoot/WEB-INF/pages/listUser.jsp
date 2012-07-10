
<%@page import="org.bmsys.form.UserCommand"%><%@include file="/resources/common/include.jsp"%>
<html>
	<head>
		<title>
			<spring:message code="listuser.title"/>
		</title>
		<link rel="stylesheet" type="text/css" href="resources/css/style.css">
		<link rel="stylesheet" type="text/css" href="resources/css/displaytag.css">
		<link rel="stylesheet" type="text/css" href="resources/css/alternative.css">
	</head>
	<body>
		<%@include file="/resources/common/header.jsp"%>
		<%@include file="/resources/common/leftpanel.jsp"%>
		<div id="content">
			<%
				if(request.getParameter("s") != null)
				{
						String message = "User deleted Successfully.";
						int status = new Integer(request.getParameter("s")).intValue();
						if(status < 0)
							message = "System error has occured. Please try again later.";
			%>
				<script type="text/javascript">
					alert('<%=message%>');
				</script>
			<% 
				}
			 %>
			<div align="right" style="width: 80%;">
				<a href="showAddUser.htm"><spring:message code="listuser.label.adduser"/></a>
			</div>
			<display:table id="user" name="usersList" pagesize="50" export="true" sort="list">  
     			<display:column property="firstName" title="First Name" sortable="true" headerClass="sortable" />  
     			<display:column property="middleName" title="Middle Name" sortable="true" headerClass="sortable" />  
     			<display:column property="lastName" title="Last Name" sortable="true" headerClass="sortable" />  
     			<display:column property="reccuringAmount" title="Recurring Amount" sortable="true" headerClass="sortable" />
     			<display:column property="contactNo" title="Contact No" sortable="true" headerClass="sortable" />
     			<display:column title="Edit" headerClass="sortable">
     				<a href="showEditUser.htm?id=<%=((UserCommand)user).getUserId()%>"><spring:message code="listuser.label.edituser"/></a>
     			</display:column>    
     			<display:column title="Delete" headerClass="sortable">
     				<a href="deleteUser.htm?id=<%=((UserCommand)user).getUserId()%>"><spring:message code="listuser.label.deleteuser"/></a>
     			</display:column>
 			</display:table>
		</div>
	</body>
</html>