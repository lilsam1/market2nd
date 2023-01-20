<%@ page import="mvc.model.RippleDTO" %>
<%@ page import="mvc.model.RippleDAO" %>
<%@ page import="java.util.ArrayList" %>
<%@ include file="../inc/dbconn.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
{ "listData" : [
<% 
	request.setCharacterEncoding("UTF-8");

	String sessionId = (String) session.getAttribute("sessionId");
	String boardName = request.getParameter("boardName");
	int num = Integer.parseInt(request.getParameter("num"));
	
	RippleDAO dao = RippleDAO.getInstance();
	
	ArrayList<RippleDTO> list = dao.getRippleList(boardName, num);
	int i = 0;
	for (RippleDTO dto : list) {
		// 클라이언트가 해당 리플의 작성자인지 여부
		boolean flag = sessionId != null && sessionId.equals(dto.getMemberId()) ? true : false;
%>
{"rippleId" : "<%=dto.getRippleId()%>", "name" : "<%=dto.getName()%>", "content" : "<%=dto.getContent()%>", "isWriter": "<%=flag%>" }
<%
	// value가 배열 형태로 들어가서 마지막 요소의 경우에는 콤마가 나오면 안됨
	if (i++ < list.size() - 1)
		out.print(", ");
	}
%>
]}