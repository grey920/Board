<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>MVC 게시판</title>
<jsp:include page="header.jsp"></jsp:include>
<script src="js/modifyform.js"></script>
<style>
.container {
	width: 60%;
}

h1 {
	font-size: 1.5em;
	text-align: center;
	color: #1a92b9
}

#upfile{display:none}

label {
	font-weight: bold;
}
</style>
</head>
<body>
	<!--  게시판 수정 -->
	<div class="container">
		<form action="BoardModifyAction.bo" method="post"
			  enctype="multipart/form-data" name="modifyform">
			  <input type="hidden" name="BOARD_NUM" value="${boarddata.BOARD_NUM }">
			<h1>MVC 게시판 - 수정</h1>	
			<div class="form-group">
				<label for="board_name">글쓴이</label>
				<input type="text" class="form-control" value="${boarddata.BOARD_NAME }" readOnly>
			</div>
			
			<div class="form-group">
				<label for="board_subject">제목</label>
				<input name="BOARD_SUBJECT"  id="board_subject" 
							type="text" size="50" maxlength="100"
							class="form-control" value="${boarddata.BOARD_SUBJECT }">
			</div>
			
			<div class="form-group">
				<label for="board_content">내용</label>
				<textarea name="BOARD_CONTENT" id="board_content"
							 rows="15" class="form-control">${boarddata.BOARD_CONTENT }</textarea>
			</div>
			<%-- 원문글인 경우에만 파일 첨부 수정 가능합니다. --%>
			<c:if test="${boarddata.BOARD_RE_LEV == 0 }">
			<div class="form-group read">
				<label for="board_file">파일 첨부</label>
				<label for="upfile">
					<img src="image/attach.png" alt="파일첨부" width="20px">
				</label>
				<input type="file" id="upfile" name="BOARD_FILE">
				<span id="filevalue">${boarddata.BOARD_FILE }</span><!-- 첨부파일명 나오는 공간 -->
				  <img src="image/remove.png" alt="파일삭제" width="10px" class="remove">
			</div>
			</c:if>
			
			<div class="form-group">
				<label for="board_pass">비밀번호</label>
				<input name="BOARD_PASS" 
									id="board_pass" 
								type="password" size="10" maxlength="30"
							  class="form-control" placeholder="Enter board_pass" value="">
			</div>
			<div class="form-group">
				<button type=submit class="btn btn-primary">수정</button>
				<button type=reset class="btn btn-danger" onClick="history.go(-1)">취소</button>
			</div>
			
		</form>
		<!--  게시판 수정 -->
	</div>
</body>
</html>