package org.kpfl.model.shop;

import org.kpfl.exception.InvalidValueException;

public class Merchandise extends Product {
	private String size;		//20 (10 * 2) bytes
	private String category;	//30 (15 * 2) bytes TOTAL: 50 BYTES (+72 super = 122 BYTES)

	public Merchandise() {
		super();
		this.size = String.format("%-10s", "Standard");
		this.category = String.format("%-15s", "Apparel");
	}

	public Merchandise(Merchandise m) {
		super(m);
		if (m != null) {
			this.size = String.format("%-10s", m.size);
			this.category = String.format("%-15s", m.category);
		}
	}

	public Merchandise(String productId, String name, double unitPrice, int stockQuantity,
					   String size, String category) throws InvalidValueException {
		super(productId, name, unitPrice, stockQuantity);
		this.size = String.format("%-10s", size != null ? size.trim() : "Standard");
		this.category = String.format("%-15s", category != null ? category.trim() : "Apparel");
	}

	public String getSize() {
		return size.trim();
	}

	public void setSize(String size) {
		this.size = String.format("%-10s", size != null ? size.trim() : "Standard");
	}

	public String getCategory() {
		return category.trim();
	}

	public void setCategory(String category) {
		this.category = String.format("%-15s", category != null ? category.trim() : "Apparel");
	}

	@Override
	public String getProductSummary() {
		return toString();
	}

	@Override
	public String toString() {
		return "Product ID: " + getProductId() + 
				"\nName: " + getName() + 
				"\nUnit Price: $" + getUnitPrice() + 
				"\nStock Quantity: " + getStockQuantity() + 
				"\nCategory: " + category.trim() + 
				"\nSize: " + size.trim() + 
				"\n";
	}
}
