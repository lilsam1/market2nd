<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<html>
<head>
<link rel="stylesheet" href="../resources/css/bootstrap.min.css" />
<%
	String sessionAdminId = (String ) session.getAttribute("sessionAdminId");
%>
<sql:setDataSource var="dataSource" 
	url = "jdbc:mariadb://localhost:3308/market"
	driver = "org.mariadb.jdbc.Driver" user="root" password="1475" />

<sql:query dataSource="${dataSource}" var="resultSet">
	SELECT * FROM ADMIN WHERE ID=?
	<sql:param value="<%=sessionAdminId%>" />
</sql:query>		
<title>관리자 수정</title>
</head>
<body>
	<jsp:include page="./menu.jsp" />
	<div class="jumbotron">
		<div class="container">
			<h1 class="display-3">관리자 수정</h1>
		</div>
	</div>
	<c:forEach var="row" items="${resultSet.rows}">
	<div class="container">
		<form name="newAdmin" class="form-horizontal"  action="processUpdateAdmin.jsp" method="post" onsubmit="return checkForm()">
			<div class="form-group  row">
				<label class="col-sm-2 ">아이디</label>
				<div class="col-sm-3">
					<input name="id" type="text" class="form-control" placeholder="id" value="<c:out value='${row.id}'/>" readonly >
				</div>
			</div>
			<div class="form-group  row">
				<label class="col-sm-2">비밀번호</label>
				<div class="col-sm-3">
					<input name="password" type="text" class="form-control" placeholder="password" value="<c:out value='${row.password}'/>">
				</div>
			</div>
			<div class="form-group  row">
				<label class="col-sm-2">비밀번호확인</label>
				<div class="col-sm-3">
					<input name="password_confirm" type="text" class="form-control" placeholder="password confirm" >
				</div>
			</div>
			<div class="form-group  row">
				<label class="col-sm-2">성명</label>
				<div class="col-sm-3">
					<input name="name" type="text" class="form-control" placeholder="name" value="<c:out value='${row.name}'/>">
				</div>
			</div>
			
			<div class="form-group  row">
				<div class="col-sm-offset-2 col-sm-10 ">
					<input type="submit" class="btn btn-primary " value="관리자수정 " > 
				</div>
			</div>
		</form>
	</div>
	</c:forEach>
	<jsp:include page="../inc/footer.jsp" />
</body>
</html>
<script type="text/javascript">
	function checkForm() {
		if (!document.newAdmin.password.value) {
			alert("비밀번호를 입력하세요.");
			return false;
		}
		if (document.newAdmin.password.value != document.newAdmin.password_confirm.value) {
			alert("비밀번호를 동일하게 입력하세요.");
			return false;
		}
	}
</script>