<%@ page import="mvc.model.RippleDTO" %>
<%@ page import="mvc.model.RippleDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ include file="../inc/dbconn.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");
	int rippleId = Integer.parseInt(request.getParameter("rippleId"));
	
	RippleDAO dao = RippleDAO.getInstance();
	RippleDTO ripple = new RippleDTO();
	ripple.setRippleId(rippleId);
	if (dao.deleteRipple(ripple)) {
%>
	{"result":"true"}
<%
	} else {
%>
	{"result":"false"}
<%
	}
%>