<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>웨이즈 | 이벤트</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<style type="text/css">
body {
	margin: 0;
	padding: 0;
	background: #139df2;
}
body, table, td, p, a, li, blockquote {
	-webkit-text-size-adjust: none!important;
	font-family: sans-serif;
	font-style: normal;
	font-weight: 400;
}
	#wrap {
	}
	.header { position: relative; }
	.top_title { width: 100%; }

	.content { padding-top: 0px; }
	.btn_kakao {
		margin: 0 auto;
		width: 200px; 
		padding: 15px 0px;
		background: #fbe300; 
		border-radius: 7px;
		text-align: center;
	}
	.invite_title{
		font-size: 13pt; color: #442121; font-weight: 900;
	}
	.content { padding: 0px; }
	.content p { padding: 30px 30px 0px 30px; font-size: 10pt; color: #485465; line-height: 22pt; letter-spacing: -1px; }
	.content .img { width: 100%; margin: 30px 0px 30px 0px; }
	.content b { color: #121212; font-weight: 800; }
	.kakao_logo { height: 20px; vertical-align: text-bottom; margin-right: 10px; }
	.footer {
		width: 100%;
		background: #fff;
		margin-top: 50px;
		padding: 50px 00px;
		
	}
	.service-btn1 { 
		position: relative; 
		left: 5%;
		width: 90%; 
		padding: 15px 0px;
		margin-bottom: 20px;
		background: #fabf3d; 
		border-radius: 7px;
		font-weight: 600;
		color: #fff;
		text-align: center;
		-webkit-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		-moz-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
	}
	.service-btn2 { 
		position: relative; 
		left: 15%;
		width: 70%; 
		padding: 15px 0px;
		margin-bottom: 100px;
		background: #0065ff; 
		border-radius: 7px;
		font-weight: 600;
		color: #fff;
		text-align: center;
		-webkit-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		-moz-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
	}
	.title-01 { 
		position: relative; 
		left: 50%;
		margin-left: -130px;
		width: 260px; 
		padding: 15px 0px;
		margin-bottom: 20px;
		background: #fff; 
		border-radius: 100px;
		font-weight: 600;
		color: #121212;
		text-align: center;
		-webkit-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		-moz-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
	}
	.count-no { 
		position: relative; 
		left: 50%;
		margin-left: -50%;
		width: 100%; 
		font-weight: 600;
		color: #fff;
		text-align: center;
		font-size: 40px;
	}
	.event-title { 
		position: relative; 
		left: 50%;
		margin-left: -50%;
		width: 100%; 
		font-weight: 600;
		color: #fff;
		text-align: center;
		font-size: 22px;
	}
	.event-title2 { 
		position: relative; 
		left: 50%;
		margin-left: -50%;
		width: 100%; 
		font-weight: 300;
		color: #fff;
		text-align: center;
		font-size: 14px;
	}
	.btn_kakao {
		margin: 0 auto;
		width: 260px; 
		padding: 15px 0px;
		background: #fbe300; 
		border-radius: 7px;
		text-align: center;
	}
	.kakao_logo { height: 20px; vertical-align: text-bottom; margin-right: 10px; }
	.invite_title{
		font-size: 13pt; color: #442121; font-weight: 900;
	}
</style>
</head>
<body>
	<div id="wrap">
		<input type="hidden" id="eventId" value="${eventId }">
		<input type="hidden" id="usrId" value="${info.usrId }">
		<div class="header">
			<img class="top_title" src="${pageContext.request.contextPath}/resources/img/09_free.png">
		</div>
		<div class="content">
			<div class="counter">
				<div class="title-01">
				<span>실시간 쿠폰 발행 현황</span>
				</div>
				<div class="count-no">
				<span><fmt:formatNumber value="${info.eventCnt }" groupingUsed="true"/>개</span>
				</div>
			</div>
			<div style="margin: 60px 0px; height: 2px; background: #fff; width: 70%; margin-left: 15%; left: 50%; opacity: 0.2;"></div>
			<div class="event">
				<img class="img" src="${pageContext.request.contextPath}/resources/img/s_02.png">
				<div style="margin: 10px 0px;"></div>
				<c:if test="${info.eventUser == 0 }"><img class="img" onclick="joinEvent()" src="${pageContext.request.contextPath}/resources/img/s_03.png"></c:if>
				<c:if test="${info.eventUser > 0 }"><img class="img" src="${pageContext.request.contextPath}/resources/img/s_03_d.png"></c:if>
			</div>
			<div style="margin: 60px 0px; height: 2px; background: #fff; width: 70%; margin-left: 15%; left: 50%; opacity: 0.2;"></div>
			<div class="event">
				<div class="event-title">
				<span>이벤트 공유하기</span>
				</div>
				<div style="margin: 10px 0px;"></div>
				<div class="event-title2">
				<span>친구에게 웨이즈 무료환전혜택을 알려주세요!</span>
				</div>
				<div style="margin: 30px 0px;"></div>
				<div class="btn_kakao">
				<a style="text-decoration:none" id="kakao-link-btn" href="javascript:;"><img class="kakao_logo" src="${pageContext.request.contextPath}/resources/img/btn_kakao.png"><span class="invite_title">카톡으로 공유하기</span></a>
				</div>
			</div>
			<div style="margin: 60px 0px; height: 2px; background: #fff; width: 70%; margin-left: 15%; left: 50%; opacity: 0.2;"></div>
			<div style="margin: 50px 0px;"></div>
			<div class="service-btn2" onclick="openUrl()">
				<span>환전 예약하기</span>
			</div>
		</div>
		<div class="footer">
			<div style="padding: 0px 30px 20px 30px; font-size: 12px; font-weight: 800;">유의사항</div>
			<div style="padding: 0px 30px 20px 30px; font-size: 9px; color: #485465;">
			ᆞ본 이벤트의 쿠폰은 신규가입 즉시 자동 발급됩니다.<br/><br/>ᆞ본 이벤트의 쿠폰은 USD 또는 JPY 중 1개의 예약에 한해 사용 가능 합니다.<br/><br/>ᆞ본 이벤트는 1인 1회만 참여 가능합니다.(탈퇴 후 재가입 시 제공되지 않습니다.)<br/><br/>ᆞ부정한 방법을 통해 이벤트에 참여한 경우 쿠폰 발급이 취소될 수 있습니다.<br/><br/>ᆞ본 쿠폰은 발급일로부터 30일 이내 사용 가능하며 기간이 지나면 사용이 불가능합니다.<br/><br/>ᆞ본 이벤트는 사전 공지 없이 당사의 사정에 따라 변경 또는 조기 종료될 수 있습니다.
			</div>
		</div>
	</div>

<script type="text/javascript">
var webUrl = 'https://weys.exchange';

Kakao.init('6a3a939d809ef2af9b36dffab19ba3b7');
Kakao.Link.createDefaultButton({
	container: '#kakao-link-btn',
	objectType : 'feed',
	content : {
		title : '늦캉스 무료환전혜택!\n신규 가입만 해도 USD, JPY 100% 환전 수수료 우대!\n(~9/30)',
		imageUrl : 'https://m.weys.exchange/weys/resources/img/09_free_kakao.png',
		link : {
			mobileWebUrl : webUrl,
			webUrl : webUrl,
			androidExecParams : 'http://bit.ly/2VWNtT8',
			iosExecParams : 'https://apple.co/2YiuxeT'
		}
	},
	buttons : [
		{
			title : '웨이즈 앱 다운받기',
			link : {
				mobileWebUrl : webUrl,
				webUrl : webUrl,
				androidExecParams : 'http://bit.ly/2VWNtT8',
				iosExecParams : 'https://apple.co/2YiuxeT'
			}
		}
	]
});

function joinEvent(){
	
	var usrId = $('#usrId').val();
	if(usrId == 0){
		alert('login');
		return;
	}
	
	var data = {};
	data["eventId"] = $('#eventId').val();
	data["usrId"] = usrId;
	
	$.ajax({
		type:"post",
		url:"${pageContext.request.contextPath}/api/event/coupon",
		data: JSON.stringify(data),
		async : false,
		contentType: "application/json",
		success : function(result){
			var result = result.result;
			if(result == 'success'){
				alert('쿠폰이 발급되었습니다.');
				location.reload(true);
			} else {
				alert('다시 시도해 주세요.');
			}
			
		},
		error : function(result){
			alert('업데이트 실패, 에러');
		}
	});
}

function openUrl(){
	alert('rsv_USD');
}
</script>
</body>
</html>