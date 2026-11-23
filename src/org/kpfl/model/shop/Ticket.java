package org.kpfl.model.shop;

import org.kpfl.exception.InvalidValueException;

public class Ticket {
	public static final double GCT_RATE = 0.15;

	private String ticketId;	//20 (10 * 2) bytes
	private String fixtureId;	//20 (10 * 2) bytes
	private String category;	//30 (15 * 2) bytes
	private double basePrice;	//8 bytes TOTAL: 78 BYTES

	public Ticket() {
		this.ticketId = String.format("%-10s", "");
		this.fixtureId = String.format("%-10s", "");
		this.category = String.format("%-15s", "Bleachers");
		this.basePrice = 0.0;
	}

	public Ticket(Ticket t) {
		if (t != null) {
			this.ticketId = String.format("%-10s", t.ticketId);
			this.fixtureId = String.format("%-10s", t.fixtureId);
			this.category = String.format("%-15s", t.category);
			this.basePrice = t.basePrice;
		}
	}

	public Ticket(String ticketId, String fixtureId, String category, double basePrice)
			throws InvalidValueException {
		if (ticketId == null || ticketId.trim().isEmpty()) {
			throw new InvalidValueException("Ticket ID cannot be empty.");
		}
		if (fixtureId == null || fixtureId.trim().isEmpty()) {
			throw new InvalidValueException("Fixture ID cannot be empty.");
		}
		if (basePrice < 0.0) {
			throw new InvalidValueException("Ticket base price cannot be negative.");
		}
		this.ticketId = String.format("%-10s", ticketId.trim());
		this.fixtureId = String.format("%-10s", fixtureId.trim());
		this.category = String.format("%-15s", category != null ? category.trim() : "Bleachers");
		this.basePrice = basePrice;
	}

	public String getTicketId() {
		return ticketId.trim();
	}

	public void setTicketId(String ticketId) {
		this.ticketId = String.format("%-10s", ticketId != null ? ticketId.trim() : "");
	}

	public String getFixtureId() {
		return fixtureId.trim();
	}

	public void setFixtureId(String fixtureId) {
		this.fixtureId = String.format("%-10s", fixtureId != null ? fixtureId.trim() : "");
	}

	public String getCategory() {
		return category.trim();
	}

	public void setCategory(String category) {
		this.category = String.format("%-15s", category != null ? category.trim() : "Bleachers");
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public double getGCT() {
		return basePrice * GCT_RATE;
	}

	public double getTotalPrice() {
		return basePrice * (1.0 + GCT_RATE);
	}

	public void display() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		return "Ticket ID: " + ticketId.trim() + 
				"\nFixture ID: " + fixtureId.trim() + 
				"\nCategory: " + category.trim() + 
				"\nBase Price: $" + basePrice + 
				"\nGCT (15%): $" + getGCT() + 
				"\nTotal Price: $" + getTotalPrice() + 
				"\n";
	}
}
