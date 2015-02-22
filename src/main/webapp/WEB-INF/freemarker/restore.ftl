<#import "spring.ftl" as spring />

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
      <center>
      <table cellpadding="2" cellspacing="0" border="0">
      	<tr>
      	<td bgcolor="#aaaaff" >
		    <table cellpadding="0" cellspacing="2" border="0" width="100%" >
		      	
		      	<tr bgcolor="#bbbbff" >
		      	  <th><b><@spring.message code = "restore.title"/></b></th>       
		      	</tr>
		      	
		      	<tr bgcolor="white">
		          <td>
			          <form method="post" name="restoreForm" action="<@spring.url relativeUrl="/restore/"/>">
				        <table cellpadding='5' cellspacing='0' border='0' bgcolor="white">
				         
				         <tr>
				         	<td><@spring.message code = "login.login"/></td>
				         	<td><input type="text" name="login" maxlength="16" size="16" value=""></td>
				         </tr>
				         
				         <tr>
				         	<td><@spring.message code = "email"/></td>
				         	<td><input type="text" id="mail" name="email" maxlength="64" size="16" value=""><div id="mailcheck"></div></td>
				         </tr>
						 
						 <tr>
						 	<td>
							</td>
						 	<td><input value="<@spring.message code = "submit"/>" type="submit"></td>
						 </tr>
						 
						 <tr>
						 	<td colspan="2"></td>
						 </tr>
						 
						 <tr>
						 	<td colspan="2"><@spring.message code = "restore.info"/></td>
						 </tr>
			         	 <#if error??>
			         	 <tr>
						 	<td colspan="2" style="color:red; font-weight:bold; font-size:larger;">
						 		<@spring.message code = "restore.error"/>
						 	</td>
						 </tr>
			         	 </#if>
			         	</table>
		        	</form>
        		 </td>
      			</tr>
        	</table>
        </td>
      	</tr>
      	
      </table>
      <br>
      <form action='<@spring.url relativeUrl="/login/"/>'>
			<input type="submit" value = "<@spring.message code = "back"/>">
	  </form>
      </center>
    
<#include "footer.ftl">