<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {
		$('#id').blur(function() {
			//1. xhttprequset 생성
			var xhttp;
			if(window.XMLHttpRequest){
				xhttp = new XMLHttpRequest();
			}else{
				xhttp = new ActiceXObject("Microsoft.XMLHTTP");
			}//너가 사용하는 브라우저가 현대버전이냐 아니면 옛날꺼냐
			
			//2. 요청정보 기록
			//xhttp.open("GET", "../member/idCheck?id="+$(this).val()); //동기, 비동기 안써주면 기본값은 true
			xhttp.open("POST", "../member/idCheck?id="+$(this).val()); 
			
			//3. 전송
			//xhttp.send();//get방식
			xhttp.send("id="+$('#id').val());
			
			//4. response처리
			xhttp.onreadystatechange = function() {
				if(this.readyState==4 && this.status == 200){//에러가 발생해도, 안해도 4번 정상적으로 확인하기 위해서 자기자신이 200이면 정상{
					//alert(this.responseText.trim()=='1');//true가 뜨면 없는 아이디
					if(this.responseText.trim()=='1'){
						$('#result').html("사용가능한 ID");
						$('#result').css("color", "blue");
					}else{
						$('#result').html("사용불가능한 ID");
						$('#result').css("color", "red");
						$('#id').val("").focus();
					}
				}
			}
		
		});
	});
</script>
</head>
<body>
	<input type="text" id="id" value="${id}">
	<div id="result"></div>
</body>
</html>