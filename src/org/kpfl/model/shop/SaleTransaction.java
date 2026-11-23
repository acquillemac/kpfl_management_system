package org.kpfl.model.shop;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a completed sales transaction with financial receipts and GCT tracking.
 */
public class SaleTransaction {
    private String transactionId;
    private LocalDateTime timestamp;
    private List<OrderItem> items;
    private double subtotal;
    private double taxAmount;
    private double grandTotal;
    private double amountTendered;
    private double changeDue;

    public SaleTransaction(String transactionId, List<OrderItem> items,
                           double subtotal, double taxAmount, double grandTotal,
                           double amountTendered, double changeDue) {
        this.transactionId = transactionId;
        this.timestamp = LocalDateTime.now();
        this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.grandTotal = grandTotal;
        this.amountTendered = amountTendered;
        this.changeDue = changeDue;
    }

    public SaleTransaction(String transactionId, LocalDateTime timestamp, List<OrderItem> items,
                           double subtotal, double taxAmount, double grandTotal,
                           double amountTendered, double changeDue) {
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.taxAmount = taxAmount;
        this.grandTotal = grandTotal;
        this.amountTendered = amountTendered;
        this.changeDue = changeDue;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public double getAmountTendered() {
        return amountTendered;
    }

    public double getChangeDue() {
        return changeDue;
    }

    public String generateReceipt() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sb.append("=============================================================\n");
        sb.append("                 KPFL OFFICIAL STADIUM SHOP                  \n");
        sb.append("                      SALES RECEIPT                          \n");
        sb.append("=============================================================\n");
        sb.append(String.format("Receipt #:   %s\n", transactionId));
        sb.append(String.format("Date/Time:   %s\n", timestamp.format(dtf)));
        sb.append("-------------------------------------------------------------\n");
        sb.append(String.format("%-26s %5s %11s %11s\n", "Item", "Qty", "Price", "Total"));
        sb.append("-------------------------------------------------------------\n");
        for (OrderItem oi : items) {
            Product p = oi.getProduct();
            double lineSub = p.getUnitPrice() * oi.getQuantity();
            String name = (p.getName().length() > 25) ? p.getName().substring(0, 22) + "..." : p.getName();
            sb.append(String.format("%-26s %5d %11.2f %11.2f\n", name, oi.getQuantity(), p.getUnitPrice(), lineSub));
        }
        sb.append("-------------------------------------------------------------\n");
        sb.append(String.format("Subtotal:                                       $%10.2f\n", subtotal));
        sb.append(String.format("General Consumption Tax (15%% GCT):             $%10.2f\n", taxAmount));
        sb.append(String.format("GRAND TOTAL:                                    $%10.2f\n", grandTotal));
        sb.append(String.format("Amount Tendered (Cash):                         $%10.2f\n", amountTendered));
        sb.append(String.format("Change Given:                                   $%10.2f\n", changeDue));
        sb.append("=============================================================\n");
        sb.append("           Thank you for supporting the KPFL!               \n");
        sb.append("=============================================================\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("[%s] Total: $%.2f (GCT: $%.2f) Tendered: $%.2f Change: $%.2f",
                transactionId, grandTotal, taxAmount, amountTendered, changeDue);
    }
}
