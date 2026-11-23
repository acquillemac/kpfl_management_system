package org.kpfl.model.shop;

import org.kpfl.exception.InvalidValueException;

/**
 * Represents an item and quantity within a shop order.
 */
public class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) throws InvalidValueException {
        if (product == null) {
            throw new InvalidValueException("Product cannot be null.");
        }
        if (quantity <= 0) {
            throw new InvalidValueException("Order quantity must be positive.");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() throws InvalidValueException {
        return product.calculateSubtotal(quantity);
    }

    public double getGCT() throws InvalidValueException {
        return product.calculateGCT(quantity);
    }

    public double getTotalWithTax() throws InvalidValueException {
        return product.calculateTotalWithTax(quantity);
    }

    @Override
    public String toString() {
        try {
            return String.format("%s x %d @ $%.2f = $%.2f (+15%% GCT: $%.2f | Total: $%.2f)",
                    product.getName(), quantity, product.getUnitPrice(), getSubtotal(), getGCT(), getTotalWithTax());
        } catch (InvalidValueException e) {
            return product.getName() + " x " + quantity;
        }
    }
}
