package market.dto;

public class Cart {
	private int cartId;	//
	private String memberId; 	// ȸ�� ���̵�
	private String orderNo;		// �ֹ� ��ȣ
	private String p_id; 		// ��ǰ���̵�
	private String p_name;		// ��ǰ�̸�
	private int p_unitPrice;	//����
	private int cnt;			// ����
	
	
	public Cart() {
		super();
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getP_id() {
		return p_id;
	}

	public void setP_id(String p_id) {
		this.p_id = p_id;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public int getP_unitPrice() {
		return p_unitPrice;
	}

	public void setP_unitPrice(int p_unitPrice) {
		this.p_unitPrice = p_unitPrice;
	}

	
}
