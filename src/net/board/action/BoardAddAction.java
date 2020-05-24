package net.board.action;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;

public class BoardAddAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		ActionForward forward = new ActionForward();
		
		String realFolder="";
		
		//WebContent아래에 꼭 폴더 생성하세요
		String saveFolder = "boardupload";
		
		int fileSize = 5*1024*1024; //업로드할 파일의 최대 사이즈 입니다. 5MB
		
		//실제 저장 경로를 지정합니다.
		ServletContext sc = request.getServletContext();
		realFolder = sc.getRealPath(saveFolder);
		/*
		 realFolder = 
		 request.getSession().getServletContext().getRealPath(saveFolder);
		 */
		System.out.println("realFolder= "+ realFolder);
		boolean result = false;
		
		try {
			MultipartRequest multi = null;
			multi = new MultipartRequest(request, 
						realFolder,
						fileSize, 
						"utf-8",
						new DefaultFileRenamePolicy());
			
			//BoardBean 객체에 글 등록 폼에서 입력 받은 정보들을 저장합니다.
			boarddata.setBOARD_NAME(multi.getParameter("BOARD_NAME"));
			boarddata.setBOARD_PASS(multi.getParameter("BOARD_PASS"));
			boarddata.setBOARD_SUBJECT(multi.getParameter("BOARD_SUBJECT"));
			boarddata.setBOARD_CONTENT(multi.getParameter("BOARD_CONTENT"));
			
			//업로드된 파일의 시스템 상에 업로드된 실제 파일명을 얻어 옵니다.
			String filename = multi.getFilesystemName("BOARD_FILE");
			boarddata.setBOARD_FILE(filename);
			
			//글 등록 처리를 위해 DAO의 boardInsert()메서드를 호출합니다.
			//글 등록 폼에서 입력한 정보가 저장되어 있는 boarddata객체를 전달합니다.
			result = boarddao.boardInsert(boarddata);
			
			//글 등록에 실패할 경우 false를 반환합니다.
			if(result == false) {
				System.out.println("게시판 등록 실패");
				forward.setPath("error/error.jsp");
				request.setAttribute("meesage", "게시판 등록 실패입니다.");
				forward.setRedirect(false);
				return forward;
			}
			System.out.println("게시판 등록 완료");
			
			//글 등록이 완료되면 글 목록을 단순히 보여주기만 할 것이므로 Redirect여부를 true로 설정합니다.
			forward.setRedirect(true);
			
			forward.setPath("BoardList.bo");//이동할 경로를 지정합니다
			return forward;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
