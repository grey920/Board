package net.board.action;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.bo") //속성 이름은 팀원들이 다 똑같이 맞춘다
public class BoardFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public BoardFrontController() {
        super();
        
    }

    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	/*
    	 요청된 전체 URI 중에서 포트번호 다음부터 마지막 문자열까지 반환됩니다.
    	 예) http://localhost:8088/Board_Ajax_bootstrap/login.net인 경우
    	  "Board_Ajax_bootstrap/login.net"이 반환됩니다.
    	 */
    	String RequestURI = request.getRequestURI();
    	System.out.println("RequestURI = " + RequestURI);
    	
    	//getContextPath() : 컨텍스트 경로가 반환됩니다.
    	//contextPath는 "/Board_Ajax_bootstrap "가 반환됩니다.
    	String contextPath= request.getContextPath();
    	System.out.println("contextPath = " + contextPath);
    	
    	//RequestURI에서 컨텍스트 경로 길이 값의 인덱스 위치의 문자부터
    	//마지막 위치 문자까지 추출합니다.
    	//command는 "/login.net" 반환됩니다.
    	String command = RequestURI.substring(contextPath.length());
    	System.out.println("command = " + command);
    	
    	//초기화
    	ActionForward forward = null;
    	Action action = null;
    	
    	if(command.equals("/BoardList.bo")) {
    		action = new BoardListAction(); //다형성에 의한 업캐스팅
    		try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else if(command.equals("/BoardWrite.bo")){
    		forward = new ActionForward();
    		forward.setRedirect(false);//처리할 내용이 없어서 주소 변경없이 바로 view페이지로 이동
    		forward.setPath("board/qna_board_write.jsp");
    	}else if(command.equals("/BoardAddAction.bo")) {
    		action = new BoardAddAction();
    		try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else if(command.equals("/BoardDeleteAction.bo")) {
    		action = new BoardDeleteAction();
    		try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else if(command.equals("/BoardReplyAction.bo")) {
    		action = new BoardReplyAction();
    		try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else if(command.equals("/BoardDelete.bo")) {
    		forward = new ActionForward();
    		forward.setRedirect(false);//포워딩 방식으로 주소가 바뀌지 않아요
    		forward.setPath("./board/qna_board_delete.jsp");
    	}else if(command.equals("/BoardModifyView.bo")) {
    		action = new BoardModifyView();
    		try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else if(command.equals("/BoardReplyView.bo")) {
    		action = new BoardReplyView();
    		try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else if(command.equals("/BoardModifyAction.bo")) {
    		action = new BoardModifyAction();
    		try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else if(command.equals("/BoardDetailAction.bo")) {
    		action = new BoardDetailAction();
    		try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else if(command.equals("/BoardFileDown.bo")) {
    		action = new BoardFileDown();
    		try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	
    	}//command 별로 분기 끝
    	
    	//이동하는 것 처리는 여기서!
    	if(forward != null) {
    		if(forward.isRedirect()) {//트루면 리다이렉트 됩니다.
    			response.sendRedirect(forward.getPath());
    		}else {//포워딩됩니다
    			RequestDispatcher dispatcher = 
    					request.getRequestDispatcher(forward.getPath());
    			dispatcher.forward(request, response);
    		}
    	}
    }
	
    //doProcess(request, response)메서드를 구현하여 요청이 GET방식이든
    //POST 방식으로 전송되어 오든 같은 메서드에서 요청을 처리할 수 있도록 하였습니다.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		doProcess(request, response);
	}

}
