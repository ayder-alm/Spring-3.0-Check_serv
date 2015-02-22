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

<@spring.message code = "controls.loggedas"/> &nbsp"<@security.authentication property="name"/>"&nbsp&nbsp&nbsp

${.now?date}

&nbsp|<a href= '<@spring.url relativeUrl="/monitor/"/>'> <@spring.message code = "controls.monitor"/> </a>|

<@security.authorize ifAnyGranted="ROLE_ADMIN">
&nbsp|<a href= '<@spring.url relativeUrl="/monitorusers/"/>'> <@spring.message code = "controls.monitorusers"/> </a>|
&nbsp|<a href= '<@spring.url relativeUrl="/monitorservers/"/>'> <@spring.message code = "controls.monitorservers"/> </a>|
&nbsp|<a href= '<@spring.url relativeUrl="/changesettings/"/>'> <@spring.message code = "controls.changesettings"/> </a>|
</@security.authorize>
&nbsp|<a href= '<@spring.url relativeUrl="/changepassword/"/>'> <@spring.message code = "controls.changepassword"/> </a>|
&nbsp|<a href= '<@spring.url relativeUrl="/j_spring_security_logout"/>'> <@spring.message code = "controls.logout"/> </a>|

<br><br><br>
	
	<form action="<@spring.url relativeUrl="/edituser/"/>" method="POST">
	
	<input type = "hidden" name="_active" >
	<input type = "hidden" name="_admin" >
	<input type = "hidden" name = "id" value = "${user.id}">

	<table>
		<tr>
			<#if editor == "admin">
      			<th colspan = '4'>
      		<#else>
      			<th colspan = '3'>
      		</#if> 
      			<b> <@spring.message code = "edituser.table.title"/> "${user.name}" </b>
    	  		<#if user.admin != true> 
					&nbsp&nbsp<a href = "<@spring.url relativeUrl="/assignservers/${user.id}/"/>">[<@spring.message code = "edituser.assignservers"/>]</a>
    			</#if> 
      		</th>
    	</tr>
    	<tr>
      		<th><@spring.message code = "name"/></th>
      		<th><@spring.message code = "email"/></th>
      		<th><@spring.message code = "active"/></th>
      		<#if editor == "admin">
      			<th><@spring.message code = "admin"/></th>
      		</#if>
    	</tr>

<#if user??>
    	<tr>
			<td> <input type="text" name="name" value="${user.name}" > </td>
			<td> <input type="text" name="email" value="${user.email}"> </td>
			<#if editor != "admin">
					<#if user.admin == false>
						<#if user.active == true>
							<td> <center><input type="checkbox" name="active" checked="checked"></center> </td>
						<#else>
							<td> <center><input type="checkbox" name="active"></center> </td>
						</#if>
					<#else>
						<td>
							<center><input type="checkbox" checked="checked" disabled="disabled"></center>
							<input type = "hidden" name = "active" value = "checked">
						</td>
					</#if>
			<#else>
					<#if user.login == "admin">
						<td>
						 	<center><input type="checkbox" checked="checked" disabled="disabled"></center>
						 	<input type = "hidden" name = "active" value = "checked">
						</td>
						<td>
						 	<center><input type="checkbox" checked="checked" disabled="disabled"></center>
						 	<input type = "hidden" name = "admin" value = "checked">
						</td>				
					<#else>
						<#if user.active == true>
							<td> <center><input type="checkbox" name="active" checked="checked"></center> </td>
						<#else>
							<td> <center><input type="checkbox" name="active"></center> </td>
						</#if>
						<#if user.admin==true>
							<td><center><input type="checkbox" name="admin" checked="checked"></center> </td>
						<#else>
							<td> <center><input type="checkbox" name="admin"></center> </td>
						</#if>
					</#if>
			</#if>
	 	</tr>	
</#if>

    </table>
    <hr>
    <center>
    	<input type="submit" value="<@spring.message code = "submit"/>">
	</center>
	</form>
	<br>
	<@spring.bind "user.name"/>
	<center><@spring.showErrors "<br>" "error"/></center>
	<@spring.bind "user.email"/>
	<center><@spring.showErrors "<br>" "error"/></center>
	<center>
	  <form action='<@spring.url relativeUrl="/monitorusers/"/>'>
			<input type="submit" value = "<@spring.message code = "cancel"/>">
	  </form>
	</center>
<#include "footer.ftl">