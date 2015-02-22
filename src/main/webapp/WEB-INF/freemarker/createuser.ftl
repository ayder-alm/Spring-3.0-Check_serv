<#include "header.ftl">
	<hr>
	<form action="<@spring.url relativeUrl="/createuser/"/>" method="POST" onsubmit="return CheckNewUserSend();">
	<input type = "hidden" name="_active">
	<table>
		<tr>
      		<th colspan = '5'> <b> <@spring.message code = "createuser.table.title"/> <#if user??> ${user.name} </#if> </b></th>       
    	</tr>
    	<tr>
      		<th><@spring.message code = "name"/></th>
      		<th><@spring.message code = "login"/></th>
      		<th><@spring.message code = "password"/></th>
      		<th><@spring.message code = "email"/></th>
      		<th><@spring.message code = "active"/></th>
    	</tr>
    	<tr>
	    	  	
	    	<#if user??>
				<td> <input type="text" name="name" value="${user.name}" id = "name" onkeyup = "StatusUpdate('name')"><div id="namecheck" style="float:right;"></div>  </td>
				<td> <input type="text" name="login" value="${user.login}" id = "login" onkeyup = "StatusUpdate('login')"><div id="logincheck" style="float:right;"></div> </td>
				<td> <input type="password" name="password" value="" id = "pass" onkeyup = "StatusUpdate('pass')"><div id="passcheck" style="float:right;"></div> </td>
				<td> <input type="text" name="email" value="${user.email}" id = "mail" onkeyup = "StatusUpdate('mail')"><div id="mailcheck" style="float:right;"></div> </td>				
				<#if user.active==true>
					<td><center> <input type="checkbox" name="active" checked="checked"></center> </td>
				<#else>
					<td><center> <input type="checkbox" name="active"> </center></td>
				</#if>
		 	<#else>		
				<td> <input type="text" name="name" value="" id = "name" onkeyup = "StatusUpdate('name')"><div id="namecheck" style="float:right;"></div> </td>
				<td> <input type="text" name="login" value="" id = "login" onkeyup = "StatusUpdate('login')"><div id="logincheck" style="float:right;"></div> </td>
				<td> <input type="password" name="password" value="" id = "pass" onkeyup = "StatusUpdate('pass')"><div id="passcheck" style="float:right;"></div> </td>
				<td> <input type="text" name="email" value="" id = "mail" onkeyup = "StatusUpdate('mail')"><div id="mailcheck" style="float:right;"></div>  </td>
				<td> <center><input type="checkbox" name="active" checked="checked" > </center></td>
		    </#if>
	    
		 </tr>
    </table>
    <hr>
    <center>
	<input type="submit" value="<@spring.message code = "submit"/>">
	</center>
	
	<br>
	<#if user??>
		<@spring.bind "user.name"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "user.login"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "user.password"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "user.email"/>
		<center><@spring.showErrors "<br>" "error"/></center>
    </#if>			
	<#if exists??>
	<table>
		<tr>
      		<th class="error"> <b> <@spring.message code = "error.user.exists"/> </b> </th>       
    	</tr>
	</table>
	</#if>
	
	</form>
	<center>
	  <form action='<@spring.url relativeUrl="/monitorusers/"/>'>
			<input type="submit" value = "<@spring.message code = "cancel"/>">
	  </form>
	</center>
<#include "footer.ftl">