<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>회원관리 시스템 관리자모드(회원 목록 보기)</title>
<style>
   table caption{caption-side:top; text-align:center}
   h1{text-align:center;}
   .center-block{display:flex; justify-content:center;} /* 가운데 정렬 */
   li .current{background:#faf7f7; color:gray;}
</style>
<jsp:include page="/board/header.jsp"/>
<script src="js/jquery-3.5.0.js"></script>
<script>
   $(function(){
      //검색 후 응답화면에는 검색시 선택한 필드가 선택되도록 합니다.
      var selectedValue = "${search_field}";
      if(selectedValue != '-1')
         $("#viewcount").val(selectedValue);
      
      //검색어 공백 유효성 검사합니다.
      $("button").click(function(){
         if($("input").val() == ''){
            alert("검색어를 입력하세요");
            return false;
         }
      });
      
      //성별의 경우 placeholder 나타나도록 합니다. -- 수정해주세요
      $("#viewcount").change(function(){
         selectedValue = $(this).val();
         $("input").val('');
         message=["아이디","이름","나이","여 또는 남"]
         $("input").attr("placeholder",message[selectedValue] + " 입력하세요");
      })
      
      $("tr > td:nth-child(3) > a").click(function(event){
         var answer = confirm("정말 삭제하시겠습니까?");
         console.log(answer); //취소를 클릭한 경우 - false
         if(!answer){   //취소를 클릭한 경우
            event.preventDefault();
         }
      })
   })
</script>
<style>
   form{margin:0 auto; width:80%; text-align:center; }
   select{
      color:#495057;
      background-color: #fff;
      background-clip: padding-box;
      border: 1px solid #ced4da;
      border-radius: .25rem;
      transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
   }
   .container{width:60%}
   td:nth-child(1){width:33%;}
</style>
</head>
<body>
<div class="container">
   <form action="member_list.net">
      <div class="input-group">
         <select id="viewcount" name="search_field">
            <option value="0" selected>아이디</option>
            <option value="1">이름</option>
            <option value="2">나이</option>
            <option value="3">성별</option>
         </select>
         <input name="search_word" type="text" class="form-control"
            placeholder="아이디를 입력하세요" value="${search_word}">
         <button class="btn btn-primary" type="submit">검색</button>
      </div>
   </form>
   <%-- 회원이 있는 경우 --%>
   <c:if test="${listcount > 0 }">
      <table class="table table-striped">
        <caption>회원 목록</caption>
        <thead>
           <tr>
              <th colspan="2">MVC 게시판 - 회원 정보 list</th>
              <th>
                 <font size=3>회원 수 : ${listcount}</font>
              </th>
           </tr>   
           <tr>
              <td>아이디</td><td>이름</td><td>삭제</td>
           </tr>
        </thead>
        <tbody>
        <c:forEach var="m" items="${totallist}">
           <tr>
             <td>
                <a href="member_info.net?id=${m.id}">${m.id}</a>
             </td>
             <td>${m.name}</td>
             <td><a href="member_delete.net?id=${m.id}">삭제</a></td>
            </tr>    
        </c:forEach>   
        </tbody>
      </table>
   <div class="center-block">
      <div class="row">
         <div class="col">
            <ul class="pagination">
            <c:if test="${page <= 1 }">
               <li class="page-item">
                  <a class="page-link current" href="#">이전&nbsp;</a>
               </li>
            </c:if>   
            <c:if test="${page >1 }">
               <li class="page-item">
               <a href="member_list.net?page=${page-1}&search_field=${search_field}&search_word=${search_word}"
               class="page-link">이전</a>&nbsp;
               </li>
            </c:if>
            
            <c:forEach var="a" begin="${startpage}" end="${endpage}">
               <c:if test="${a == page }">
                  <li class="page-item">
                  <a class="page-link current" href="#">${a}</a>
                  </li>
               </c:if>
               <c:if test="${a != page }">
                  <li class="page-item">
                  <a href="member_list.net?page=${a}&search_field=${search_field}&search_word=${search_word}"
                  class="page-link">${a}</a>
                  </li>
               </c:if>
            </c:forEach>
            
            <c:if test="${page >= maxpage }">
               <li class="page-item">
                  <a class="page-link current" href="#">&nbsp;다음</a>
               </li>
            </c:if>
            <c:if test="${page < maxpage }">
               <li class="page-item">
               <a href="member_list.net?page=${page+1}&search_field=${search_field}&search_word=${search_word}"
               class="page-link">&nbsp;다음</a>
               </li>
            </c:if>
            </ul>
         </div>
      </div>
   </div>   
   </c:if>
</div>

<%-- 회원이 없는 경우 --%>
<c:if test="${listcount == 0 }">
   <h1>  검색 결과가 없습니다.  </h1>
</c:if>
</body>
</html>