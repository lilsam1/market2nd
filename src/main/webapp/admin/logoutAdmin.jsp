<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	session.removeAttribute("sessionAdminId");
	session.removeAttribute("sessionAdminName");
	session.removeAttribute("sessionAdminDay");

	response.sendRedirect("index.jsp");
%>