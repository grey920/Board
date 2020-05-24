<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%
String id = "";

Cookie[] cookies = request.getCookies(); //쿠키생성
if(cookies != null){
   for(int i = 0 ; i < cookies.length ; i++){
      if(cookies[i].getName().equals("id")){
         id = cookies[i].getValue();
      }
   }
}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>회원관리 시스템 로그인 페이지</title>
<%--context 경로(프로젝트이름) 기분으로 위치를 잡는다 --%>
<link href="css/login.css" type="text/css" rel="stylesheet">
<script src="js/jquery-3.5.0.js"></script>
<script>

$(function(){
  	$(".join").click(function(){
  		location.href="join.net";
  	});
	var id='${id}';
	if(id){
		$("#id").val(id);
  		$("#remember").prop("checked",true);
	}
})
</script>
</head>
<body>
	
   <form name="loginform" action="loginProcess.net" method="post">
      <h1>로그인</h1>
      <hr>
      <b>아이디</b>
         <input type="text" name="id" placeholder="Enter id" required>
      <b>비밀번호</b>
         <input type="password" name="pass" placeholder="Enter password" required>
         <input type="checkbox" id="remember" name="remember" value="store">remember
      <div class="clearfix">
         <button type="submit" class="submitbtn">로그인</button>
         <button type="button" class="join">회원가입</button>
      </div>
   </form>
</body>
</body>
</html>