<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="mvc.model.BoardDTO"%> 
<%
	BoardDTO notice = (BoardDTO) request.getAttribute("board");
	int num = ((Integer) request.getAttribute("num")).intValue();
	int pageNum = ((Integer) request.getAttribute("page")).intValue();
%>
<html>
<head>
<link rel="stylesheet" href="../resources/css/bootstrap.min.css" />
<title>Board</title>
</head>
<body>
	<jsp:include page="../inc/menu.jsp" />
	<div class="jumbotron">
		<div class="container">
			<h1 class="display-3">게시판</h1>
		</div>
	</div>
	
	<div class="container">
			<div class="form-group row">
				<label class="col-sm-2 control-label">성명</label>
				<div class="col-sm-3">
					<input name="name" type="text" class="form-control" value="<%=notice.getName()%>">
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-2 control-label">제목</label>
				<div class="col-sm-5">
					<input name="subject" type="text" class="form-control" value="<%=notice.getSubject()%>">
				</div>
			</div>
			<div class="form-group row">
				<label class="col-sm-2 control-label">내용</label>
				<div class="col-sm-8" style="word-break: break-all;">
					<textarea name="content" cols="50" rows="5" class="form-control"
					><%=notice.getContent()%></textarea>
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-offset-2 col-sm-10">
					<c:set var="userId" value="<%=notice.getId()%>" />
					<c:if test="${sessionId==userId}">
						<p>
							<span class="btn btn-danger" onclick="goDelete();">삭제</span>
							<span class="btn btn-success" onclick="goUpdate();">수정</span>
					</c:if>
					<a href="./BoardListAction.do?pageNum=<%=pageNum%>" class="btn btn-primary">목록</a>										
				</div>
			</div>
		<form name="frmUpdate" method="post">
			<input type="hidden" name="num" value="<%=num%>">
			<input type="hidden" name="pageNum" value="<%=pageNum%>">
		</form>
		<script type="text/javascript">
			let goUpdate = function () {
				const frm = document.frmUpdate;
				frm.action = "./BoardUpdateForm.do";
				frm.submit();
			}
			let goDelete = function () {
				if (confirm("삭제하시겠습니까")) {
					const frm = document.frmUpdate;
					frm.action = "./BoardDeleteAction.do";
					frm.submit();
				}
			}
		</script>
		<hr>
	</div>
	<jsp:include page="../inc/footer.jsp" />
</body>
</html>





