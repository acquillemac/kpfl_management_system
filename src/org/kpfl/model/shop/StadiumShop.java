package org.kpfl.model.shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kpfl.exception.InsufficientFundsException;
import org.kpfl.exception.InvalidValueException;

/**
 * Manages stadium-shop inventory, sales transactions, tax computation, and receipts.
 */
public class StadiumShop {
    private String shopId;
    private String stadiumName;
    private List<Product> inventory;
    private List<SaleTransaction> transactionHistory;
    private int transactionCounter;

    public StadiumShop(String shopId, String stadiumName) {
        this.shopId = (shopId != null) ? shopId : "SHOP-01";
        this.stadiumName = (stadiumName != null) ? stadiumName : "Venue Concessions";
        this.inventory = new ArrayList<>();
        this.transactionHistory = new ArrayList<>();
        this.transactionCounter = 1000;
    }

    public String getShopId() {
        return shopId;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void addProduct(Product product) throws InvalidValueException {
        if (product == null) {
            throw new InvalidValueException("Cannot add null product.");
        }
        for (Product p : inventory) {
            if (p.getProductId().equalsIgnoreCase(product.getProductId())) {
                throw new InvalidValueException("Product with ID " + product.getProductId() + " already exists in inventory.");
            }
        }
        inventory.add(product);
    }

    public Product findProductById(String productId) {
        if (productId == null) return null;
        for (Product p : inventory) {
            if (p.getProductId().equalsIgnoreCase(productId.trim())) {
                return p;
            }
        }
        return null;
    }

    public List<Product> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public List<SaleTransaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    /**
     * Completes a purchase transaction:
     * - Validates stock availability.
     * - Computes Subtotal and 15% GCT.
     * - Enforces BR-07: amountTendered >= grandTotal.
     * - Deducts stock and logs transaction.
     */
    public SaleTransaction processSale(List<OrderItem> cart, double amountTendered)
            throws InvalidValueException, InsufficientFundsException {
        if (cart == null || cart.isEmpty()) {
            throw new InvalidValueException("Cannot process an empty cart.");
        }

        // 1. Verify stock availability for all items
        for (OrderItem item : cart) {
            Product p = item.getProduct();
            if (p.getStockQuantity() < item.getQuantity()) {
                throw new InvalidValueException(
                    String.format("Insufficient stock for %s. Available: %d, Requested: %d",
                        p.getName(), p.getStockQuantity(), item.getQuantity())
                );
            }
        }

        // 2. Compute financial totals
        double subtotal = 0.0;
        for (OrderItem item : cart) {
            subtotal += item.getSubtotal();
        }
        double gct = subtotal * Product.GCT_RATE;
        double grandTotal = subtotal + gct;

        // 3. Enforce BR-07: Insufficient payment check
        if (amountTendered < grandTotal) {
            throw new InsufficientFundsException(
                String.format("Insufficient payment. Grand Total: $%.2f, Amount Tendered: $%.2f. Short by $%.2f",
                    grandTotal, amountTendered, (grandTotal - amountTendered))
            );
        }

        // 4. Deduct stock
        for (OrderItem item : cart) {
            item.getProduct().reduceStock(item.getQuantity());
        }

        // 5. Generate receipt and log transaction
        double change = amountTendered - grandTotal;
        String txId = "TX-" + (++transactionCounter);
        SaleTransaction tx = new SaleTransaction(txId, cart, subtotal, gct, grandTotal, amountTendered, change);
        transactionHistory.add(tx);

        return tx;
    }

    public double getTotalGrossRevenue() {
        double sum = 0.0;
        for (SaleTransaction tx : transactionHistory) {
            sum += tx.getGrandTotal();
        }
        return sum;
    }

    public double getTotalGCTCollected() {
        double sum = 0.0;
        for (SaleTransaction tx : transactionHistory) {
            sum += tx.getTaxAmount();
        }
        return sum;
    }
}
