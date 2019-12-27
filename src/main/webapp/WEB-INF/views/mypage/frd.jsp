<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>친구 추천</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<style type="text/css">
body {
	margin: 0;
	padding: 0;
	background: #f2f2f2;
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
	.top_mycode { 
		position: absolute; 
		left: 5%; 
		bottom: -30px; 
		width: 90%; 
		padding: 24px 0px;
		background: #fff; 
		border-radius: 12px;
		text-align: center;
		-webkit-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		-moz-box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
		box-shadow: 0px 0px 61px -6px rgba(0,0,0,0.2);
	}
	.top_mycode {  }
	.mycode_title { font-size: 15px; color: #485465; font-weight: 500; }
	.mycode_code { font-size: 18px; color: #0065ff; font-weight: 900; margin-left: 10px; }
	
	.content { padding-top: 80px; padding-bottom: 40px; background: #fff; font-size: 13px; }
	.btn_kakao {
		margin: 0 auto;
		width: 200px; 
		padding: 15px 0px;
		background: #fbe300; 
		border-radius: 7px;
		text-align: center;
		border: 1px solid rgba(0,0,0,0.1);
	}
	.invite_title{
		font-size: 15px; color: #442121; font-weight: 900;
	}
	.kakao_logo { height: 20px; vertical-align: text-bottom; margin-right: 10px; }
	.stamp { padding: 50px 30px 10px 30px; text-align: center; }
	.invite_history { width: 140px; }
	.stamp_object { text-align: center; margin: 2%; width: 26%; float: left;}
	.stamp_object :first-child { padding-left: 17%; }
	.stamp_object img {width: 100%;}
	.invite_desc {
		clear: left;
		margin: 0 auto;
		padding-top: 10px;
		text-align: center;
		font-size: 11pt; color: #485465;
	}
	.footer {
		border-top: 1px solid #dce3ed;
		width: 100%;
				margin-bottom:30px;
	}
	/* Style the element that is used to open and close the accordion class */
p.accordion {
    color: #444;
    cursor: pointer;
    padding: 20px 0px;
    text-align: left;
    outline: none;
    margin: 0px;
	font-size: 13px;
	font-weight: 800;
	margin: 0px 30px;
	-webkit-tap-highlight-color: rgba(0,0,0,0);
}

/* Add a background color to the accordion if it is clicked on (add the .active class with JS), and when you move the mouse over it (hover) */

/* Unicode character for "plus" sign (+) */
p.accordion:after {
    float: right;
	background-image: url("${pageContext.request.contextPath}/resources/img/btn_arrow.png");
    background-size: 14px 9px;
    display: inline-block;
    width: 14px; 
    height: 9px;
	margin-top: 4px;
    content:"";
}
	p.accordion.active { color: #0065ff; border-bottom: 0px solid #dce3ed;}
	
/* Unicode character for "minus" sign (-) */
p.accordion.active:after {
    float: right;
	background-image: url("${pageContext.request.contextPath}/resources/img/btn_arrow_up.png");
    background-size: 14px 9px;
    display: inline-block;
    width: 14px; 
    height: 9px;
    content:"";
}
	
	div.panel {
    padding: 0 30px;
    max-height: 0;
    overflow: hidden;
    opacity: 0;
	border-top: 1px solid #dce3ed;
}

div.panel.show {
    opacity: 1;
	padding: 20px 30px;
    max-height: 1000px; /* Whatever you like, as long as its more than the height of the content (on all screen sizes) */
}
</style>
<script>
document.addEventListener("DOMContentLoaded", function(event) { 


var acc = document.getElementsByClassName("accordion");
var panel = document.getElementsByClassName('panel');

for (var i = 0; i < acc.length; i++) {
    acc[i].onclick = function() {
        var setClasses = !this.classList.contains('active');
        setClass(acc, 'active', 'remove');
        setClass(panel, 'show', 'remove');

        if (setClasses) {
            this.classList.toggle("active");
            this.nextElementSibling.classList.toggle("show");
        }
    }
}

function setClass(els, className, fnName) {
    for (var i = 0; i < els.length; i++) {
        els[i].classList[fnName](className);
    }
}

});
</script>
</head>
<body>
	<div id="wrap">
		<div class="header">
			<img class="top_title" src="${pageContext.request.contextPath}/resources/img/top_title.png">
			<div id="btn_copy" class="top_mycode">
				<span class="mycode_title">나의 추천인 코드</span><span id="frdCd" class="mycode_code">${info.FRD_CD}</span>
				<script>
				$('#btn_copy').click(function() {
					var frdCd = $('#frdCd').html();
				  alert('copy_' + frdCd);
				});
				</script>
			</div>
		</div>
		<div class="content">
			<div class="btn_kakao">
			<a style="text-decoration:none" id="kakao-link-btn" href="javascript:;"><img class="kakao_logo" src="${pageContext.request.contextPath}/resources/img/btn_kakao.png"><span class="invite_title">친구 초대하기</span></a>
			</div>
			<hr style="width: 85%; border: 0; height: 1px; background: #dce3ed; margin: 40px auto;">
			<div style="width: 85%; margin: 0 auto;">
				<div style="color: #2a333c;"><span style="font-weight: 700; color: #0065ff;">1. </span>친구에게 링크를 공유합니다.(추천인 코드와 함께)</div>
				<div style="color: #2a333c; margin-top: 14px;"><span style="font-weight: 700; color: #0065ff;">2. </span>친구가 추천인 코드를 입력하고 회원가입을 완료합니다.</div>
				<div style="color: #2a333c; margin-top: 14px;"><span style="font-weight: 700; color: #0065ff;">3. </span>친구와 나에게 USD, JPY 100% 우대 쿠폰이 지급됩니다.</div>
				
			</div>
		</div>
		<div class="footer">
			<p class="accordion">친구초대 이벤트 유의사항</p>
			<div class="panel" style="font-size: 11px;">
				<ul style="margin: 0px 14px; padding: 0px; line-height: 18px; color: dimgray;">
				<li>쿠폰은 친구가 추천인 코드를 입력하고 신규 가입을 완료하는 즉시 지급됩니다.(유효기간: 발급일로부터 3개월)</li>
				<li>초대 받은 친구가 가입하지 않거나 탈퇴하는 경우 쿠폰은 지급되지 않습니다.</li>
				<li>친구 초대는 무제한 가능하며, 초대를 받고 가입한 신규 가입자 모두에게 쿠폰을 지급합니다.</li>
				<li>단, 나에게는 1회에 한해 쿠폰이 제공됩니다.</li>
				<li>부정한 방법을 통해 이벤트에 참여한 경우 쿠폰 지급 취소 및 회원 자격이 상실될 수 있습니다.</li>
				<li>본 이벤트를 통해 지급된 쿠폰은 타인에게 양도할 수 없으며, 본인에 한해서 사용 가능합니다.</li>
				<li>본 이벤트는 사전 공지 없이 당사의 사정에 따라 변경 또는 조기 종료될 수 있습니다.</li>
				</ul>
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
		title : '신규 가입시 추천받은 친구와 나에게 100% 환율 우대 쿠폰 지급! \n추천인 코드: ' + $('#frdCd').html(),
		imageUrl : 'https://dev.weys.exchange/weys/resources/img/invitation_event.png',
		link : {
			mobileWebUrl : webUrl,
			webUrl : webUrl,
			androidExecParams : 'https://play.google.com/store/apps/details?id=i4unetworks.weys',
			iosExecParams : 'https://itunes.apple.com/app/id983091615'
		}
	},
	buttons : [
		{
			title : '웨이즈 앱 다운받기',
			link : {
				mobileWebUrl : webUrl,
				webUrl : webUrl,
				androidExecParams : 'https://play.google.com/store/apps/details?id=i4unetworks.weys',
				iosExecParams : 'https://itunes.apple.com/app/id983091615'
			}
		}
	]
});

function resultLink(res){
	
}

</script>
</body>
</html>