package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import mvc.database.DBconnection;

public class RippleDAO {
	private static RippleDAO instance;
	
	private RippleDAO() {
		
	}
	
	public static RippleDAO getInstance() {
		if (instance == null)
			instance = new RippleDAO();
		return instance;
	}
	
	public boolean insertRipple(RippleDTO ripple) {
		int flag = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DBconnection.getConnection();
			
			String sql = "insert into ripple values(?, ?, ?, ?, ?, ?, now(), ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ripple.getRippleId());
			pstmt.setString(2, ripple.getBoardName());
			pstmt.setInt(3, ripple.getBoardNum());
			pstmt.setString(4, ripple.getMemberId());
			pstmt.setString(5, ripple.getName());
			pstmt.setString(6, ripple.getContent());
			pstmt.setString(7, ripple.getInsertDate());
			pstmt.setString(8, ripple.getIp());
			
			flag = pstmt.executeUpdate();
			
		} catch (Exception ex) {
			System.out.println("WriteRipple() 작성에러: " + ex);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
		return flag != 0;
	}
	
	public ArrayList<RippleDTO> getRippleList(String boardName, int boardNum) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM ripple WHERE boardName = ? AND boardNum = ?";
		ArrayList<RippleDTO> list = new ArrayList<>();
		
		try {
			conn = DBconnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boardName);
			pstmt.setInt(2, boardNum);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				RippleDTO ripple = new RippleDTO();
				ripple.setRippleId(rs.getInt("rippleId"));
				ripple.setBoardName(rs.getString("boardName"));
				ripple.setBoardNum(rs.getInt("boardNum"));
				ripple.setMemberId(rs.getString("memberId"));
				ripple.setName(rs.getString("name"));
				ripple.setContent(rs.getString("content"));
				ripple.setIp(rs.getString("ip"));
				list.add(ripple);
			}
		} catch (Exception ex) {
			System.out.println("getRippleList() 에러 : " + ex);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
		return list;
		
	}

	public boolean deleteRipple(RippleDTO ripple) {
		int flag = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "DELETE FROM ripple WHERE rippleId = ? ";
			
			conn  = DBconnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ripple.getRippleId());
			flag = pstmt.executeUpdate();
			
			System.out.println(sql);
			System.out.println(ripple.getRippleId());
		} catch (Exception ex) {
			System.out.println("deleteRipple() 에러 : " + ex);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				throw new RuntimeException(ex.getMessage());
			}
		}
		return flag != 0;
		
	}
}
