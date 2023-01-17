<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="market.dao.CartDAO" %>
<%@ include file="../inc/dbconn.jsp" %>

<%
	request.setCharacterEncoding("UTF-8");

	String id = request.getParameter("id");
	String password = request.getParameter("password");
	String sql = "SELECT * FROM member WHERE id= ? AND password = ?";
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1,id);
	pstmt.setString(2, password);
	rs = pstmt.executeQuery();
	if (rs.next()) {
		session.setAttribute("sessionId", id);
		session.setAttribute("sesionName", rs.getString("name"));
		
		CartDAO cartDAO = new CartDAO();
		cartDAO.updateCartBylogin(session);
		response.sendRedirect("resultMember.jsp?msg=2");
	}
	else {
		response.sendRedirect("loginMember.jsp?error=1");
	}
%>
