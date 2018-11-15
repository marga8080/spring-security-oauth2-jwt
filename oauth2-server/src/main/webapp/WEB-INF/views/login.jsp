<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>统一门户管理系统</title>
<link rel="stylesheet" href="./static/bootstrap-3.3.7/css/bootstrap.min.css">
<script src="./static/jquery/jquery-1.11.3.min.js"></script>
<script src="./static/bootstrap-3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container-fluid" style="padding-top: 20px">
	    
		<c:if test="${not empty error }">
			<div class="alert alert-danger" role="alert">${error }</div>
		</c:if>
		<div id="codemsg" style="display: none;" class="alert alert-danger"></div>
		
		<div class="row">
			<div class="col-md-4">
			    <div class="panel panel-primary">
				    <div class="panel-heading">
				    	用户名密码登录
				    </div>
				    <div class="panel-body">
						<form action="<c:url value='/login'/>" method="POST" class="form-horizontal">
							<!-- 开启csrf后必须包含_csrf.token,否则报错： 403 Could not verify the provided CSRF token because your session was not found -->
							<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
							<div class="form-group">
							    <label for="username" class="col-sm-3 control-label">用户名:</label>
							    <div class="col-sm-9">
							      <input type="text" name="username" id="username" autocomplete="off" />
							    </div>
							</div>
							<div class="form-group">
							    <label for="password" class="col-sm-3 control-label">密码:</label>
							    <div class="col-sm-9">
							      <input type="password" name="password" id="password" autocomplete="new-password" />
							    </div>
							</div>
							<div class="form-group">
							    <div class="col-sm-offset-3 col-sm-9">
							      <button type="submit" class="btn btn-default" style="width: 100px">登 录</button>
							    </div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-md-4">
			<div class="panel panel-primary">
				    <div class="panel-heading">
				    	短信验证码登录
				    </div>
				    <div class="panel-body">
						<form action="<c:url value='/smsCodeLogin'/>" method="post" class="form-horizontal">
							<!-- 开启csrf后必须包含_csrf.token,否则报错： 403 Could not verify the provided CSRF token because your session was not found -->
							<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
							<div class="form-group">
							    <label for="phone" class="col-sm-3 control-label">手机号:</label>
							    <div class="col-sm-9">
							      <input type="text" name="phone" id="phone" />
							    </div>
							</div>
							<div class="form-group">
							    <label for="code" class="col-sm-3 control-label">密码:</label>
							    <div class="col-sm-9">
							      <input type="text" name="code" id="code" /> 
								  <input type="button" id="sendCodeBtn" value="发送验证码" />
							    </div>
							</div>
							<div class="form-group">
							    <div class="col-sm-offset-3 col-sm-9">
							      <button type="submit" class="btn btn-default" style="width: 100px">登 录</button>
							    </div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>

</body>
<script type="text/javascript">
	//验证码倒计时初始
	var COUNT_NUMBER = 60;
	var countdown = COUNT_NUMBER;

	function sendVerifyCode(obj, phone) {
		$.ajax({
			url : "./sms/loginPhoneCode?phone=" + phone,
			type : 'get',
			dataType : "json",
			success : function(data) {
				if (data.errcode == "0") {
					countdownTimer(obj);
				} else {
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
			verifyCode = null;
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

	$(function() {
		//发送短信验证码
		$("#sendCodeBtn").click(function() {
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