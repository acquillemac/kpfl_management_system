package org.kpfl.model.shop;

import org.kpfl.exception.InvalidValueException;

public abstract class Product {
	public static final double GCT_RATE = 0.15; // 15% General Consumption Tax

	protected String productId;		//20 (10 * 2) bytes
	protected String name;			//40 (20 * 2) bytes
	protected double unitPrice;		//8 bytes
	protected int stockQuantity;	//4 bytes TOTAL: 72 BYTES

	public Product() {
		this.productId = String.format("%-10s", "");
		this.name = String.format("%-20s", "");
		this.unitPrice = 0.0;
		this.stockQuantity = 0;
	}

	public Product(Product p) {
		if (p != null) {
			this.productId = String.format("%-10s", p.productId);
			this.name = String.format("%-20s", p.name);
			this.unitPrice = p.unitPrice;
			this.stockQuantity = p.stockQuantity;
		}
	}

	public Product(String productId, String name, double unitPrice, int stockQuantity)
			throws InvalidValueException {
		if (productId == null || productId.trim().isEmpty()) {
			throw new InvalidValueException("Product ID cannot be empty.");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new InvalidValueException("Product name cannot be empty.");
		}
		if (unitPrice < 0.0) {
			throw new InvalidValueException("Unit price cannot be negative.");
		}
		if (stockQuantity < 0) {
			throw new InvalidValueException("Stock quantity cannot be negative.");
		}
		this.productId = String.format("%-10s", productId.trim());
		this.name = String.format("%-20s", name.trim());
		this.unitPrice = unitPrice;
		this.stockQuantity = stockQuantity;
	}

	public String getProductId() {
		return productId.trim();
	}

	public void setProductId(String productId) {
		this.productId = String.format("%-10s", productId != null ? productId.trim() : "");
	}

	public String getName() {
		return name.trim();
	}

	public void setName(String name) {
		this.name = String.format("%-20s", name != null ? name.trim() : "");
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public void addStock(int quantity) throws InvalidValueException {
		if (quantity < 0) {
			throw new InvalidValueException("Quantity to add cannot be negative.");
		}
		this.stockQuantity += quantity;
	}

	public void reduceStock(int quantity) throws InvalidValueException {
		if (quantity < 0) {
			throw new InvalidValueException("Quantity to reduce cannot be negative.");
		}
		if (quantity > this.stockQuantity) {
			throw new InvalidValueException("Insufficient stock available.");
		}
		this.stockQuantity -= quantity;
	}

	public double calculateSubtotal(int quantity) throws InvalidValueException {
		if (quantity <= 0) {
			throw new InvalidValueException("Quantity must be positive.");
		}
		return this.unitPrice * quantity;
	}

	public double calculateGCT(int quantity) throws InvalidValueException {
		return calculateSubtotal(quantity) * GCT_RATE;
	}

	public double calculateTotalWithTax(int quantity) throws InvalidValueException {
		return calculateSubtotal(quantity) * (1.0 + GCT_RATE);
	}

	public abstract String getProductSummary();

	public void display() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		return "Product ID: " + productId.trim() + 
				"\nName: " + name.trim() + 
				"\nUnit Price: $" + unitPrice + 
				"\nStock Quantity: " + stockQuantity + 
				"\n";
	}
}
