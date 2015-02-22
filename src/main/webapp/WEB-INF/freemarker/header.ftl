<#import "spring.ftl" as spring />
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<#setting number_format="0">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<#if refreshInterval??>
		
	  		<META HTTP-EQUIV="Refresh" CONTENT="${refreshInterval}">
	  	
	</#if>
	<title><@spring.message code = "title"/></title>

	<script src='<@spring.url relativeUrl="/resources/js/util.js"/>' type="text/javascript"></script>

	<link href='<@spring.url relativeUrl="/resources/css/style.css"/>' rel="stylesheet" type="text/css">
	
</head>
<body>
<a href="./?lang=en">English</a>&nbsp|&nbsp
<a href="./?lang=ru">Русский</a>&nbsp&nbsp&nbsp 
${.now?date}&nbsp
<@security.authorize access="isAuthenticated()">
<@spring.message code = "controls.loggedas"/> &nbsp"<@security.authentication property="name"/>"&nbsp&nbsp&nbsp
<a href= '<@spring.url relativeUrl="/monitor/"/>'> <@spring.message code = "controls.monitor"/> </a>|
</@security.authorize>
<@security.authorize ifAnyGranted="ROLE_ADMIN">
&nbsp|<a href= '<@spring.url relativeUrl="/monitorusers/"/>'> <@spring.message code = "controls.monitorusers"/> </a>|
&nbsp|<a href= '<@spring.url relativeUrl="/monitorservers/"/>'> <@spring.message code = "controls.monitorservers"/> </a>|
&nbsp|<a href= '<@spring.url relativeUrl="/changesettings/"/>'> <@spring.message code = "controls.changesettings"/> </a>|
</@security.authorize>
<@security.authorize access="isAuthenticated()">
&nbsp|<a href= '<@spring.url relativeUrl="/changepassword/"/>'> <@spring.message code = "controls.changepassword"/> </a>|
&nbsp|<a href= '<@spring.url relativeUrl="/j_spring_security_logout"/>'> <@spring.message code = "controls.logout"/> </a>|
</@security.authorize>
<br><br><br>
