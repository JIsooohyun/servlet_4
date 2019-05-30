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
		$("#updateBtn").click(function() {
			var contents = $("#updateContents").val();
			var cnum = $('#cnum').val();
			$.post("../comments/commentsUpadte", {
				contents : contents,
				cnum : cnum
			}, function(data){
				data = data.trim();
				if(data=='1'){
					alert("성공");
					//getList(1); 단점 : 
					$("#c"+cnum).html(contents); //새로고침안해도 바뀐것 처럼 보인다. 
				}else{
					alert("실패");
				}
			});
		});

		$("#commentsList").on('click', '.up', function() {
			var id = $(this).attr("title");
			var con = $('#c'+id).html();
			$('#updateContents').val(con);
			$('#cnum').val(id);
			
			
		});
		
		var curPage = 1;
		$("#more").click(function() {
			curPage++;
			getList(curPage);

		});

		$('#btn').click(function() {
			var writer = $('#writer').val();
			var contents = $('#contents').val();
			var num = '${dto.num}'

			$.post("../comments/commentsWrite", {
				writer : writer, //변수 : 파라미터이름
				contents : contents,
				num : num
			}, function(data) {
				data = data.trim();
				if (data == '1') {
					alert('성공');
					getList(1);
					//location.reload();//새로고침과 비슷한 역할
				} else {
					alert('실패');
				}
			});
			
		});

		//로딩과 동시에 리스트 가져오기
		function getList(count) {//해당글에 대한 리스트만 가져오기 (글번호를 넘겨줘야 한다.)
			$.get("../comments/commentsList?num=${dto.num}&curPage=" + count,
					function(data) {
						if (count == 1) {
							$("#commentsList").html(data); //처음에는 덮어씌우기
						} else {
							$("#commentsList").append(data);
						}
					});

		}

		$("#commentsList").on(
				"click",
				".del",
				function() {//위임받아서 사용
					var cnum = $(this).attr("id");
					var check = confirm("delete check");
					if (check) {
						$.get("../comments/commentsDelete?cnum=" + cnum,
								function(data) {
									data = data.trim();
									if (data == "1") {
										alert("삭제 성공");
										getList(1);
									} else {
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
				<td><div>${dto.contents}</div></td>
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
				<button id="more">더보기</button>
			</div>
		</div>
	</c:if>
	<div class="container">
		<div class="modal fade" id="myModal" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title">${memberDTO.id}</h4>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<label for="contents">Contents:</label>
							<textarea class="form-control" rows="5" id="updateContents" name="contents"></textarea>
							<input type="hidden" id="cnum">
						</div>
					</div>
					<div class="modal-footer">
							<button class="btn btn-danger" id="updateBtn" data-dismiss="modal">Update</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>

			</div>
		</div>
	</div>
</body>
</html>