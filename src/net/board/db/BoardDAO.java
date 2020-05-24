package net.board.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	DataSource ds;

	// 생성자에서 JNDI 리소스를 참조하여 Connection객체를 얻어온다
	public BoardDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception ex) {
			System.out.println("DB 연결 실패 : " + ex);
			return;
		}
	}

	// 글의 갯수 구하기
	public int getListCount() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement("select count(*) from board");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				x = rs.getInt(1);
			}
		} catch (Exception ex) {
			System.out.println("getListCount() 에러 : " + ex);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		} // finally
		return x;
	}

	// 글 목록 보기
	public List<BoardBean> getBoardList(int page, int limit) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// page : 페이지
		// limit : 페이지 당 목록의 수
		// BOARD_RE_REF desc, BOARD_RE_SEQ asc에 의해 정렬한 것을
		// 조건절에 맞는 rnum의 범위만큼 가져오는 쿼리문입니다.

		String board_list_sql = "select * from " 
							+ "	(select rownum rnum, BOARD_NUM, BOARD_NAME,"
							+ "		BOARD_SUBJECT, BOARD_CONTENT, BOARD_FILE," 
							+ "		BOARD_RE_REF, BOARD_RE_LEV, BOARD_RE_SEQ,"
							+ "		BOARD_READCOUNT, BOARD_DATE" 
							+ "		from (select * from board "
							+ "			  order by BOARD_RE_REF desc," 
							+ "			  BOARD_RE_SEQ asc) " 
							+ "	) "
							+ "where rnum>=? and rnum<=?";
		List<BoardBean> list = new ArrayList<BoardBean>();
		// 한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지 ...
		int startrow = (page - 1) * limit + 1;// 읽기 시작할 row번호(1 11 21 31 ...)
		int endrow = startrow + limit - 1; // 읽을 마지막 row번호 (10 20 30 40 ...)
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(board_list_sql);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();

			// DB에서 가져온 데이터를 BoardBean객체에 담습니다.
			while (rs.next()) {
				BoardBean board = new BoardBean();
				board.setBOARD_NUM(rs.getInt("BOARD_NUM"));
				board.setBOARD_NAME(rs.getString("BOARD_NAME"));
				board.setBOARD_SUBJECT(rs.getString("BOARD_SUBJECT"));
				board.setBOARD_CONTENT(rs.getString("BOARD_CONTENT"));
				board.setBOARD_FILE(rs.getString("BOARD_FILE"));
				board.setBOARD_RE_REF(rs.getInt("BOARD_RE_REF"));
				board.setBOARD_RE_LEV(rs.getInt("BOARD_RE_LEV"));
				board.setBOARD_RE_SEQ(rs.getInt("BOARD_RE_SEQ"));
				board.setBOARD_READCOUNT(rs.getInt("BOARD_READCOUNT"));
				board.setBOARD_DATE(rs.getString("BOARD_DATE"));
				list.add(board);// 값을 담은 객체를 리스트에 저장합니다.
			}

		} catch (Exception ex) {
			System.out.println("getBoardLsit() 에러 : " + ex);

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		} // finally

		return list;
	}

	// 최대값 구하기1. 시퀀스 이용 2.Max이용 => 값가져오기 rs
	public boolean boardInsert(BoardBean board) {
		Connection con = null;
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;
		int num = 1;
		String sql = "";
		boolean result = false;
		try {
			con = ds.getConnection();

			// board테이블의 board_num필드의 최대값을 구해와서 글을
			// 등록할 때 글 번호를 순차적으로 지정하기 위함입니다.
			String max_sql = "select max(board_num) from board";
			pstmt = con.prepareStatement(max_sql);
			rs = pstmt.executeQuery();

			/*
			 * select max(board_num) from board 처음 글쓰기를 하는 경우 rs.getInt(1)은 0입니다. if the
			 * value is SQL NULL, the value returned is 0
			 */

			if (rs.next()) {
				System.out.println("rs.getInt(1)=" + rs.getInt(1));
				num = rs.getInt(1) + 1;// 최대값보다 1만큼 큰 값을 지정합니다.
			}

			sql = "insert into board " 
					+ "(BOARD_NUM,BOARD_NAME, BOARD_PASS, BOARD_SUBJECT,"
					+ "BOARD_CONTENT, BOARD_FILE, BOARD_RE_REF," 
					+ "BOARD_RE_LEV, BOARD_RE_SEQ, BOARD_READCOUNT,"
					+ "BOARD_DATE) " 
					+ "values(?,?,?,?,?,?,?,?,?,?,sysdate)";

			// 새로운 글을 등록하는 부분입니다.
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, num);
			pstmt2.setString(2, board.getBOARD_NAME());
			pstmt2.setString(3, board.getBOARD_PASS());
			pstmt2.setString(4, board.getBOARD_SUBJECT());
			pstmt2.setString(5, board.getBOARD_CONTENT());
			pstmt2.setString(6, board.getBOARD_FILE());
			pstmt2.setInt(7, num); // 원문글의 BOARD_RE_REF 필드는 자신의 글번호 입니다.

			// 원문의 경우 BOARD_RE_LEV, BOARD_RE_SEQ 필드 값은 0입니다.
			pstmt2.setInt(8, 0);// BOARD_RE_LEV 필드
			pstmt2.setInt(9, 0);// BOARD_RE_SEQ 필드
			pstmt2.setInt(10, 0);// BOARD_RE_READCOUNT 필드

			int r = pstmt2.executeUpdate();

			System.out.println("데이터 삽입이 모두 완료되었습니다.");
			if (r == 1)
				result = true;

		} catch (Exception ex) {
			System.out.println("boardInsert() 에러 : " + ex);
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		} // finally

		return result;
	}

	//조회수 업데이트 - 글번호에 해당하는 조회수를 1 증가합니다.
	public void setReadCountUpdate(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		String sql = "update board "
				   + "set BOARD_READCOUNT=BOARD_READCOUNT+1 "
				   + "where BOARD_NUM = ?";
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("setReadCountUpdate() 에러 : "+ ex);
		}finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		}
		
	}

	//글 내용보기
	public BoardBean getDetail(int num) {
		BoardBean board = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement("select * from board where BOARD_NUM=?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();

			// DB에서 가져온 데이터를 BoardBean객체에 담습니다.
			if (rs.next()) {
				board = new BoardBean();
				board.setBOARD_NUM(rs.getInt("BOARD_NUM"));
				board.setBOARD_NAME(rs.getString("BOARD_NAME"));
				board.setBOARD_SUBJECT(rs.getString("BOARD_SUBJECT"));
				board.setBOARD_CONTENT(rs.getString("BOARD_CONTENT"));
				board.setBOARD_FILE(rs.getString("BOARD_FILE"));
				board.setBOARD_RE_REF(rs.getInt("BOARD_RE_REF"));
				board.setBOARD_RE_LEV(rs.getInt("BOARD_RE_LEV"));
				board.setBOARD_RE_SEQ(rs.getInt("BOARD_RE_SEQ"));
				board.setBOARD_READCOUNT(rs.getInt("BOARD_READCOUNT"));
				board.setBOARD_DATE(rs.getString("BOARD_DATE"));
			}

		} catch (Exception ex) {
			System.out.println("getDetail() 에러 : " + ex);

		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		} // finally

		return board;
		
	}

	public int boardReply(BoardBean board) {
		Connection con = null;
		PreparedStatement pstmt = null, pstmt2=null;
		ResultSet rs = null;
		// board 테이블의 board_num 필드의 최대값을 구해와서 글을 등록할 때
		// 글 번호를 순차적으로 지정하기 위함입니다.
		String board_max_sql = "select max(board_num) from board";
		String sql = "";
		int num = 0;
		/*
		 답변을 달 원문 글 그룹 번호입니다.
		 답변을 달게 되면 답변 글은 이 번호와 같은 관련글 번호를 갖게 처리되면서
		 같은 그룹에 속하게 됩니다
		 글 목록에서 보여줄 때 하나의 그룹으로 묶여서 출력됩니다.
		  */
		int re_ref = board.getBOARD_RE_REF();
		/*
		 답글의 깊이를 의미합니다.
		 원문에 대한 답글이 출력될 때 한 번 들여쓰기 처리가 되고
		 답글에 대한 답글은 들여쓰기가 두 번 처리되게 합니다.
		 원문인 경우에는 이 값이 0이고 원문의 답글은 1, 답글의 답글은 2가 됩니다.
		 */
		int re_lev = board.getBOARD_RE_LEV();
		
		//같은 관련 글 중에서 해당 글이 출력되는 순서입니다.
		int re_seq = board.getBOARD_RE_SEQ();
		
		try {
			con = ds.getConnection();
			
			//트랜잭션을 이용하기 위해서 setAutoCommit을 false로 설정합니다.
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(board_max_sql);
			rs = pstmt.executeQuery();
			if(rs.next())
				num = rs.getInt(1) + 1;
			pstmt.close();
			
			//BOARD_RE_REF, BOARD_RE_SEQ값을 확인하여 원문 글에
			//다른 답글이 있으면 다른 답글들의 BOARD_RE_SEQ값을 1씩 증가시킵니다.
			//현재 글을 다른 답글보다 앞에 출력되게 하기 위해서 입니다.
			
			sql = "update board "
				+ "set BOARD_RE_SEQ = BOARD_RE_SEQ + 1 "
				+ "where BOARD_RE_REF = ? "
				+ "and BOARD_RE_SEQ > ?";

			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			pstmt.executeUpdate();//삽입 성공시 result는 1
			
			//등록할 답변 글의 BOARD_RE_LEV, BOARD_RE_SEQ 값을 원문 글보다 1씩
			//증가시킵니다.
			re_seq = re_seq + 1;
			re_lev = re_lev + 1;
			
			//시퀀스와 레벨이 한 번 증가된 상태
			sql = "insert into board "
				+"(BOARD_NUM, BOARD_NAME, BOARD_PASS, BOARD_SUBJECT,"
				+" BOARD_CONTENT, BOARD_FILE, BOARD_RE_REF,"
				+" BOARD_RE_LEV, BOARD_RE_SEQ,"
				+" BOARD_READCOUNT,BOARD_DATE) "
				+" values(?,?,?,?,?,?,?,?,?,?,sysdate)";
			
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, num);
			pstmt2.setString(2, board.getBOARD_NAME());
			pstmt2.setString(3, board.getBOARD_PASS());
			pstmt2.setString(4, board.getBOARD_SUBJECT());
			pstmt2.setString(5, board.getBOARD_CONTENT());
			pstmt2.setString(6, ""); //답변에는 파일을 업로드하지 않습니다.
			pstmt2.setInt(7, re_ref);
			pstmt2.setInt(8, re_lev);
			pstmt2.setInt(9, re_seq);
			pstmt2.setInt(10, 0);//BOARD_READCOUNT(조회수)는 0
			pstmt2.executeUpdate();
			con.commit();//커밋합니다.
			
		}
		 catch (SQLException ex) {
			System.out.println("boardReply() 에러 : "+ ex);
			if(con != null) {
				try {
					con.rollback(); //rollback합니다.
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if(pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if(pstmt2 != null)
				try {
					pstmt2.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			if(con != null)
				try {
					con.setAutoCommit(true); //다시 true로 설정합니다.
					con.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
		}//finally
		return num;
	}

	public boolean isBoardWriter(int num, String pass) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean boardModify(BoardBean boarddata) {
		// TODO Auto-generated method stub
		return false;
	}



}
