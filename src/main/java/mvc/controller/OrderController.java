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
		
		if (command.contains("form.do")) { // �ֹ��� / ������� �Է� ������
			setOrderData(req);
			// ��ܿ� ��ٱ��� ���
			
			// ��ܿ� ����� ��ٱ��� 
			ArrayList<OrderDataDTO> datas = getOrderData(getOrderNo(req));
			req.setAttribute("datas", datas);
			
			// ��ٱ��� �հ� �ݾ�
			int totalPrice = getTotalPrice(getOrderNo(req));
			req.setAttribute("totalPrice", totalPrice);
			
			req.getRequestDispatcher("/WEB-INF/order/form.jsp").forward(req, resp);
		}
		
		else if (command.contains("pay.do")) {	// �ֹ��� ���� ���� �� ���� ���� ���
			setOrderInfo(req);	// �ֹ����� ����
			
			// ��ٱ��� �հ� �ݾ�
			int totalPrice = getTotalPrice(getOrderNo(req));
			req.setAttribute("totalPrice", totalPrice);
			
			// �ֹ��� ���� ������
			OrderInfoDTO info = getOrderInfo(getOrderNo(req));
			req.setAttribute("info", info);
			
			// �ֹ���ǰ ���� �������� (ex: iPhone 6S �� 1��)
			String orderProductName = getOrderProductName(getOrderNo(req));
			req.setAttribute("orderProductName", orderProductName);
			
			req.getRequestDispatcher("/WEB-INF/order/pay.jsp").forward(req, resp);
			
		}
	}

	private String getOrderNo(HttpServletRequest req) {
		// TODO Auto-generated method stub
		/* �ֹ� ��ȣ ��ȯ
		 1. �ֹ���ȣ ��� ������ �ڵ� �ݺ��� �Ǿ
		 2. �ֹ���ȣ ü�谡 ���� ��츦 ����� �޼���ȭ
		 */
		
		HttpSession session = req.getSession();	// ���� ����� ���� ����
		return session.getId();
	}
	
	private ArrayList<OrderDataDTO> getOrderData(String orderNo) {
		OrderDAO dao = OrderDAO.getInstance();
		ArrayList<OrderDataDTO> dtos = dao.selectAllOrderData(orderNo);
		return dtos;
	}

	private void setOrderData(HttpServletRequest req) {
		// TODO Auto-generated method stub
		// ��ٱ��Ͽ� �ִ� ��ǰ�� �ֹ������Ϳ� ����
		// ���� �ݾ��� ��ٱ��ϰ� �ƴ϶� �ֹ������� �������� ���
		
		OrderDAO dao = OrderDAO.getInstance();
		
		// �ֹ� ��ȣ ��������
		String orderNo = getOrderNo(req);
		
		// 1. �ߺ��� ���� ���� �ֹ���ȣ�� ����� ������ ����
		dao.clearOrderData(orderNo);
		
		// 2. �ֹ���ȣ �������� ��ٱ��Ͽ� �ִ� ��ǰ�� ������ ��
		CartDAO cartDAO = new CartDAO();
		ArrayList<Cart> carts = cartDAO.getCartList(orderNo);
		System.out.println(carts);
		
		// 3. CartList�� OrderData List�� ����
		ArrayList<OrderDataDTO> dtos = changeCartData(carts, orderNo);
		System.out.println(dtos);
		
		// 4. OrderData List�� ������ ���̽��� ����
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
		
		// 1. �ߺ��� ���� ���� �ֹ���ȣ�� ����� ������ ����
		dao.clearOrderInfo(getOrderNo(request));
		
		// 2. request�� ���� dto�� �����ؼ� dao�� ����
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
		// ���ǿ� ����� ���̵� ������
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
