package market.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import market.dto.Product;
import mvc.database.DBconnection;

public class ProductDAO {
	
	private Connection connection = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	// DB占쏙옙占쏙옙 占쌨쇽옙占쏙옙
	void connect() {
		try {
			connection = DBconnection.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public ProductDAO() {
		connect();
	}
	
	public Product getProductById(String id) {
		// 占쏙옙占쌨듸옙 id占쏙옙 占쌔댐옙占싹댐옙 占쏙옙품占쏙옙 占쏙옙澍【占� 占쏙옙회占쌔쇽옙
		// 1) 占쌍댐옙 占쏙옙占� 占쏙옙체占쏙옙 占쏙옙티占� 占쏙옙占쏙옙
		// 2) 占쏙옙占쏙옙 占쏙옙占� null 占쏙옙占쏙옙
		Product product = null;
		String sql = "select * from product where p_id = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				product = new Product(resultSet.getString("p_id"),
						resultSet.getString("p_name"), resultSet.getInt("p_unitPrice"));
				product.setDescription(resultSet.getString("p_description"));
				product.setCategory(resultSet.getString("p_category"));
				product.setManufacture(resultSet.getString("p_manufacture"));
				product.setUnitsInStock(resultSet.getInt("p_unitsInStock"));
				product.setCondition(resultSet.getString("p_condition"));
				product.setFilename(resultSet.getString("p_filename"));
			}
		
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return product;
	}
}
