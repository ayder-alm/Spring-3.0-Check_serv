<#include "header.ftl">
	<table>
      	<tr>
      		<th colspan = '8'><b><@spring.message code = "serverinfo.table.title"/> "${server.name}"</b></th>       
      	</tr>
      	<tr>
      		<th><@spring.message code = "name"/></th>
      		<th><@spring.message code = "adress"/></th>
      		<th><@spring.message code = "port"/></th>
      		<th><@spring.message code = "url"/></th>
			<th><@spring.message code = "status"/></th>
			<th><@spring.message code = "lastcheck"/></th>
			<th><@spring.message code = "response"/></th>	
			<th><@spring.message code = "active"/></th>	
      	</tr>        		 
      	<tr>
			<td>${server.name}</td>
			<td>${server.adress}</td>
			<td>${server.port}</td>
			<td>"${server.urlPath}"</td>
			<td>${server.state}</td>
			<#if server.lastCheckTime??>
			<td>${server.lastCheckTime}</td>
			<#else>
			<td></td>
			</#if>
			<td><#if server.response??>${server.response}</#if></td>
			<#if server.active=true>
				<td><center><@spring.message code = "yes"/></center></td>
			<#else>
				<td><center><@spring.message code = "no"/></center></td>
			</#if>
		</tr>
    </table>
    <br>
    <center>
	  <form action='<@spring.url relativeUrl="/monitorservers/"/>'>
			<input type="submit" value = "<@spring.message code = "back"/>">
	  </form>
	</center>
<#include "footer.ftl">