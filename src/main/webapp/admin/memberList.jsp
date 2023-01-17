<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../resources/css/bootstrap.min.css" >
<meta charset="UTF-8">
<title>회원 목록</title>
</head>
<body>
	<jsp:include page="../inc/menu.jsp" />
	<div class="jumbotron">
		<div class="container">
			<h1 class="display-3">회원 목록</h1>
		</div>
	</div>
	<div class="container">
		<div class="row" align="center">
		<table class="table table-hover table-striped text-center">
		<thead>
		<tr>
			<th>아이디</th>
			<th>이름</th>
			<th>메일</th>
			<th>전화번호</th>
			<th>주소</th>
			<th>가입일</th>
			<th>상세보기</th>
		</tr>
		</thead>
		<%@ include file="../inc/dbconn.jsp"%>
		<%
		// 1. 페이지당 게시물을 가져오기 위해 필요한 값 : 페이지당 게시물 수, 페이지 번호
		int cntListPerPage = 2;	// 페이지당 게시물 수 지정
		int pageNum = 1;	// 페이지 번호가 전달이 안되면 1페이지
		if (request.getParameter("pageNum") != null) {	// 페이지 번호가 전달이 된 경우
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		// 2. 페이지 번호당 게시물을 들고 오기 위해 필요한 값 : 시작 게시물 번호, 페이지당 게시물 수
		int startNum = (pageNum - 1) * cntListPerPage;	// 1페이지: 0, 2페이지: 10, 3페이지: 20
		String sql = "SELECT * FROM member LIMIT ?, ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, startNum);
		pstmt.setInt(2, cntListPerPage);
		rs = pstmt.executeQuery();
		while (rs.next()) {
		%>
		<tr>
			<td><%=rs.getString("id")%></td>
			<td><%=rs.getString("name")%></td>
			<td><%=rs.getString("mail")%></td>
			<td><%=rs.getString("phone")%></td>
			<td><%=rs.getString("address")%></td>
			<td><%=rs.getString("regist_day")%></td>
			<td><a href="./detailMember.jsp?id=<%=rs.getString("id")%>"
			class="btn btn-secondary" role="button"> 상세 정보 &raquo;</a></td>
		</tr>
		<%
		}
		%>
		</table>
		</div>
		<hr>
	
	
	<div class="col-sm-5">
		<%
		// 3. 페이징 출력을 위해 필요한 것 : 전체 페이지 수
		sql = "SELECT count(*) FROM member";
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		rs.next();
		int totalRecord = rs.getInt(1);	// 전체 게시물 수
		// 전체 페이지 수 1) 전체 게시물 수가 페이지당 게시물 수의 배수일 경우 전체 게시물 수 / 페이지당 게시물 수
		// 2) 전체 게시물 수가 페이지당 게시물 수의 배수가 아닐 경우 (전체 게시물 수 / 페이지당 게시물 수) + 1
		int totalPage = (totalRecord % cntListPerPage == 0) ? totalRecord / cntListPerPage : (totalRecord / cntListPerPage) + 1;
		// out.print(totalPage);
		
		// 4. 페이징 처리
		int block = 2;	// 페이지 나올 범위
		int blockTotal = totalPage % block == 0 ? totalPage / block : totalPage / block + 1;	// 총 블럭의 수
		int blockThis = ((pageNum - 1) / block) + 1;	// 현재 페이지의 블럭
		int blockThisFirstPage = ((blockThis - 1) * block) + 1;	// 현재 블럭의 첫 페이지
		int blockThisLastPage = blockThis * block;	// 현재 블럭의 마지막 페이지
		blockThisLastPage = (blockThisLastPage > totalPage)? totalPage : blockThisLastPage;	// 마지막 블럭의 경우 전체 페이지 번호가 블럭의 마지막 페이지
		
		out.println("totalPage :" + totalPage + "<br>");
		out.println("totalBlock :" + blockTotal + "<br>");
		out.println("thisBlock :" + blockThis + "<br>");
		out.println("fistPage :" + blockThisFirstPage + "<br>");
		out.println("lastPage :" + blockThisLastPage + "<br>");
		
		// 4. 페이징 출력
		
		%>
		<a href="memberList.jsp?pageNum=1">첫 페이지</a>
		<% 
		if (blockThis > 1) { // 2번 블럭부터 이전 페이지 출력 : 이번 블럭의 첫 페이지 -1
		%>
		<a href="memberList.jsp?pageNum=<%=(blockThisFirstPage - 1)%>">이전</a>
		<%
		}
		%>
		<%
		// 현재 블럭의 첫 페이지부터 현재 블럭의 마지막 페이지까지 반복
		for (int i = blockThisFirstPage; i <= blockThisLastPage; i++) {
		%>
		<a href="memberList.jsp?pageNum=<%=i%>"><%=i%></a>
		<%
		}
		%>
		<%
		if (blockThis < blockTotal) {	// 현재 블럭이 마지막 블럭이 아니면
		%>
		<a href="memberList.jsp?pageNum=<%=(blockThisLastPage + 1)%>">다음</a>
		<%
		}
		%>
		<a href="memberList.jsp?pageNum=<%=totalPage%>">마지막 페이지</a>
		<%	 
		if (rs != null)
			rs.close();
		if (pstmt != null)
			pstmt.close();
		if (conn != null)
			conn.close();
		%>
		</div>
	</div>	
	<jsp:include page="../inc/footer.jsp" />
</body>
</html>