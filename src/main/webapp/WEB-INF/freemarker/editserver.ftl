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
<hr>	
	<form action="<@spring.url relativeUrl="/editserver/"/>" method="POST">
	<input type = "hidden" name="_active">
	<#if server??> <input type = "hidden" name = "id" value = "${server.id}"> </#if> 
	<table>
		<tr>
      		<th colspan = '5'> <b> <@spring.message code = "editserver.table.title"/> <#if server??> "${server.name}" </#if> </b></th>       
    	</tr>
    	<tr>
      		<th><@spring.message code = "name"/></th>
      		<th><@spring.message code = "adress"/></th>
      		<th><@spring.message code = "port"/></th>
      		<th><@spring.message code = "url"/></th>
      		<th><@spring.message code = "active"/></th>	
    	</tr>
    	<tr>
    	<#if server??>
			<td> <input type="text" name="name" value="${server.name}" > </td>
			<td> <input type="text" name="adress" value="${server.adress}"> </td>
			<td> <input type="text" name="port" value="<#if server.port??>${server.port}</#if>" > </td>
			<td> <input type="text" name="urlPath" value="${server.urlPath}"> </td>
			<#if server.active=true>
				<td> <center> <input type="checkbox" name="active" checked="checked"> </center> </td>
			<#else>
				<td> <center> <input type="checkbox" name="active"> </center> </td>
			</#if>
		<#else>
			<td> <input type="text" name="name" value=""> </td>
			<td> <input type="text" name="adress" value=""> </td>
			<td> <input type="text" name="port" value=""> </td>
			<td> <input type="text" name="urlPath" value=""> </td>
			<td> <center> <input type="checkbox" name="Active" checked="checked"> </center> </td>
		</#if>
		</tr>
    </table>
    <hr>
    <center>
	<input type="submit" value="<@spring.message code = "submit"/>">
	</center>
	
	<#if server??>
		<@spring.bind "server.name"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "server.adress"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "server.port"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "server.urlPath"/>
		<center><@spring.showErrors "<br>" "error"/></center>
    </#if>	
	
	<#if duplication??>
	<table>
		<tr>
      		<th class="error"> <b> <@spring.message code = "error.server.duplication"/> </b> </th>       
    	</tr>
	</table>
	</#if>
	</form>
	<br>
	<center>
	  <form action='<@spring.url relativeUrl="/monitorservers/"/>'>
			<input type="submit" value = "<@spring.message code = "cancel"/>">
	  </form>
	</center>
<#include "footer.ftl">