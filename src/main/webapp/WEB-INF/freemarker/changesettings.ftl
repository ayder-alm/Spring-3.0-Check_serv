<#include "header.ftl">
	<hr>
	<form action='<@spring.url relativeUrl="/changesettings/"/>' method="POST" onsubmit="return CheckSettingsSend();">
	<table>
		<tr>
      		<th colspan = '2'> <b> <@spring.message code = "changesettings.title"/> </b></th>       
    	</tr>
    	<tr>
      		<td><@spring.message code = "changesettings.checkinterval"/></td>
      		<td> <input type="text" name="checkInterval" value="${settings.checkInterval!""}" id = "checkInterval" onkeyup = "StatusUpdate('checkInterval')"><div id="checkIntervalcheck" style="float:right;"></div>   </td>
      	</tr>
    	<tr>
      		<td><@spring.message code = "changesettings.refreshinterval"/></td>
      		<td> <input type="text" name="refreshInterval" value="${settings.refreshInterval!""}" id = "refreshInterval" onkeyup = "StatusUpdate('refreshInterval')"><div id="refreshIntervalcheck" style="float:right;"></div>   </td>
      	</tr>
    	<tr>
      		<td><@spring.message code = "changesettings.timeout"/></td>  <td>
      		<input type="text" name="serverTimeout" value="${settings.serverTimeout!""}" id = "serverTimeout" onkeyup = "StatusUpdate('serverTimeout')"><div id="serverTimeoutcheck" style="float:right;"></div>   </td>
      	</tr>
    	<tr>
      		<td><@spring.message code = "changesettings.smtp"/></td>
      		<td> <input type="text" name="smtpAdress" value="${settings.smtpAdress!""}" id = "smtpAdress" onkeyup = "StatusUpdate('smtpAdress')"><div id="smtpAdresscheck" style="float:right;"></div>   </td>
      	</tr>
    	<tr>
      		<td><@spring.message code = "changesettings.smtpport"/></td>
      		<td> <input type="text" name="smtpPort" value="${settings.smtpPort!""}" id = "smtpPort" onkeyup = "StatusUpdate('smtpPort')"><div id="smtpPortcheck" style="float:right;"></div>   </td>
    	</tr>
    </table>
    <hr>
    <center>
	<input type="submit" value="<@spring.message code = "submit"/>">
	</center>
	<hr>
  </form>
		<@spring.bind "settings.refreshInterval"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "settings.checkInterval"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "settings.serverTimeout"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "settings.smtpPort"/>
		<center><@spring.showErrors "<br>" "error"/></center>
		<@spring.bind "settings.smtpAdress"/>
		<center><@spring.showErrors "<br>" "error"/></center>
	<center>	
		<form action='<@spring.url relativeUrl="/monitor/"/>'>
			<input type="submit" value = "<@spring.message code = "cancel"/>">
		</form>
    </center>	
<#include "footer.ftl">