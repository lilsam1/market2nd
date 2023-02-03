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
		
		if (command.contains("form.jsp")) { // �ֹ��� / ������� �Է� ������
			setOrderData(req);
			// ��ܿ� ��ٱ��� ���
//			ArrayList<OrderDataDTO> datas = getOrderData(getOrderNo(req));
//			req.setAttribute("datas", datas);
//			req.getRequestDispatcher("/WEB-INF/order/form.jsp").forward(req, resp);
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
	
}
