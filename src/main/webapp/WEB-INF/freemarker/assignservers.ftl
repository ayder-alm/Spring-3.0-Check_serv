<#include "header.ftl">
	<hr>
	<form action="<@spring.url relativeUrl="/assignservers/" />" method="POST">
	<input type = "hidden" name="userId" value="${user.id}">
	<input type="hidden" name="belongId"  value="">
	
	<table>
		<tr> 
      		<th colspan = '2'> <b> <@spring.message code = "assignservers.table.title"/> '${user.name}' </b></th>       
    	</tr>
    	<tr>
      		<th><@spring.message code = "assignservers.dont.belong"/></th>
			<th><@spring.message code = "assignservers.belong"/></th>      
      	</tr>
    	<tr>
    		<td>
    		<table>
    		
    		<#list others as server>
    			<tr>
    				<td> ${server.name} </td> <td> <center> <input type="checkbox" name="belongId" value="${server.id}" > </center> </td>
    			</tr>
    		</#list>
    		</table>
    		</td>
    		
    		<td>
    		<table>
    	
    		<#list belong as server>
    			<tr>
    				<td> ${server.name} </td> <td><center> <input type="checkbox"  name="belongId" value="${server.id}" checked> </center></td>
    			</tr>
    		</#list>
    		</table>
    		</td>
    	</tr>
    </table>
    
    <hr>
    
    <center>
    <a href='<@spring.url relativeUrl="/monitorusers/"/>' ><input type="button" value="<@spring.message code = "cancel"/>"></a>
	<input type="submit" value="<@spring.message code = "submit"/>">
	</center>
	
	</form>
	<hr>
	
<#include "footer.ftl">