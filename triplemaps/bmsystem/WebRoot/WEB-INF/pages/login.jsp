<%@include file="/resources/common/include.jsp"%>
<html>
	<head>
		<title><spring:message code="index.title"></spring:message>
		</title>
		<link rel="stylesheet" type="text/css" href="resources/css/style.css">
	</head>
	<body>
		<div id="loginTop">
			<spring:message code="index.title"></spring:message>
		</div>
		<div>
			<div id="loginBox" align="center">
				<div style="margin: 10px;" class="errorMsg">
					<spring:bind path="errors">
						<c:forEach items="${errors.globalErrors}" var="error">
        					${error.defaultMessage}<br />
						</c:forEach>
					</spring:bind>
				</div>
				<form:form action="processLogin.htm" method="post"
					commandName="loginCommand">
					<div>
						<label>
							<spring:message code="index.label.username"></spring:message>
							:
						</label>
						<form:input path="userName" />
					</div>
					<div style="margin-top: 5px;">
						<label>
							<spring:message code="index.label.password"></spring:message>
							:
						</label>
						<form:password path="password" />
					</div>
					<div style="margin-top: 5px;">
						<input type="submit" name="btnsubmit"
							value='<spring:message code="index.label.btnsubmit"/>'
							id="btnsubmit" />
					</div>
				</form:form>
			</div>
		</div>
	</body>
	<script type="text/javascript">
  	userName.focus();
  </script>
</html>