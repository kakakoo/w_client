<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>[WEYS] 현금영수증 신청</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<link href="${pageContext.request.contextPath}/resources/css/mine.css" rel="stylesheet">
</head>
<body style="width:100%;margin: 0 auto;padding: 0;">
	<div style="width:450px;margin:auto;">
	
		<div class="title">
			<div class="title_text">현금영수증 발급신청</div>
			<div class="title_img"><img src="${pageContext.request.contextPath}/resources/img/tax_logo.png"></div>
		</div>
		
		<div class="contents">
			<div class="content">
				<div class="order">발급용도</div>
				<div class="answer">
					<input type="radio" name="cashTp" id="cashTp1" value="U" checked><label for="cashTp1">소득공제용</label>
					<input type="radio" style="margin-left:10px;" name="cashTp" id="cashTp2" value="C"><label for="cashTp2">지출증빙용</label>
				</div>
			</div>
			
			<div class="content">
				<div class="order">결제내용</div>
				<div class="answer">
					<input type="radio" name="itemNm" id="itemNm1" value="M" checked><label for="itemNm1">멤버십가입</label>
					<input type="radio" style="margin-left:10px;" name="itemNm" id="itemNm2" value="R"><label for="itemNm2">외화배송료</label>
				</div>
			</div>
			
			<div class="content">
				<div class="order">금액(원)</div>
				<div class="answer">
					<input type="number" name="amnt" id="amnt" placeholder="금액을 입력하세요">
				</div>
			</div>
			
			<div class="content">
				<div class="order">성명</div>
				<div class="answer">
					<input type="text" name="usrNm" id="usrNm" placeholder="성명을 입력하세요">
				</div>
			</div>
			
			<div class="content">
				<div class="order">이메일</div>
				<div class="answer">
					<input type="text" name="usrEmail" id="usrEmail" placeholder="이메일을 입력하세요">
				</div>
			</div>
			
			<div class="content" style="border-bottom:1px solid #d2d2d2;">
				<div class="order">등록번호</div>
				<div class="answer">
					<input type="text" name="usrContact" id="usrContact" placeholder="등록번호를 입력하세요">
				</div>
			</div>
			
			<div class="info">
				※ 소득공제용 : 휴대전화번호 또는 주민등록번호 입력<br>
				※ 지출증빙용 : 사업자등록번호 입력<br>
				※ 숫자 입력시 (-)는 제외하고 입력하세요<br>
			</div>
			
			<div class="btn" id="sub">발급신청</div>
		</div>
	</div>
</body>

<script type="text/javascript">

$(function () {

	$('#amnt').keyup(function(){
		checkVal();
	})

	$('#usrNm').keyup(function(){
		checkVal();
	})

	$('#usrEmail').keyup(function(){
		checkVal();
	})

	$('#usrContact').keyup(function(){
		checkVal();
	})

	$('#sub').click(function(){

		if($('#amnt').val() == ''){
			alert('금액을 입력하세요.');
			$('#amnt').focus();
			return;
		}
		if($('#usrNm').val() == ''){
			alert('성명을 입력하세요.');
			$('#usrNm').focus();
			return;
		}
		if($('#usrEmail').val() == ''){
			alert('이메일을 입력하세요.');
			$('#usrEmail').focus();
			return;
		}
		if($('#usrContact').val() == ''){
			alert('등록번호를 입력하세요.');
			$('#usrContact').focus();
			return;
		}
		
		var data = {};
		data["cashTp"] = $(":input:radio[name=cashTp]:checked").val();
		data["itemNm"] =  $(":input:radio[name=itemNm]:checked").val();
		data["amnt"] = $('#amnt').val();
		data["usrNm"] = $('#usrNm').val();
		data["usrEmail"] = $('#usrEmail').val();
		data["usrContact"] = $('#usrContact').val();
		
		$.ajax({
			type:"post",
			url:"${pageContext.request.contextPath}/cash/regCash",
			data: JSON.stringify(data),
			async : false,
			contentType: "application/json",
			success : function(result){
				var result = result.result;
				if(result == 'success'){
					alert('신청이 완료되었습니다.');
					location.reload(true);
				} else {
					alert('다시 시도해 주세요.');
				}
				
			},
			error : function(result){
				alert('업데이트 실패, 에러');
			}
		});
	});
})

function checkVal(){
	var check = 0;
	if($('#amnt').val() == ''){
		check = check + 1;
	}
	if($('#usrNm').val() == ''){
		check = check + 1;
	}
	if($('#usrEmail').val() == ''){
		check = check + 1;
	}
	if($('#usrContact').val() == ''){
		check = check + 1;
	}
	if(check == 0){
		$('#sub').css('background-color', 'rgba(13,13,13,1)');
	} else {
		$('#sub').css('background-color', 'rgba(13,13,13,0.3)');
	}
}
</script>
</html>
