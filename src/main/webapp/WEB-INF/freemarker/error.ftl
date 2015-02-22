<#include "header.ftl">
<center>
	<#if errorName??>
      <h1><@spring.message code = "error.name"/> "<@spring.message code = "${errorName}"/>"</h1>
      <p>
      	<@spring.message code = "error.body"/> <@spring.message code = "${errorBody}"/>
      </p>
      <p>
      	<@spring.message code = "error.cause"/> <@spring.message code = "${errorCause}"/>
      </p>
    <#else>
	    <#if errorClass??>
	      <h1><@spring.message code = "error.name"/> ${errorClass}</h1>
	      <p>
	      	<@spring.message code = "error.body"/> ${errorMessage}/>
	      </p>
	      <p>
	      	<@spring.message code = "error.cause"/> ${errorMessageCause}/>
	      </p>
	     <#else>
    		<h1><@spring.message code = "error.general"/></h1>
    	 </#if>
    </#if>    
    <a href="javascript:history.back()"><@spring.message code = "back"/></a>
    <br>
    <a href='<@spring.url relativeUrl="/monitor/"/>'> <@spring.message code = "error.link"/> </a> 
</center>    
<#include "footer.ftl">