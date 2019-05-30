<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<c:import url="../temp/bootstrap.jsp"></c:import>
<script type="text/javascript">
	$(function() {
		getList();
		$('#btn').click(
				function() {
					var writer = $('#writer').val();
					var contents = $('#contents').val();
					var num = '${dto.num}'

					var xhttp;
					if (window.XMLHttpRequest) {
						xhttp = new XMLHttpRequest();
					} else {
						xhttp = new ActiveXObject("Microsoft.XMLHTTP");
					}

					xhttp.open("POST", "../comments/commentsWrite", true);
					xhttp.setRequestHeader("Content-type",
							"application/x-www-form-urlencoded");

					xhttp.send("num=" + num + "&writer=" + writer
							+ "&contents=" + contents);

					xhttp.onreadystatechange = function() {
						if (this.readyState == 4 && this.status == 200) {
							var result = this.responseText.trim(); //response에서 값이 넘어온다.
							if (result == '1') {
								alert('성공');
								location.reload();
							} else {
								alert('실패');
							}
						}
					}
				});

		//로딩과 동시에 리스트 가져오기
		function getList() {
			var xhttp;
			if (window.XMLHttpRequest) {
				xhttp = new XMLHttpRequest();
			} else {
				xhttp = new ActiveXObject("Microsoft.XMLHTTP");
			}

			xhttp.open("GET", "../comments/commentsList", true);

			xhttp.send();

			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					$("#commentsList").append(this.responseText);
				}
			}

		}

		$("#commentsList").on("click", ".del", function() {//위임받아서 사용
			var cnum = $(this).attr("id");
			var check = confirm("delete check");
			if(check) {
				$.get("../comments/commentsDelete?cnum="+cnum, function(data) {
					data = data.trim();
					if(data=="1"){
						alert("삭제 성공");
						location.reload();
					}else{
						alert("삭제 실패");
					}
				}); /*url주소, 콜백함수, data<- reresponseText*/
			}
		});
	});
</script>
</head>
<body>
	<c:import url="../temp/header.jsp"></c:import>
	<div class="container">
		<h1>${board}List</h1>
		<table class="table table-hover;">
			<tr>
				<td>NUM</td>
				<td>TITLE</td>
				<td>CONTENTS</td>
				<td>WRITER</td>
				<td>DATE</td>
				<td>HIT</td>
			</tr>

			<tr>
				<td>${dto.num}</td>
				<td>${dto.title}</td>
				<td>${dto.contents}</td>
				<td>${dto.writer}</td>
				<td>${dto.reg_date}</td>
				<td>${dto.hit}</td>
			</tr>
		</table>
	</div>
	<div class="container">
		<a href="./${board}Update?num=${dto.num}">update</a>
	</div>
	<c:if test="${board ne 'notice'}">
		<div class="container">
			<!-- 댓글입력폼 -->
			<div class="row">
				<!-- 원래는 로그인한 사람의 아이드를 꺼내서 입력되어 있어야 한다. -->
				<div class="form-group">
					<label for="writer">Writer:</label> <input type="text"
						class="form-control" id="writer" name="writer">
				</div>
				<div class="form-group">
					<label for="contents">Contents:</label>
					<textarea class="form-control" rows="5" id="contents"
						name="contents"></textarea>
					<button class="btn btn-danger" id="btn">Write</button>
				</div>
			</div>

			<!-- 댓글리스트 -->
			<div class="row">
				<table class="table table-bordered" id="commentsList">

				</table>
			</div>
		</div>
	</c:if>
</body>
</html>