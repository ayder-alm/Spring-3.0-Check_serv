<#import "spring.ftl" as spring />
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

	<title><@spring.message code = "title"/></title>

	<script src='<@spring.url relativeUrl="/resources/js/util.js"/>' type="text/javascript"></script>

</head>
<body style="font-family:Arial,Helvetica,sans-serif; background-color:rgb(245,250,255); font-size:small;">
<a href="./?lang=en">English</a>&nbsp|&nbsp
<a href="./?lang=ru">Русский</a>&nbsp&nbsp&nbsp ${.now?date}

<br><br>
	  <@security.authorize access="!isAuthenticated()">
      <center>
      <table cellpadding="2" cellspacing="0" border="0">
      	<tr>
      	<td bgcolor="#aaaaff" >
		    <table cellpadding="0" cellspacing="2" border="0" width="100%" >
		      	
		      	<tr bgcolor="#ccccff" >
		      	  <th><b><@spring.message code = "login.title"/></b></th>       
		      	</tr>
		      	
		      	<tr bgcolor="white" align="left">
		          <td>
			          <form method="post" id="loginForm" action="<@spring.url relativeUrl="/login/"/>">
			          <br>
				        <table cellpadding='5' cellspacing='0' border='0' bgcolor="white">
				         
				         <tr>
				         	<td><@spring.message code = "login.login"/></td>
				         	<td><input type="text" name="j_username" maxlength="16" size="16" ></td>
				         </tr>
				         
				         <tr>
				         	<td><@spring.message code = "login.password"/></td>
				         	<td><input type="password" name="j_password" maxlength="16" size="16"></td>
				         </tr>
						 
						 <tr>
						 	<td></td>
						 	<td align="center"><input value="<@spring.message code = "submit"/>" type="submit"></td>
						 </tr>
						 
						 <tr>
						 	<td colspan="2"></td>
						 </tr>
						 
						 <tr>
						 	<td colspan="2"><@spring.message code = "login.lost"/>&nbsp<a href="<@spring.url relativeUrl="/restore/"/>"><@spring.message code = "login.lost.link"/></a>!</td>
						 </tr>
			         	
			         	</table>
			         	
		        	</form>
        	
        	
        		 </td>
      			</tr>
        	</table>
        </td>
      	</tr>
      	<#if error??>
      	<tr>
      		<td bgcolor="#ffaaaa"><@spring.message code = "login.error"/></td>
      	</tr>
      	</#if>
      </table>
      </center>
      </@security.authorize>
	  <@security.authorize access="isAuthenticated()">
    	<center><a href='<@spring.url relativeUrl="/monitor/"/>'> <@spring.message code = "error.link"/> </a></center>
      </@security.authorize>
<#include "footer.ftl">