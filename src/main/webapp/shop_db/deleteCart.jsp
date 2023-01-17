<%@ page import="market.dao.CartDAO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String orderNo = session.getId();
	CartDAO cartDAO = new CartDAO();
	cartDAO.deleteCart(orderNo);
	
	response.sendRedirect("cart.jsp");
%>