package net.member.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {
	DataSource ds = null;

	// 생성자에서 JNDI 리소스를 참조하여 Connection 객체를 얻어옵니다.
	public MemberDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB 연결 실패 : " + e);
			return;
		}
	}

	public int insert(Member dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = -1;
		try {
			conn = ds.getConnection();
			System.out.println("getConnection : insert()");

			String checkid_sql = "insert into member values (?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(checkid_sql);
			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPassword());
			pstmt.setString(3, dto.getName());
			pstmt.setInt(4, dto.getAge());
			pstmt.setString(5, dto.getGender());
			pstmt.setString(6, dto.getEmail());
			// 상빕 성공시 result는 1
			result = pstmt.executeUpdate();
			// Primary key 제약 조건 위반할 경우 발생하는 에러
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			result = -1;
			System.out.println("멤버 아이디 중복 에러입니다.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}

	public int isId(String id, String pass) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = ds.getConnection();
			System.out.println("getConnection : isId(id, pass)");

			String checkid_sql = "select id, password from member where id = ?";
			pstmt = conn.prepareStatement(checkid_sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				if (rs.getString(2).equals(pass)) {
					result = 1; // 아이디와 비밀번호가 일치하는 경우
				} else {
					result = 0; // 비밀번호가 일치하지 않는 경우
				}
			} else {
				result = -1; // 아이디가 존재하지 않습니다.
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}

	public int isId(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = -1;
		try {
			conn = ds.getConnection();
			System.out.println("getConnection : isId(id)");

			String checkid_sql = "select id from member where LOWER(id) = ?";
			pstmt = conn.prepareStatement(checkid_sql);
			pstmt.setString(1, id.toLowerCase());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = 0; // DB에 해당 id가 있습니다.
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}

	public List<Member> getList(int page, int limit) {
		List<Member> list = new ArrayList<Member>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();

			// 쿼리는 확인 먼저 하고 여기에 넣자! ->하고도 꼭 확인하자!!
			String sql = "select * " 
					+ " from (select b.*, rownum rnum" 
					+ "		from(select * from member "
					+ "			where id != 'admin'" 
					+ "			order by id) b" + ")"
					+ " where rnum>=? and rnum<=?";

			pstmt = con.prepareStatement(sql);
			// 한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지, 4페이지 ...
			int startrow = (page - 1) * limit + 1;
			// 읽기 시작할 row 번호(1 11 21 31 ...
			int endrow = startrow + limit - 1;
			// 읽을 마지막 row 번호(10 20 30 40 ...
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();
			/*
			 * create table member( id varchar2(15), password varchar2(10), name
			 * varchar2(15), age Number, gender varchar2(5), email varchar2(30), PRIMARY
			 * KEY(id) );
			 */

			// DB에서 가져온 데이터를 VO 객체에 담습니다.
			while (rs.next()) {
				Member member = new Member();
				member.setId(rs.getString("id"));
				member.setPassword(rs.getString(2));
				member.setName(rs.getString(3));
				member.setAge(rs.getInt(4));
				member.setGender(rs.getString(5));
				member.setEmail(rs.getString(6));
				list.add(member);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return list; //반드시 리턴으로 김밥싼거 가져가야져~~
	}
	
	public List<Member> getList(String field, String value, int page, int limit) {
		List<Member> list = new ArrayList<Member>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = ds.getConnection();
			
			String sql = "select * " 
					+ " from (select b.*, rownum rnum" 
					+ "		from(select * from member "
					+ "			where id != 'admin' " 
					+ 			"and " + field +" like ? "
					+ "			order by id) b" 
					+ 		")"
					+ " where rnum between ? and ?";

			System.out.println(sql);
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "%"+value+"%"); //value를 포함하고 있는 값 전부불러!
			
			System.out.println("field = " + field);
			System.out.println("value = " + value);
			
			int startrow = (page - 1) * limit + 1;
			//읽기 시작할 row 번호(1 11 21 31 ...
			int endrow = startrow + limit - 1;
			//읽을 마지막 row 번호(10 20 30 40 ...
			pstmt.setInt(2, startrow);
			pstmt.setInt(3, endrow);
			rs = pstmt.executeQuery();
			/*
			 * create table member( 
			 * id varchar2(15), 
			 * password varchar2(10), 
			 * name varchar2(15),
			 *  age Number,
			 *  gender varchar2(5),
			 *  email varchar2(30), 
			 *  PRIMARY KEY(id) 
			 *  );
			 */

			// DB에서 가져온 데이터를 VO 객체에 담습니다.
			while (rs.next()) {
				Member member = new Member();
				member.setId(rs.getString("id"));
				member.setPassword(rs.getString(2));
				member.setName(rs.getString(3));
				member.setAge(rs.getInt(4));
				member.setGender(rs.getString(5));
				member.setEmail(rs.getString(6));
				list.add(member);
			}
		} catch (Exception e) {
			System.out.println("getListCount() 에러 : " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return list;
	}

	public int ListCount() {
		return 0;
	}


	public Member member_info(String a) {
		return null;
	}

	public void delete(String a) {

	}

	public int update(Member dto) {
		return 0;
	}

	public int getListCount() {
	      Connection con = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;

	      int x = 0;

	      try {

	         con = ds.getConnection();
	         pstmt = con.prepareStatement("select count(*) from member where id != 'admin'");
	         rs = pstmt.executeQuery();

	         if (rs.next()) {
	            x = rs.getInt(1);
	         }
	      } catch (Exception ex) {
	         ex.printStackTrace();
	         System.out.println("getListCount() 에러 : " + ex);
	      } finally {
	         if (rs != null)
	            try {
	               rs.close();
	            } catch (SQLException ex) {

	            }
	         if (pstmt != null) {

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
	      return x;

	   }
	
	public int getListCount(String field, String value) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		try {
			conn = ds.getConnection();
			String sql = "select count(*) from member "
					  + "where id != 'admin' "
					  + "and " + field +" like ?";
			System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+value+"%"); //value를 포함하고 있는 값 전부불러!
			rs = pstmt.executeQuery();

			if (rs.next()) {
				x = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("getListCount() 에러 : " + e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return x;
	}

	
}