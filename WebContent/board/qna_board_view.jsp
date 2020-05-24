<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<html>
<head>
<title>MVC 게시판 - view</title>
<jsp:include page="header.jsp"></jsp:include>
<script>
	$(function(){
		$("form").submit(function(){
			if($("#board_pass").val() == ''){
				alert("비밀번호를 입력하세요");
				$("#board_pass").focus();
				return false;
			}
		})
	})
</script>
<style>
tr:nth-child(1){
text-align:center;
}

td:nth-child(1){
width:20%
}

a{color:white}

tr:nth-child(5)> td:nth-child(2)> a{color:black;}

tbody tr:last-child{
text-align: center;
}

.btn-primary{
background-color: #4f97e5}

#myModal{
display: none;
}

</style>
</head>
<body>
	<div class="container">
		<table class="table table-striped">
		<tr>
			<th colspan="2">MVC 게시판-view페이지</th>
		</tr>
		<tr>
			<td><div>글쓴이</div></td>
			<td><div>${boarddata.BOARD_NAME}</div></td>
		</tr>
		<tr>
			<td><div>제목</div></td>
			<td><div>${boarddata.BOARD_SUBJECT}</div></td>
		</tr>
		<tr>
			<td><div>내용</div></td>
			<td><textarea class="form-control" rows="5"
				readOnly style="width:102%">${boarddata.BOARD_CONTENT}</textarea></td>
		</tr>
	<c:if test="${boarddata.BOARD_RE_LEV==0 }"><%--원문글인 경우에만 첨부파일을 추가할 수 있습니다. --%>	
		<tr>
			<td><div>첨부파일</div></td>
		<c:if test="${!empty boarddata.BOARD_FILE }"><%--파일첨부한 경우 --%>
		<td><img src="image/down.png" width="10px">
			<a href="BoardFileDown.bo?filename=${boarddata.BOARD_FILE}">
			${boarddata.BOARD_FILE }</a></td>
	</c:if>	
	<c:if test="${empty boarddata.BOARD_FILE }"><%--파일 첨부하지 않은 경우 --%>
		<td></td>
	</c:if>
	</tr>
	</c:if>
	<tr>
		<td colspan="2" class="center">
		<a href="BoardReplyView.bo?num=${boarddata.BOARD_NUM }">
			<button class="btn btn-primary">답변</button>
		</a>
		<c:if test="${boarddata.BOARD_NAME == id || id == 'admin' }">
		<a href="BoardModifyView.bo?num=${boarddata.BOARD_NUM }">
			<button class="btn btn-info">수정</button>
		</a>
		<%-- href의 주소를 #으로 설정합니다. --%>
		<a href="#">
		<button class="btn btn-danger" data-toggle="modal"
				data-target="#myModal">삭제</button>
		</a>
		</c:if>
		<a href="BoardList.bo">
			<button class="btn btn-primary">목록</button>
		</a>
		</td>
		</tr>
		</table>
		<%--게시판 수정 end --%>
		
		<%--modal 시작 --%>
		<div class="modal" id="myModal">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <!-- Modal body -->
		      <div class="modal-body">
		        <form name="deleteForm" action="BoardDeleteAction.bo" method="post">
		        <%--http://localhost:8088/Board_ajax_bootstrap/BoardDetailAction.bo?num=22
		        	주소를 보면 num을 파라미터로 넘기고 있습니다.
		        	이 값을 가져와서 ${param.num}를 사용
		        	또는 ${boarddata.BOARD_NUM}
		        	--%>
		        	<input type="hidden" name="num" value="${param.num }">
		        	<div class="form-group">
		        	  <label for="pwd">비밀번호</label>
		        	  <input type="password"
		     				class="form-control" placeholder="Enter password"
		     				name="BOARD_PASS" id="board_pass">
		        	</div>
		        	<button type="submit" class="btn btn-primary">전송</button>
					<button type="button" class="btn btn-danger"
							data-dismiss="modal">취소</button>
		        </form>
		      </div>
		    </div> 
		  </div>
		</div>
	</div>
</body>
</html>