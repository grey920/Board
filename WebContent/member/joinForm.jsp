<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원관리 시스템 회원가입 페이지</title>
<link href="css/join.css" rel="stylesheet" type="text/css">
<script src="js/jquery-3.5.0.js"></script>
<script>
	$(function(){
		var checkid=false;
		var checkemail=false;
		
		$('form').submit(function(){
			if(!$.isNumeric($("input[name='age']").val())) {
				alert("나이는 숫자를 입력하세요");
				$("input[name='age']").val('');
				$("input[name='age']").focus();
				return false;
			}
			if(!checkid){
				alert("사용가능한 id로 입력하세요.");
				$("input:eq(0)").val('').focus();
				return false;
			}
			if(!checkemail){
				alert("email 형식을 확인하세요");
				$("input:eq(6)").focus();
				return false;
			}
			
		})//submit
		
	      $('input:eq(6)').on('keyup', function(){
	          $("#email_message").empty();
	          //[A-Za-z0-9_]와 동일한 것이 \w
	          //+는 1회 이상 반복을 의미합니다. {1,}와 동일합니다.
	          //\w+는 [A-Za-z0-9_]를 1개이상 사용하라는 의미
	          var pattern = /\w+@\w+[.]\w{3}/;
	          var email = $("input:eq(6)").val();
	          if(!pattern.test(email)){
	             $("#email_message").css('color', 'red').html("이메일형식이 맞지 않습니다.");
	             checkemail = false;   
	          } else {
	             $("#email_message").css('color', 'green').html("이메일형식에 맞습니다.");
	             checkemail = true;   
	          }
	       });   //email keyup 이벤트 처리 끝 
	       
	       $("input:eq(0)").on('keyup',function(){
	    	   $("#message").empty();//처음에 pattern에 적합하지 않은 경우 메시지 출력 후 적합한
	    	   //[A-Za-z0-9_]의 의미가 \w
	    	   var pattern = /^\w{5,12}$/;
	    	   var id = $("input:eq(0)").val();
	    	   if(!pattern.test(id)){
	    		   $("#message").css('color','red').html("영문자 숫자 _로 5~12자 가능합니다.");
	    		   checkid = false;
	    		   return;//ajax를 실행하지 않기 위해서 56번째 라인으로 리턴.
	    	   }
	    	   
	    	   //위에 패턴을 만족하면 여기로 내려옴
	    	   $.ajax({
	    		   url : "idcheck.net",
	    		   data : {"id" : id}, //폼에서 입력한 id가 idcheck.net으로 같이 따라간다 
	    		   success : function(resp){
	    			   if(resp == -1){ //아이디가 -1이면 db에 해당 id가 없다는 것
	    				   $("#message").css('color','green').html("사용 가능한 아이디 입니다.");
	    				   checkid = true;
	    			   }else{//db에 해당 id가 있는 경우(0)
	    				   $("#message").css('color','blue').html("사용중인 아이디 입니다.");
	    				   checkid = false;
	    			   }
	    		   }
	    	   });//ajax end
	    	   
	       })//id keyup end
	})
</script>
</head>
<body>
	<form name="joinform" action="joinProcess.net" method="post">
		<h1>회원가입</h1>
		<hr>
		<b>아이디</b> <input type="text" name="id" placeholder="Enter id"
			required maxlength="12"> <span id="message"></span> 
		<b>비밀번호</b>
		<input type="password" name="pass" placeholder="Enter password"
			required> 
			<b>이름</b> <input type="text" name="name"
			placeholder="Enter name" maxlength=15 required> <b>나이</b> <input
			type="text" name="age" placeholder="Enter age" maxlength=2 required>
		<b>성별</b>
		<div>
			<input type="radio" name="gender" value="남" checked><span>남자</span>
			<input type="radio" name="gender" value="여"><span>여자</span>
		</div>
		<b>이메일 주소</b> <input type="text" name="email"
			placeholder="Enter email" required> <span id="email_message"></span>
		<div class="clearfix">
			<button type="submit" class="submitbtn">회원가입</button>
			<button type="reset" class="cancelbtn">다시작성</button>
		</div>
	</form>
</body>
</html>