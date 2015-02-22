<#include "header.ftl">
	<#if userList??>
		    <table>
		      	<tr>
		      	<th colspan = '4'><b> <@spring.message code = "monitorusers.table.title"/> &nbsp 
		      	<a href = '<@spring.url relativeUrl="/createuser/"/>'>
		      		<@spring.message code = "monitorusers.table.createlink"/>
		      	</a></b></th>       
		      	</tr>
		      	<tr>
		      		<th><@spring.message code = "name"/></th>
					<th><@spring.message code = "lastlogin"/></th>
					<th><@spring.message code = "active"/></th>	
					<th><@spring.message code = "controls"/></th>
		      	</tr>
		      	<#list userList as user>
		      	<tr>
						<td>${user.name}</td>
						<#if user.lastLoginTime??>
						<td>${user.lastLoginTime}</td>
						<#else>
						<td></td>
						</#if>
						<#if user.active=true>
							<td><center> <@spring.message code = "yes"/> </center></td>
						<#else>
							<td><center> <@spring.message code = "no"/> </center></td>
						</#if>
						
						<td>
							<center>
							<#if editor == "admin">
								<a href = "<@spring.url relativeUrl="/edituser/"/>${user.id}/">
									<@spring.message code = "edit"/>
								</a>  
							<#else>
								<#if user.admin == false>
									<a href = "<@spring.url relativeUrl="/edituser/"/>${user.id}/">
									<@spring.message code = "edit"/>
								</a>
								</#if>
							</#if>
							&nbsp&nbsp
							<#if user.admin != true>
								<a href = "<@spring.url relativeUrl="/deleteuser/"/>${user.id}/">
									<@spring.message code = "delete"/>
								</a>
							</#if>
							</center>
						</td>
      			</tr>
      			</#list>
				
        	</table>
    </#if>
<#include "footer.ftl">