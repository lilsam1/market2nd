<%@page import="market.dto.Cart"%>
<%@page import="market.dao.CartDAO"%>
<%@page import="market.dto.Product"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%
	String cartId = session.getId();
%>
<link rel="stylesheet" href="../resources/css/bootstrap.min.css" >
<meta charset="UTF-8">
<title>장바구니</title>
</head>
<body>
	<jsp:include page="../inc/menu.jsp" />
	<div class="jumbotron">
		<div class="container">
			<h1 class="display-3">장바구니</h1>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<table width="100%">
				<tr>
					<td alingn="left">
					<span class="btn btn-danger" onclick="deleteCart();">전체 삭제하기</span>
					<span class="btn btn-danger" onclick="deleteCartSel();">선택 삭제하기</span></td>
					<td align="right"><a href="./shippingInfo.jsp?cartId=<%= cartId%>" class="btn btn-success">주문하기</a></td>
				</tr>
			</table>
		</div>
		<div style="padding-top: 50px">
		<script src="../resources/js/check_system.js"></script>
		<form name="frmCart" method="get">
			<input type="hidden" name="id">
			<input type="text" name="chkdID">
			<table class="table table-hover" >
				<tr>
					<th><input name="chkAll" type="checkbox" onClick="setChkAll();">상품</th>
					<th>가격</th>
					<th>수량</th>
					<th>소계</th>
					<th>비고</th>
				</tr>
				<%
					int sum = 0;
					CartDAO cartDAO = new CartDAO();
					String orderNo = session.getId();
					//ArrayList<Cart> cartArrayList = cartDAO.getCartList(orderNo);
					
					ArrayList<Cart> cartArrayList = (ArrayList<Cart>) request.getAttribute("carts");
					for (Cart cart : cartArrayList) {
						int total = cart.getP_unitPrice() * cart.getCnt();
						sum += total;
				%>
				<tr>
					<td>
					<input type="checkbox" name="chkID" value="<%=cart.getCartId()%>" onClick="setChkAlone(this);">
					<%=cart.getP_id()%> - <%=cart.getP_name() %></td>
					<td><%=cart.getP_unitPrice()%></td>
					<td><%=cart.getCnt()%></td>
					<td><%=total %></td>
					<td><span class="badge badge-danger" onClick="removeCartByID('<%=cart.getCartId()%>')">삭제</span></td>
				</tr>
				<%
					}
				%>
				<tr>
					<th></th>
					<th></th>
					<th>총액</th>
					<th><%=sum%></th>
					<th></th>
				</tr>
			</table>
		</form>
		<script>
			window.onload=function() {
				document.frmCart.chkAll.checked = true;	// 전체 선택 체크 박스 체크
				setChkAll();	// 목록의 체크박스 체크
			}
			function frmName() {
				return document.frmCart;
			}
		</script>
		<script>
			const frm = document.frmCart;
			let removeCartByID = function(ID) {
				if(confirm('해당 상품을 삭제하시겠습니까?')) {
					//frm.id.value = ID;
					//frm.action = "removeCart.jsp";
					//frm.submit();
					location.href = 'removeCart.jsp?id=' + ID;
				}
			}
			let deleteCartSel = function () {
				if(confirm('선택한 상품을 삭제하시겠습니까?')) {
					frm.action = "removeCartSel.jsp"
					frm.submit();
				}
			}
			let deleteCart = function() {
				if(confirm('전체 삭제하시겠습니까?')) {
					//frm.action = "deleteCart.jsp";
					//frm.submit();
					location.href = 'deleteCart.jsp'
				}
			}
		</script>
			<a href="./products.jsp" class="btn btn-secondary"> &laquo; 쇼핑 계속하기</a>
		</div>
		<hr>
	</div>
	<jsp:include page="../inc/footer.jsp" />
</body>
</html>