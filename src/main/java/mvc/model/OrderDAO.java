package mvc.model;

import java.sql.Connection;
import java.sql.PreparedStatement;

import mvc.database.DBconnection;

public class OrderDAO {

	private static OrderDAO instance;
	
	private OrderDAO() {
		
	}
	
	public static OrderDAO getInstance() {
		if (instance == null)
			instance = new OrderDAO();
		return instance;
	}
	
	public void clearOrderData (String orderNo) {
		// 주문번호 기준으로 주문데이터 삭제. 중복 등록 방지용
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			String sql = "DELETE FROM order_data WHERE orderNo = ? ";
			
			conn  = DBconnection.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, orderNo);
			pstmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("ClearOrderData() error: " + ex);
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
	}
	
	public boolean insertOrderData(OrderDataDTO dto) {
		int flag = 0;
		String sql = "INSERT INTO order_data VALUES (null, ?, ?, ?, ?, ?, ?, ?)";
		
		try (Connection conn = DBconnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, dto.getOrderNo());
			pstmt.setInt(2, dto.getCartId());
			pstmt.setString(3, dto.getP_id());
			pstmt.setString(4, dto.getP_name());
			pstmt.setInt(5, dto.getUnitPrice());
			pstmt.setInt(6, dto.getCnt());
			pstmt.setInt(7, dto.getSumPrice());
			flag = pstmt.executeUpdate();
		} catch (Exception ex) {
			System.out.println("insertOrderData() error : " + ex);
		}
		return flag != 0;
	}

}
