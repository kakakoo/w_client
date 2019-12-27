<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>은행 테스트</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
</head>
<body>
	
	${res }
<script type="text/javascript">

function submit(){
	var data = {};
	data["usrNm"] = $('#usrNm').val();
	data["usrEmail"] = $('#usrEmail').val();
	data["usrBirth"] = $('#usrBirth').val();
	data["usrTelTp"] = $('#usrTelTp').val();
	data["usrTel"] = $('#usrTel').val();
	data["usrSex"] = $('#usrSex').val();
	data["usrBankNm"] = $('#usrBankNm').val();
	data["usrBankCd"] = $('#usrBankCd').val();
	data["usrBankNick"] = $('#usrBankNick').val();
	
	$.ajax({
		type:"post",
		url:"${pageContext.request.contextPath}/api/bank/v9/authorize",
		data: JSON.stringify(data),
		async : false,
		contentType: "application/json",
		success : function(result){
			
		},
		error : function(result){
			alert('서버 에러, 다시 시도해주세요.');
		}
	});
}

</script>
</body>
</html>