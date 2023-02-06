package mvc.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import market.dao.CartDAO;
import market.dto.Cart;
import mvc.model.OrderDAO;
import mvc.model.OrderDataDTO;
import mvc.model.OrderInfoDTO;

@WebServlet("/order/*")
public class OrderController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String RequestURI = req.getRequestURI();	 
		String contextPath = req.getContextPath();	 
		String command = RequestURI.substring(contextPath.length());
		
		resp.setContentType("text/html; charset=utf-8");
		req.setCharacterEncoding("utf-8");
		
		System.out.println("command : " + command);
		
		if (command.contains("form.do")) { // 주문서 / 배송정보 입력 페이지
			setOrderData(req);
			// 상단에 장바구니 출력
			
			// 상단에 출력할 장바구니 
			ArrayList<OrderDataDTO> datas = getOrderData(getOrderNo(req));
			req.setAttribute("datas", datas);
			
			// 장바구니 합계 금액
			int totalPrice = getTotalPrice(getOrderNo(req));
			req.setAttribute("totalPrice", totalPrice);
			
			req.getRequestDispatcher("/WEB-INF/order/form.jsp").forward(req, resp);
		}
		
		else if (command.contains("pay.do")) {	// 주문서 정보 저장 및 결제 수단 출력
			setOrderInfo(req);	// 주문정보 저장
			
			// 장바구니 합계 금액
			int totalPrice = getTotalPrice(getOrderNo(req));
			req.setAttribute("totalPrice", totalPrice);
			
			// 주문서 정보 가져옴
			OrderInfoDTO info = getOrderInfo(getOrderNo(req));
			req.setAttribute("info", info);
			
			// 주문상품 정보 가져오기 (ex: iPhone 6S 외 1건)
			String orderProductName = getOrderProductName(getOrderNo(req));
			req.setAttribute("orderProductName", orderProductName);
			
			req.getRequestDispatcher("/WEB-INF/order/pay.jsp").forward(req, resp);
			
		}
	}

	private String getOrderNo(HttpServletRequest req) {
		// TODO Auto-generated method stub
		/* 주문 번호 반환
		 1. 주문번호 사용 때문에 코드 반복이 되어서
		 2. 주문번호 체계가 변할 경우를 대비해 메서드화
		 */
		
		HttpSession session = req.getSession();	// 세션 사용을 위해 생성
		return session.getId();
	}
	
	private ArrayList<OrderDataDTO> getOrderData(String orderNo) {
		OrderDAO dao = OrderDAO.getInstance();
		ArrayList<OrderDataDTO> dtos = dao.selectAllOrderData(orderNo);
		return dtos;
	}

	private void setOrderData(HttpServletRequest req) {
		// TODO Auto-generated method stub
		// 장바구니에 있는 상품을 주문데이터에 복사
		// 결제 금액을 장바구니가 아니라 주문데이터 기준으로 계산
		
		OrderDAO dao = OrderDAO.getInstance();
		
		// 주문 번호 가져오기
		String orderNo = getOrderNo(req);
		
		// 1. 중복을 막기 위해 주문번호로 저장된 데이터 삭제
		dao.clearOrderData(orderNo);
		
		// 2. 주문번호 기준으로 장바구니에 있는 상품을 가지고 옴
		CartDAO cartDAO = new CartDAO();
		ArrayList<Cart> carts = cartDAO.getCartList(orderNo);
		System.out.println(carts);
		
		// 3. CartList를 OrderData List로 변경
		ArrayList<OrderDataDTO> dtos = changeCartData(carts, orderNo);
		System.out.println(dtos);
		
		// 4. OrderData List를 데이터 베이스에 저장
		for(OrderDataDTO dto : dtos) {
			dao.insertOrderData(dto);
		}
	}

	private ArrayList<OrderDataDTO> changeCartData(ArrayList<Cart> carts, String orderNo) {
		// TODO Auto-generated method stub
		ArrayList<OrderDataDTO> datas = new ArrayList<>();
		for(Cart cart : carts) {
			OrderDataDTO dto = new OrderDataDTO();
			dto.setOrderNo(orderNo);
			dto.setCartId(cart.getCartId());
			dto.setP_id(cart.getP_id());
			dto.setP_name(cart.getP_name());
			dto.setUnitPrice(cart.getP_unitPrice());
			dto.setCnt(cart.getCnt());
			dto.setSumPrice(cart.getP_unitPrice() * cart.getCnt());
			datas.add(dto);
		}
		return datas;
	}
	
	private int getTotalPrice(String orderNo) {
		OrderDAO dao = OrderDAO.getInstance();
		return dao.getTotalPrice(orderNo);
	}
	
	private void setOrderInfo(HttpServletRequest request) {
		OrderDAO dao = OrderDAO.getInstance();
		
		// 1. 중복을 막기 위해 주문번호로 저장된 데이터 삭제
		dao.clearOrderInfo(getOrderNo(request));
		
		// 2. request온 값을 dto에 저장해서 dao에 전달
		OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
		
		orderInfoDTO.setOrderNo(getOrderNo(request));
		orderInfoDTO.setMemberId(getMemberId(request));
		orderInfoDTO.setOrderName(request.getParameter("orderName"));
		orderInfoDTO.setOrderTel(request.getParameter("orderTel"));
		orderInfoDTO.setOrderEmail(request.getParameter("orderEmail"));
		orderInfoDTO.setReceiveName(request.getParameter("receiveName"));
		orderInfoDTO.setReceiveTel(request.getParameter("receiveTel"));
		orderInfoDTO.setReceiveAddress(request.getParameter("receiveAddress"));
		orderInfoDTO.setPayAmount(getTotalPrice(getOrderNo(request)));
		
		dao.insertOrderInfo(orderInfoDTO);
	}
	
	private String getMemberId(HttpServletRequest request) {
		// 세션에 저장된 아이디 가져옴
		HttpSession session = request.getSession();
		return (String) session.getAttribute("sessionId");
	}
	
	private String getOrderProductName(String orderNo) {
		OrderDAO dao = OrderDAO.getInstance();
		return dao.getOrderProductName(orderNo);
	}
	
	private OrderInfoDTO  getOrderInfo(String orderNo) {
		OrderDAO dao = OrderDAO.getInstance();
		return dao.getOrderInfo(orderNo);
	}
}
