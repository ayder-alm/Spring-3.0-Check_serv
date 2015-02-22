<#include "header.ftl">
	<hr>
	<form action="<@spring.url relativeUrl="/createserver/"/>" method="POST" name="sendForm" onsubmit="return CheckNewServerSend();">
	<input type = "hidden" name="_active">
	<input type = "hidden" name = "id" value = "0">
	<table>
	    <tr>
      		<th colspan = '5'> <b> <@spring.message code = "createserver.table.title"/> </b></th>       
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
			<td> <input type="text" name="name" value="${server.name}" id = "servername" onkeyup = "StatusUpdate('servername')"><div id="servernamecheck" style="float:right;"></div>  </td>
			<td> <input type="text" name="adress" value="${server.adress}" id = "fqdn" onkeyup = "StatusUpdate('fqdn')"><div id="fqdncheck" style="float:right;"></div></td>
			<td> <input type="text" name="port" value="<#if server.port??>${server.port}</#if>" id = "port" onkeyup = "StatusUpdate('port')"><div id="portcheck" style="float:right;"></div></td>
			<td> <input type="text" name="urlPath" value="${server.urlPath}" id = "url" onkeyup = "StatusUpdate('url')"><div id="urlcheck" style="float:right;"></div></td>
			<#if server.active=true>
				<td><center> <input type="checkbox" name="active" checked="checked"> </center></td>
			<#else>
				<td><center> <input type="checkbox" name="active"> </center></td> 
			</#if>
		<#else>
			<td> <input type="text" name="name" value="" id = "servername" onkeyup = "StatusUpdate('servername')"><div id="servernamecheck" style="float:right;"></div>  </td>
			<td> <input type="text" name="adress" value="" id = "fqdn" onkeyup = "StatusUpdate('fqdn')"><div id="fqdncheck" style="float:right;"> </div></td>
			<td> <input type="text" name="port" value="" id = "port" onkeyup = "StatusUpdate('port')"><div id="portcheck" style="float:right;"> </div></td>
			<td> <input type="text" name="urlPath" value="" id = "url" onkeyup = "StatusUpdate('url')"><div id="urlcheck" style="float:right;"> </div></td>
			<td><center> <input type="checkbox" name="Active" checked="checked"> </center></td>
		</#if>
	    </tr>
    </table>
    <hr>
    <center>
		<input type="submit" value="<@spring.message code = "submit"/>">
	</center>
	</form>
	<br>
				
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
	
	<#if exists??>
	<br>
	<table>
		<tr>
      		<th class="error"> <b> <@spring.message code = "error.server.exists"/> </b> </th>       
    	</tr>
	</table>
	</#if>
	<center>
	  <form action='<@spring.url relativeUrl="/monitorservers/"/>'>
			<input type="submit" value = "<@spring.message code = "cancel"/>">
	  </form>
	</center>
<#include "footer.ftl">