<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<jsp:include page="header.jsp"></jsp:include>
<script src="js/writeform.js"></script>
<style>
h1 {
	font-size: 1.5em;
	text-align: center;
	color: #1a92b9
}

.container {
	width: 60%;
}

label {
	font-weight: bold;
}

#upfile {
	display: none
}

img {
	width: 20px;
}
</style>
</head>
<body>

	<div class="container">
		<form action="BoardAddAction.bo" method="post"
			  enctype="multipart/form-data" name="boardform">
			<h1>MVC 게시판-write 페이지</h1>	
			<div class="form-group">
				<label for="board_name">글쓴이</label>
				<input name="BOARD_NAME" id="board_name" value="${id}"
				readOnly
				type="text" size="10" maxlength="30"
				class="form-control"
				placeholder="Enter board_name">
			</div>
			<div class="form-group">
				<label for="board_pass">비밀번호</label>
				<input name="BOARD_PASS" id="board_pass" 
				type="password" size="10" maxlength="30"
				class="form-control" placeholder="Enter board_pass">
			</div>
			<div class="form-group">
				<label for="board_subject">제목</label>
				<input name="BOARD_SUBJECT" id="board_subject" 
				type="text" size="50" maxlength="100"
				class="form-control" placeholder="Enter board_subject">
			</div>
			<div class="form-group">
				<label for="board_content">내용</label>
				<textarea name="BOARD_CONTENT" id="board_content"
							cols="67" rows="10" class="form-control"></textarea>
			</div>
			<div class="form-group">
				<label for="board_file">파일 첨부</label>
				<label for="upfile">
					<img src="image/attach.png" alt="파일첨부">
				</label>
				<input type="file" id="upfile" name="BOARD_FILE">
				<span id="filevalue"></span>
			</div>
			<div class="form-group">
				<button type=submit class="btn btn-primary">등록</button>
				<button type=reset class="btn btn-danger">취소</button>
			</div>
		</form>
	</div>
</body>
</html>