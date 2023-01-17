package market.dto;

public class Product {
	
	private String productId;	// 占쏙옙품 占쏙옙占싱듸옙
	private String pname;		// 占쏙옙품占쏙옙
	private Integer unitPrice;	// 占쏙옙품 占쏙옙占쏙옙
	private String description;	// 占쏙옙품 占쏙옙占쏙옙
	private String manufacture;	// 占쏙옙占쏙옙占쏙옙
	private String category;	// 占싻뤄옙
	private long unitsInStock;	// 占쏙옙占쏙옙
	private String condition;	// 占신삼옙품 or 占쌩곤옙품 or 占쏙옙占실�
	private String filename;	// 占싱뱄옙占쏙옙 占쏙옙占싹몌옙
	private int quantity;		// 占쏙옙袂占쏙옙臼占� 占쏙옙占쏙옙 占쏙옙占쏙옙
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product() {
		super();
	}
	
	public Product(String productId, String pname, Integer unitPrice) {
		this.productId = productId;
		this.pname = pname;
		this.unitPrice = unitPrice;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public Integer getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getManufacture() {
		return manufacture;
	}

	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public long getUnitsInStock() {
		return unitsInStock;
	}

	public void setUnitsInStock(long unitsInStock) {
		this.unitsInStock = unitsInStock;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	/* 占쏙옙占쏙옙 확占싸울옙
	@Override
	public String toString() {
		return "Product [productId=" + productId + ", pname=" + pname + ", unitPrice=" + unitPrice + ", description="
				+ description + ", manufacture=" + manufacture + ", category=" + category + ", unitsInStock="
				+ unitsInStock + ", condition=" + condition + ", filename=" + filename + "]";
	}
	*/
	
	
	

}
