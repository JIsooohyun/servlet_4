<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	window.onload = function() {
		var btn = document.getElementById('btn');
		var num = document.getElementById('num');
		
		//btn.onClick = function() {}
		btn.addEventListener("click", function() {
			//../notice/noticeSelect
			var xhttp;
			if(window.XMLHttpRequest){//XMLHttpRequest가 지원한다면
				xhttp = new XMLHttpRequest();
			}else{
				xhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}
			
			xhttp.open("GET", "../notice/noticeSelect?num="+num.value, true);
			xhttp.send();
			xhttp.onreadystatechange=function(){
				if(this.readyState==4 && this.status==200){
					alert(this.responseText);
				}
			}
		});
	}
</script>
</head>
<body>
<input type="text" id="num">
<button id="btn">CLICK</button>
</body>
</html>