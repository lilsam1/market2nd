package market.dao;

import java.util.ArrayList;

import market.dto.Product;

public class ProductRepository {
	private ArrayList<Product> listofProducts = new ArrayList<Product>();
	private static ProductRepository instance = new ProductRepository();	//
	
	public static ProductRepository getInstance() {	//
		return instance;
	}
	
	public ProductRepository() {
		Product phone = new Product("P1234", "iPhone 6s", 800000);
		phone.setDescription("4.7-inch, 1334X750 Renina HD display, 8-megapixel iSight Camera");
		phone.setCategory("smart Phone");
		phone.setManufacture("Apple");
		phone.setUnitsInStock(1000);
		phone.setCondition("New");
		phone.setFilename("P1234.png");
		
		Product notebook = new Product("P1235", "LG PC �׷�", 1500000);
		notebook.setDescription("13.3-inch, IPS LED display, 5rd Generation Intel core processors");
		notebook.setCategory("Notebook");
		notebook.setManufacture("LG");
		notebook.setUnitsInStock(1000);
		notebook.setCondition("Refurbished");
		notebook.setFilename("P1235.png");
		
		Product tablet = new Product("P1236", "Galaxy Tab 5", 900000);
		tablet.setDescription("212.8*125.6*6.6mm, Super AMOLED display, Octa-core processor");
		tablet.setCategory("Tablet");
		tablet.setManufacture("Samsung");
		tablet.setUnitsInStock(1000);
		tablet.setCondition("Old");
		tablet.setFilename("P1236.png");
		
		listofProducts.add(phone);
		listofProducts.add(notebook);
		listofProducts.add(tablet);
	}
	
	public ArrayList<Product> getAllProducts() {
		return listofProducts;
	}
	
	public Product getProductById(String productId) {
		Product productById = null;
		
		for (int i =  0; i < listofProducts.size(); i++) {
			Product product = listofProducts.get(i);
			if (product != null && product.getProductId() != null && product.getProductId().equals(productId)) {
				productById = product;
				break;
			}
		}
		return productById;
	}
	
	public void addProduct(Product product) {	//
		listofProducts.add(product);
	}
	
}
