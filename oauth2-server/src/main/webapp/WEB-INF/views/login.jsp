<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="ie=edge">
	<title>统一门户管理系统</title>
	<link rel="stylesheet" href="./static/bootstrap-3.3.7/css/bootstrap.min.css" >
	<link rel="stylesheet" href="./static/bootstrap-3.3.7/css/bootstrap-theme.min.css" >
	<script src="./static/jquery/jquery-1.11.3.min.js" ></script>
	<script src="./static/bootstrap-3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

	<div class="loginInfo" id="loginError">
			<c:if test="${not empty error }">
				<div class="error">
					<font color="red">${error }</font><br />
					<br />
				</div>
			</c:if>
			<c:if test="${not empty msg }">
				<div class="msg">
					<font color="red">${msg }</font><br />
					<br />
				</div>
			</c:if>
			<div id="codemsg" style=" height:22px; display:none;" class="clearfix"></div>
		</div>

    <!-- SpringSecurity3.x默认的登录拦截URL是/j_spring_security_check； 4.x默认的登录拦截URL是/login -->
	<form action="<c:url value='/login'/>" method="POST">
	    <!-- 开启csrf后必须包含_csrf.token,否则报错： 403 Could not verify the provided CSRF token because your session was not found -->
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
        <div>
            <label> 用户名: <input type="text" name="username" />
            </label>
        </div>
        <div>
            <label> 密码: <input type="password" name="password" />
            </label>
        </div>
        <div>
            <input type="submit" value="登录" />
        </div>
    </form>
    <span>-------------用手机号验证码登录-------------</span>
    <form action="<c:url value='/phoneCodeLogin'/>" method="post">
        <!-- 开启csrf后必须包含_csrf.token,否则报错： 403 Could not verify the provided CSRF token because your session was not found -->
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
        <div>
            <label> 手机号: <input type="text" name="phone" id="phone"/>
            </label>
        </div>
        <div>
            <label> 验证码: <input type="text" name="code" id="code"/> <input type="button" id="sendCodeBtn" value="发送短信验证码"/>
            </label>
        </div>
        <div>
            <input type="submit" value="登录" />
        </div>
    </form>
</body>
<script type="text/javascript">
//验证码倒计时初始
var COUNT_NUMBER = 60;
var countdown = COUNT_NUMBER;

function sendVerifyCode(obj, phone){
	$.ajax({	                            
        url: "./sms/loginPhoneCode?phone=" + phone,
        type: 'get',
        dataType: "json",
        success: function(data) {
        	if(data.code == "0"){
        		countdownTimer(obj);
        	}else{
        		$("#codemsg").show();
                $("#codemsg").text(data.message);
        	}
        }
    });
};

function countdownTimer(obj) { 
	if (countdown == 0) { 
		obj.removeAttr("disabled"); 
		obj.val("发送验证码"); 
		countdown = COUNT_NUMBER; 
		verifyCode=null;
		return;
	} else { 
		obj.attr("disabled", true); 
		obj.val("重新发送(" + countdown + ")"); 
		countdown--;
	}
	setTimeout(function() { 
		countdownTimer(obj) 
	}, 1000);
};

function checkPhone(phone) { 
    return (/^1[34578]\d{9}$/.test(phone));
};


$(function(){
	//发送短信验证码
	$("#sendCodeBtn").click(function(){
		var phone = $("#phone").val();
		$("#codemsg").hide();
		if (phone == null || phone == '') {
			$("#codemsg").show();
	        $("#codemsg").text("请输入手机号码！");
			return;
		}
		if (!checkPhone(phone)) {
			$("#codemsg").show();
	        $("#codemsg").text("请输入正确的手机号码！");
			return;
		}
		sendVerifyCode($(this), phone);
	});
})


</script>
</html>