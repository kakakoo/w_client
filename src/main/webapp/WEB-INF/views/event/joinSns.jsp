<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>웨이즈 | 인증샷 이벤트</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<style type="text/css">
body {
	margin: 0;
	padding: 0;
	background: #2b2b2b;
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
	.content p { padding: 30px 30px 0px 30px; font-size: 11pt; color: #485465; line-height: 22pt; letter-spacing: -1px; }
	.content img { width: 100%; margin: 0px; }
	.content b { color: #121212; font-weight: 800; }
	.footer {
		width: 100%;
		background: #2b2b2b;
		margin-top: 50px;
		padding: 50px 00px;
		
	}
	.service-btn1 { 
		position: relative; 
		left: 15%;
		width: 70%; 
		padding: 20px 0px;
		background: #ffe442; 
		border-radius: 100px;
		font-weight: 600;
		color: #1e1e1e;
		text-align: center;
		-webkit-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		-moz-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
	}
	.service-btn2 { 
		position: relative; 
		left: 5%;
		width: 90%; 
		padding: 15px 0px;
		margin-bottom: 20px;
		background: #ffe442; 
		border-radius: 7px;
		font-weight: 600;
		color: #1e1e1e;
		text-align: center;
		-webkit-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		-moz-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
	}
		.input_url { 
		position: relative; 
		left: 5%;
		width: 90%; 
		padding: 15px 0px;
		margin-bottom: 20px;
		background: #fff; 
		border-radius: 7px;
		border: 0px;
		font-weight: 100;
		font-size: 11pt;
		color: #1e1e1e;
		text-align: center;
		-webkit-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		-moz-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
	}
	.btn_kakao {
		margin: 0 auto;
		width: 220px; 
		padding: 15px 0px;
		background: #fbe300; 
		border-radius: 7px;
		text-align: center;
		margin-top: 30px; 
	}
	.invite_title{ font-size: 13pt; color: #442121; font-weight: 900; }
	.kakao_logo { height: 20px; width: auto !important; vertical-align: text-bottom; margin: 0px 10px 0px 0px !important;  }
</style>
</head>
<body>
	<div id="wrap">
		<input type="hidden" id="eventId" value="${eventId }">
		<input type="hidden" id="usrId" value="${info.usrId }">
		<div class="header">
			<img class="top_title" src="${pageContext.request.contextPath}/resources/img/review_header.gif">
		</div>
		<div class="content">
			<img src="${pageContext.request.contextPath}/resources/img/review_011.png">
			<div class="service-btn1">
				<a href="#gogo" style="text-decoration:none;"><span class="invite_title">인증샷 URL 등록하기</span></a>
			</div>
			<div class="btn_kakao">
			<a style="text-decoration:none" id="kakao-link-btn" href="javascript:;"><img class="kakao_logo" src="${pageContext.request.contextPath}/resources/img/btn_kakao.png"><span class="invite_title">친구에게 소개하기</span></a>
			</div>
			<img src="${pageContext.request.contextPath}/resources/img/review_02.png">
			<img src="${pageContext.request.contextPath}/resources/img/review_03.png" id="gogo">
			<c:choose>
		       <c:when test="${info.usrId == 0 }">
		           <input type="text" name="url" id="eventUrl" placeholder="로그인 이후 진행해주세요." class="input_url">
		       </c:when>
		       <c:otherwise>
		           <input type="text" name="url" id="eventUrl" placeholder="인증샷을 등록한 게시물 URL을 입력하세요." class="input_url">
		       </c:otherwise>
		   </c:choose>
			<div class="service-btn2" onclick="submit()">
				<span>완료</span>
			</div>
			<img src="${pageContext.request.contextPath}/resources/img/review_04.png">
			<div style="margin: 15px 0px;"></div>
		</div>
		<div class="footer">
			<div style="padding: 0px 30px 20px 30px; font-size: 13pt; font-weight: 500; color: #fff;">유의사항</div>
			<div style="padding: 0px 30px 20px 30px; font-size: 7pt; color: #ababab;">
ᆞ응모 시 등록한 게시물은 공개 상태로 되어있어야 확인 후 쿠폰 지급이 가능합니다.(비공개 계정의 경우 확인이 불가능하여 지급되지 않습니다.)<br><br>ᆞ응모 시 등록한 저작물은 추후 웨이즈 마케팅 용도로 이용될 수 있으며, 해당 이벤트는 중복 참여가 가능합니다.(쿠폰은 1회 제공)<br><br>ᆞ응모자는 자신이 저작권을 보유한 저작물에 한하여 응모가 가능합니다.(타인 저작물을 통한 참여 불가)<br><br>ᆞ참여 횟수가 많을수록 BEST컷 경품 당첨 확율이 높아집니다.<br><br>ᆞ쿠폰은 영업일 기준 3일 이내에 URL 확인 후 발급됩니다.(해당 URL에서 인증샷이 확인되지 않을 경우 쿠폰이 발급되지 않습니다.)<br><br>ᆞ본 이벤트를 통해 발급된 쿠폰은 발급일로부터 180일간 유효합니다.<br><br>ᆞ본 이벤트를 통해 발급된 배송료/위탁수수료 쿠폰은 주소지 배송에는 사용이 불가능합니다.<br><br>ᆞ본 이벤트를 통해 발급된 배송료/위탁수수료 쿠폰은 각각 발급되지만 서로 연동되어 1개라도 사용하면 2개 모두 사용완료 처리됩니다.<br><br>ᆞ기프티콘/현물 경품 당첨자는 2019년 7월 19일(금) 공지사항과 개별연락을 통해 알려드릴 예정입니다.<br><br>ᆞ기프티콘/현물 경품의 경우 이벤트 종료 후 2주 이내에 개별적으로 지급될 예정입니다.<br><br>ᆞ기프티콘/현물 경품의 경우 본인 확인 연락이 닿지 않을 경우 당첨이 취소될 수 있습니다.<br><br>ᆞ5만원 이상의 경품 제세공과금(경품 금액의 22%)은 당첨자가 부담합니다.<br><br>
(제세공과금 처리를 위해 당첨자의 개인정보를 요청할 수 있습니다.)<br><br>ᆞ본 이벤트를 통해 발급된 쿠폰을 사용한 거래의 환불 요청 시 쿠폰은 반환되지 않습니다.<br><br>ᆞ본 이벤트를 통해 발급된 쿠폰은 타인에게 양도할 수 없으며, 본인에 한해서 사용할 수 있습니다.<br><br>ᆞ부정한 방법으로의 발급, 사용이 확인되는 경우 거래가 취소될 수 있습니다.<br><br>ᆞ본 이벤트 또는 경품은 당사의 사정에 따라 사전 예고 없이 변경 또는 조기 종료될 수 있습니다.<br><br>ᆞ본 이벤트에서 표기된 ‘무료 환전’은 환전 수수료 및 외화 배송료, 위탁 수수료 등 부대 비용이 발생하지 않는 것을 뜻합니다.<br><br>ᆞ쿠폰 지급 후 게시물이 삭제되는 경우 지급 된 쿠폰은 사전 고지없이 회수될 수 있습니다.

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
		title : '웨이즈 환전 인증샷 올리면 다음 여행 무료 환전! 만다리나덕 캐리어 당첨 행운까지! (~2019년 8월 31일까지)',
		imageUrl : 'https://dev.weys.exchange/weys/resources/img/review_invite_kakao.png',
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

function submit(){
	
	var usrId = $('#usrId').val();
	if(usrId == 0){
		alert('login');
		return;
	}
	
	var eventUrl = $('#eventUrl').val();
	if(eventUrl == ''){
		alert('인증샷 경로를 입력해주세요.');
		return;
	}

	var data = {};
	data["eventId"] = $('#eventId').val();
	data["usrId"] = usrId;
	data["eventUrl"] = eventUrl;
	
	$.ajax({
		type:"post",
		url:"${pageContext.request.contextPath}/api/event/join",
		data: JSON.stringify(data),
		async : false,
		contentType: "application/json",
		success : function(result){
			var result = result.result;
			if(result == 'success'){
				$('#eventUrl').val('');
				alert('신청이 완료되었습니다.');
			} else {
				alert('다시 시도해 주세요.');
			}
			
		},
		error : function(result){
			alert('업데이트 실패, 에러');
		}
	});
}

</script>
</body>
</html>