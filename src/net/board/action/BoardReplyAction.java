//답변글
package net.board.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;

public class BoardReplyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = new ActionForward();
		BoardDAO boarddao= new BoardDAO();
		BoardBean boarddata = new BoardBean();
		int result=0;
		
		//파라미터로 넘어온 값들을 boarddata 객체에 저장합니다.
		boarddata.setBOARD_NUM(Integer.parseInt(request.getParameter("BOARD_NUM")));
		boarddata.setBOARD_NAME(request.getParameter("BOARD_NAME"));
		boarddata.setBOARD_PASS(request.getParameter("BOARD_PASS"));
		boarddata.setBOARD_SUBJECT(request.getParameter("BOARD_SUBJECT"));
		boarddata.setBOARD_CONTENT(request.getParameter("BOARD_CONTENT"));
		boarddata.setBOARD_RE_REF(Integer.parseInt(request.getParameter("BOARD_RE_REF")));
		boarddata.setBOARD_RE_LEV(Integer.parseInt(request.getParameter("BOARD_RE_LEV")));
		boarddata.setBOARD_RE_SEQ(Integer.parseInt(request.getParameter("BOARD_RE_SEQ")));
		
		//답변ㅇ르 DB에 저장하기 위해 boarddata 객체를 파라미터로
		//DAO의 메서드 boardReply를 호출합니다.
		result = boarddao.boardReply(boarddata);
		
		//답변 저장에 실패한 경우
		if(result == 0) {
			System.out.println("답장 저장 실패");
			forward = new ActionForward();
			forward.setRedirect(false);
			request.setAttribute("message", "답장 저장 실패입니다.");
			forward.setPath("error/error.jsp");
			return forward;
		}
		
		//답변 저장이 제대로 된 경우
		System.out.println("답장 완료");
		forward.setRedirect(true);
		//답변 글 내용을 확인하기 위해 글 내용 보기 페이지를 경로로 설정합니다.
		forward.setPath("BoardDetailAction.bo?num=" + result);
		return forward;
	}

}
