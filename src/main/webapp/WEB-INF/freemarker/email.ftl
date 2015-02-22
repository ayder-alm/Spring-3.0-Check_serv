<#import "spring.ftl" as spring />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
 "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>

	<#if email??>
      <center>
      	<@spring.message code = "email.message"/> ${email}
      </center>
    </#if>    
    <br>
    <center><a href='<@spring.url relativeUrl="/login/"/>'> <@spring.message code = "error.link"/> </a></center>
  </body>
</html>