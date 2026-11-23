package org.kpfl.model.league;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kpfl.exception.CapacityExceededException;
import org.kpfl.exception.InvalidValueException;
import org.kpfl.model.shop.StadiumShop;

public class Stadium {
	private String stadiumId;		//20 (10 * 2) bytes
	private String name;			//50 (25 * 2) bytes
	private String location;		//40 (20 * 2) bytes
	private int seatingCapacity;	//4 bytes
	private int ticketsSold;		//4 bytes
	private String fieldCondition;	//30 (15 * 2) bytes
	private double baseTicketPrice;	//8 bytes TOTAL: 156 BYTES

	private StadiumShop stadiumShop;
	private List<String> practiceSessions;

	public Stadium() {
		this.stadiumId = String.format("%-10s", "");
		this.name = String.format("%-25s", "");
		this.location = String.format("%-20s", "");
		this.seatingCapacity = 0;
		this.ticketsSold = 0;
		this.fieldCondition = String.format("%-15s", "GOOD");
		this.baseTicketPrice = 0.0;
		this.stadiumShop = new StadiumShop("SHOP-DEFAULT", "Venue Concessions");
		this.practiceSessions = new ArrayList<>();
	}

	public Stadium(Stadium s) {
		if (s != null) {
			this.stadiumId = String.format("%-10s", s.stadiumId);
			this.name = String.format("%-25s", s.name);
			this.location = String.format("%-20s", s.location);
			this.seatingCapacity = s.seatingCapacity;
			this.ticketsSold = s.ticketsSold;
			this.fieldCondition = String.format("%-15s", s.fieldCondition);
			this.baseTicketPrice = s.baseTicketPrice;
			this.stadiumShop = s.stadiumShop;
			this.practiceSessions = new ArrayList<>(s.practiceSessions);
		}
	}

	public Stadium(String stadiumId, String name, String location, int seatingCapacity,
				   String fieldCondition, double baseTicketPrice) throws InvalidValueException {
		if (stadiumId == null || stadiumId.trim().isEmpty()) {
			throw new InvalidValueException("Stadium ID cannot be empty.");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new InvalidValueException("Stadium name cannot be empty.");
		}
		if (seatingCapacity <= 0) {
			throw new InvalidValueException("Seating capacity must be a positive number.");
		}
		if (baseTicketPrice < 0.0) {
			throw new InvalidValueException("Base ticket price cannot be negative.");
		}

		this.stadiumId = String.format("%-10s", stadiumId.trim());
		this.name = String.format("%-25s", name.trim());
		this.location = String.format("%-20s", location != null ? location.trim() : "Kingston");
		this.seatingCapacity = seatingCapacity;
		this.ticketsSold = 0;
		this.fieldCondition = String.format("%-15s", fieldCondition != null ? fieldCondition.trim() : "GOOD");
		this.baseTicketPrice = baseTicketPrice;
		this.stadiumShop = new StadiumShop("SHOP-" + this.stadiumId.trim(), this.name.trim());
		this.practiceSessions = new ArrayList<>();
	}

	public String getStadiumId() {
		return stadiumId.trim();
	}

	public void setStadiumId(String stadiumId) {
		this.stadiumId = String.format("%-10s", stadiumId != null ? stadiumId.trim() : "");
	}

	public String getName() {
		return name.trim();
	}

	public void setName(String name) {
		this.name = String.format("%-25s", name != null ? name.trim() : "");
	}

	public String getLocation() {
		return location.trim();
	}

	public void setLocation(String location) {
		this.location = String.format("%-20s", location != null ? location.trim() : "");
	}

	public int getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public int getTicketsSold() {
		return ticketsSold;
	}

	public void setTicketsSold(int ticketsSold) {
		this.ticketsSold = ticketsSold;
	}

	public int getAvailableSeats() {
		return seatingCapacity - ticketsSold;
	}

	public String getFieldCondition() {
		return fieldCondition.trim();
	}

	public void setFieldCondition(String fieldCondition) {
		this.fieldCondition = String.format("%-15s", fieldCondition != null ? fieldCondition.trim() : "GOOD");
	}

	public double getBaseTicketPrice() {
		return baseTicketPrice;
	}

	public void setBaseTicketPrice(double baseTicketPrice) {
		this.baseTicketPrice = baseTicketPrice;
	}

	public StadiumShop getStadiumShop() {
		return stadiumShop;
	}

	public List<String> getPracticeSessions() {
		return Collections.unmodifiableList(practiceSessions);
	}

	public boolean hasAvailableCapacity(int quantity) {
		return (ticketsSold + quantity) <= seatingCapacity;
	}

	public void allocateTickets(int quantity) throws InvalidValueException, CapacityExceededException {
		if (quantity <= 0) {
			throw new InvalidValueException("Requested ticket quantity must be greater than zero.");
		}
		if (!hasAvailableCapacity(quantity)) {
			throw new CapacityExceededException(
				String.format("Cannot sell %d tickets for %s. Available seats: %d (Capacity: %d, Sold: %d)",
					quantity, name.trim(), getAvailableSeats(), seatingCapacity, ticketsSold)
			);
		}
		this.ticketsSold += quantity;
	}

	public void schedulePractice(FootballTeam team, String sessionTime) throws InvalidValueException {
		if (team == null) {
			throw new InvalidValueException("Cannot schedule practice for null team.");
		}
		if ("SUSPENDED".equalsIgnoreCase(team.getLeagueStatus())) {
			throw new InvalidValueException("Suspended team " + team.getName() + " cannot book practice sessions.");
		}
		practiceSessions.add(String.format("Team: %s | Time: %s | Venue: %s", team.getName(), sessionTime, name.trim()));
	}

	public void resetOccupancy() {
		this.ticketsSold = 0;
	}

	public void display() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		return "Stadium ID: " + stadiumId.trim() + 
				"\nName: " + name.trim() + 
				"\nLocation: " + location.trim() + 
				"\nSeating Capacity: " + seatingCapacity + 
				"\nTickets Sold: " + ticketsSold + 
				"\nAvailable Seats: " + getAvailableSeats() + 
				"\nField Condition: " + fieldCondition.trim() + 
				"\nBase Ticket Price: $" + baseTicketPrice + 
				"\n";
	}
}
