<#import "spring.ftl" as spring />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

	<title><@spring.message code = "title"/></title>

</head>
<body style="font-family:Arial,Helvetica,sans-serif; background-color:rgb(245,250,255); font-size:small;">
<br><br><br>
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
        message.innerHTML = "<@spring.message code = "changepass.script.match"/>"
    }else{
        pass2.style.backgroundColor = WrongColor;
        message.style.color = WrongColor;
        message.innerHTML = "<@spring.message code = "changepass.script.error"/>"
    }
}

function checkLength()
{
	var pass1 = document.getElementById('pass1');
    if( (pass1.value.length < 6) ){
    	alert("Password length incorrect!");
    	return false;
    }
    document.passForm.submit();
}      
-->
</script>
      <center>
      <table cellpadding="2" cellspacing="0" border="0">
      	<tr>
      	<td bgcolor="#6699f0" >
      		
		    <table cellpadding="0" cellspacing="2" border="0" width="100%" >
		      		      	
		      	<tr bgcolor="#ccccff" >
		      	  <th style="padding:2;padding-bottom:4"><b> <@spring.message code = "changepass.title"/> </b></th>       
		      	</tr>
		      	
		      	<tr bgcolor="white" padding="10"  align="left">
		          <td>
			          <form name = "passForm" method="POST" action="<@spring.url relativeUrl="/login/firstrun" />">
			          <br>
				        <table cellpadding='5' cellspacing='0' border='0' bgcolor="white">
				         
				         <tr>
				         	<td>
				         		<label for="pass1"><@spring.message code = "login.password.new"/></label>
				         	</td>
				         	<td><input type="password" id="pass1" name="password" maxlength="16" size="16" onkeyup="checkPass(); return false;"></td>
				         </tr>
				         
				         <tr>
				         	<td><label for="pass2"><@spring.message code = "login.password.confirm"/></label></td>
				         	<td>
				         		<input type="password" maxlength="16" size="16" id="pass2" onkeyup="checkPass(); return false;">
				         		<span id="confirmMessage" class="confirmMessage"></span>
				         	</td>
				         </tr>
						 
						 <tr>
						 	<td></td>
						 	<td> <input value="<@spring.message code = "submit"/>" type="button" onclick="checkLength();"> </td>
						 </tr>
						 
						 <tr>
						 	<td colspan="2"><@spring.message code = "changepass.info"/></td>
						 </tr>
			         	
			         	</table>
			         	
		        	</form>
        	
        		 </td>
      			</tr>
        	</table>
        </td>
      	</tr>
      	
      </table>
      </center>
    <#if msg??>
		<center class="error">msg</center>			
	</#if>
<#include "footer.ftl">