<#include "header.ftl">
	<#if serverList??>
		    <table>
		      	<tr>
		      	<th colspan = '5'><b> <@spring.message code = "monitor.table.title"/> &nbsp <a href = '<@spring.url relativeUrl="/createserver/"/>'><@spring.message code = "monitorservers.table.createlink"/></a></b></th>       
		      	</tr>
		      	<tr>
		      		<th><@spring.message code = "name"/></th>
					<th><@spring.message code = "status"/></th>
					<th><@spring.message code = "lastcheck"/></th>
					<th><@spring.message code = "active"/></th>	
					<th><@spring.message code = "controls"/></th>	
		      	</tr>
		      	<#list serverList as server>
		      	<tr>
						<td>${server.name}</td>
						
						<#if server.state="FAIL">
							<td><center><img src='<@spring.url relativeUrl="/resources/images/gif/fail.gif"/>' alt="fail"></center></td>
						<#elseif server.state="WARN">
							<td><center><img src='<@spring.url relativeUrl="/resources/images/gif/warn.gif"/>' alt="warn"></center></td>
						<#else>
							<td><center><img src='<@spring.url relativeUrl="/resources/images/gif/ok.gif"/>' alt="ok"></center></td>
						</#if>
						<#if server.lastCheckTime??>
						<td>${server.lastCheckTime}</td>
						<#else>
						<td></td>
						</#if>
						<#if server.active=true>
							<td><center> <@spring.message code = "yes"/> </center></td>
						<#else>
							<td><center> <@spring.message code = "no"/> </center></td>
						</#if>
						<td>
							<a href = "<@spring.url relativeUrl="/serverinfo/"/>${server.id}/">
								<@spring.message code = "details"/>
							</a> &nbsp 
							<a href = "<@spring.url relativeUrl="/editserver/"/>${server.id}/">
								<@spring.message code = "edit"/>
							</a> &nbsp 
							<a href = "<@spring.url relativeUrl="/deleteserver/"/>${server.id}/">
								<@spring.message code = "delete"/>
							</a>
						</td>
      			</tr>
      			</#list>
				
        	</table>
    </#if>
<#include "footer.ftl">