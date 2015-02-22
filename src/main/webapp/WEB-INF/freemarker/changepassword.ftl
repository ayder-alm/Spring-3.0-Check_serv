<#include "header.ftl"/>

<script type="text/javascript">
<!--
function checkPass()
{
    var pass1 = document.getElementById('pass1');
    var pass2 = document.getElementById('pass2');
    var message = document.getElementById('confirmMessage');
    var OkColor = "#66cc66";
    var WrongColor = "#ff6666";
    
    if(pass1.value == pass2.value){
        pass2.style.backgroundColor = OkColor;
        message.style.color = OkColor;
        message.innerHTML = "<@spring.message code = "changepass.script.match"/>";
        return true;
    }else{
        pass2.style.backgroundColor = WrongColor;
        message.style.color = WrongColor;
        message.innerHTML = "<@spring.message code = "changepass.script.error"/>";
        return false;
    }
}

function checkLength()
{
	var pass1 = document.getElementById('pass1');
	if(!checkPass()){
		return false;
	}
    if( (pass1.value.length < 6)){
    	var WrongColor = "#ff6666";
    	var message = document.getElementById('confirmMessage');
    	message.style.color = WrongColor;
    	message.innerHTML = "<@spring.message code = "changepass.script.error.short"/>"
    	return false;
    }
    return true;
}      
-->
</script>
     
      <center>
      <table style="width:36%; margin-left:32%; margin-right:32%; border: 3px solid #47f; empty-cells:hide;">
		      	<tr bgcolor="#ccccff" >
		      	  <th><b> <@spring.message code = "changepass.title"/> </b></th>       
		      	</tr>
		      	
		      	<tr bgcolor="white" align="left">
		          <td>
			          <form method="POST" action="<@spring.url relativeUrl="/changepassword/"/>"  onsubmit="return checkLength();" >
				        <table style="width:99%; margin-left:2px; margin-right:2px; border: 0px; cellpadding:2; cellspacing:'0';" bgcolor="white">
				         <tr>
				         	<td>
				         		<label for="pass1"><@spring.message code = "login.password.new"/></label>
				         		<input type = "hidden" name = "id" value = "1">
				         	</td>
				         	<td>
				         		<input type="password" id="pass1" name="newpassword" maxlength="16" size="16" onkeyup="checkPass(); return false;">
				         	</td>
				         </tr>
				         
				         <tr>
				         	<td>
				         		<label for="pass2"><@spring.message code = "login.password.confirm"/></label>
				         	</td>
				         	<td>
				         		<input type="password" maxlength="16" size="16" id="pass2" onkeyup="checkPass(); return false;">
				         	</td>
				         </tr>
						 
						 <tr>
						 	<td></td>
						 	<td align="center">
						 		<input value="<@spring.message code = "submit"/>" type="submit">
						 	</td>
						 </tr>
						 
						 <tr>
						 	<td colspan="2"> <@spring.message code = "changepass.info"/> </td>
						 </tr>
					    </table>				 
        		 	  </form>
        		 </td>
      			</tr>
      			<tr>
      				<td>
      					<center><span id="confirmMessage" class="confirmMessage"></span></center>
      				</td>
      			</tr>
       </table>
       </center>
      <br>
    <#if error??>
		<center class="error"><@spring.message code = "error.password"/></center>			
	</#if>
	<center>
		<form action='<@spring.url relativeUrl="/monitor/"/>'>
				<input type="submit" value = "<@spring.message code = "cancel"/>">
	    </form>
    </center>
<#include "footer.ftl">