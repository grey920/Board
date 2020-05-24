package net.member.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.db.Member;
import net.member.db.MemberDAO;

public class ListAction implements Action {

    public ActionForward execute(HttpServletRequest request, HttpServletResponse response)throws Exception {
      ActionForward forward = new ActionForward();
      MemberDAO dao = new MemberDAO();
      
      int page=1;
      int limit=3;
      
      if(request.getParameter("page")!=null){
         page = Integer.parseInt(request.getParameter("page"));
      }
      System.out.println("넘어온 페이지 = "+page);
      
      int listcount = dao.getListCount();
      List<Member> list = dao.getList(page, limit);
      
      int maxpage = (listcount + limit -1)/limit;
      System.out.println("총 페이지수 = "+maxpage);
      
      int startpage = ((page-1)/10) * 10 +1;
      int endpage = startpage +10 -1;
      System.out.println("현재 페이지에 보여줄 마지막 페이지 수 = "+endpage);
      System.out.println("현재 페이지에 보여줄 시작 페이지 수 = "+startpage);
      
      if(endpage>maxpage) endpage=maxpage;
      request.setAttribute("page", page);
      request.setAttribute("maxpage", maxpage);
      request.setAttribute("startpage", startpage);
      request.setAttribute("endpage", endpage);
      request.setAttribute("listcount", listcount);
      request.setAttribute("totallist", list);
      
      System.out.println(list.size());
      forward.setPath("member/member_list.jsp");
      forward.setRedirect(false);
       return forward;
   }

}