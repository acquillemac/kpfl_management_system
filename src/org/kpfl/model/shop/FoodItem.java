package org.kpfl.model.shop;

import org.kpfl.exception.InvalidValueException;

public class FoodItem extends Product {
	private String expirationDate;	//24 (12 * 2) bytes
	private boolean isVegetarian;	//1 byte TOTAL: 25 BYTES (+72 super = 97 BYTES)

	public FoodItem() {
		super();
		this.expirationDate = String.format("%-12s", "N/A");
		this.isVegetarian = false;
	}

	public FoodItem(FoodItem f) {
		super(f);
		if (f != null) {
			this.expirationDate = String.format("%-12s", f.expirationDate);
			this.isVegetarian = f.isVegetarian;
		}
	}

	public FoodItem(String productId, String name, double unitPrice, int stockQuantity,
					String expirationDate, boolean isVegetarian) throws InvalidValueException {
		super(productId, name, unitPrice, stockQuantity);
		this.expirationDate = String.format("%-12s", expirationDate != null ? expirationDate.trim() : "N/A");
		this.isVegetarian = isVegetarian;
	}

	public String getExpirationDate() {
		return expirationDate.trim();
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = String.format("%-12s", expirationDate != null ? expirationDate.trim() : "N/A");
	}

	public boolean isVegetarian() {
		return isVegetarian;
	}

	public void setVegetarian(boolean isVegetarian) {
		this.isVegetarian = isVegetarian;
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
				"\nExpiration Date: " + expirationDate.trim() + 
				"\nVegetarian: " + (isVegetarian ? "Yes" : "No") + 
				"\n";
	}
}
