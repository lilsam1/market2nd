<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ include file="../inc/dbconn.jsp" %>

<%
	request.setCharacterEncoding("UTF-8");

	String id = (String) session.getAttribute("sessionAdminId");
	String password = request.getParameter("password");
	String name = request.getParameter("name");
	
	// 관리자 정보 수정
	String sql = "UPDATE admin SET PASSWORD=?, NAME=? WHERE ID=?";
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, password);
	pstmt.setString(2, name);
	pstmt.setString(3, id);
	int result = pstmt.executeUpdate();
	// execute업데이트 실행 후 쿼리문으로 영향받은 열의 숫자 반환. 변경된 열이 없다면 결과 0 
	if (result == 1) {
		session.setAttribute("sessionAdminName", name);
		response.sendRedirect("index.jsp");
	}
	else {
		response.sendRedirect("updateAdmin.jsp");
	}
	
%>
