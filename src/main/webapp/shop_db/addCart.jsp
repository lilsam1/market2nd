<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="market.dto.Product" %>
<%@ page import="market.dao.ProductDAO" %>
<%@ page import="market.dao.CartDAO" %>

<%
	// 아이디 값 파라미터
	String productId = request.getParameter("id");
	if (productId == null || productId.trim().equals("")) {
		response.sendRedirect("Products.jsp");
		return;
	}
	
	ProductDAO productDAO = new ProductDAO();
	
	Product product = productDAO.getProductById(productId);
	if (product == null) {
		response.sendRedirect("exceptionNoProductId.jsp");
	}
	
	String orderNo = session.getId();
	String memberId = (String) session.getAttribute("sessionId");
	
	CartDAO cartDAO = new CartDAO();
	
	boolean flag = cartDAO.updateCart(product, orderNo, memberId);
	
	response.sendRedirect("product.jsp?id=" + productId);
%>