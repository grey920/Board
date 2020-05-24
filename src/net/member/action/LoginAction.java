package net.member.action;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAction implements Action {
	LoginAction(){}

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id="";
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for (int i=0; i<cookies.length; i++) {
				if(cookies[i].getName().equals("id")) {
					id = cookies[i].getValue();
				}
			}
		}
		
		request.setAttribute("id", id);
		ActionForward forward = new ActionForward();
		forward.setRedirect(false); //주소변경 없이 jsp페이지의 내용을 보여줍니다. 
									//왜? request에 id값을 가져가니까!
		forward.setPath("member/loginForm.jsp"); //화면 보여줄 루트
		
		return forward;
	}

}
