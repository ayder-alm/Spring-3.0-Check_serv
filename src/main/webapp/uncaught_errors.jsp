<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error page</title>
</head>
<body>
	<%@ page language="java" %>
	<center>
      <h1>
         Error occured in application
      </h1>
	</center>
	<p>
    <b>The status code is:</b> <%= request.getAttribute("javax.servlet.error.status_code") %><br>

    <b>The error message again is:</b> <%= request.getAttribute("javax.servlet.error.message") %><br>
	</p> 
</body>
</html>