<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>统一门户管理系统</title>
	
</head>
<body>
    <H1>Index</H1>
    <h2><a href="javascript:formSubmit()">退出</a></h2>
    <!-- csrf for logout-->
    <form action="/logout" method="post" id="logoutForm">
    	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
    </form>
    <script>
        function formSubmit() {
            document.getElementById("logoutForm").submit();
        }
    </script>
</body>	
</html>