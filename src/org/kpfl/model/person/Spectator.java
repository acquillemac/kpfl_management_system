package org.kpfl.model.person;

import org.kpfl.exception.InvalidValueException;

public class Spectator extends Person {
	private String seatCategoryPreference;	//30 (15 * 2) bytes TOTAL: 30 BYTES (+66 super = 96 BYTES)

	public Spectator() {
		super();
		this.seatCategoryPreference = String.format("%-15s", "Bleachers");
	}

	public Spectator(Spectator s) {
		super(s);
		if (s != null) {
			this.seatCategoryPreference = String.format("%-15s", s.seatCategoryPreference);
		}
	}

	public Spectator(String id, String name, int age, char gender, String seatCategoryPreference) {
		super(id, name, age, gender);
		this.seatCategoryPreference = String.format("%-15s", seatCategoryPreference != null ? seatCategoryPreference.trim() : "Bleachers");
	}

	public Spectator(String id, String name, int age, String gender, String seatCategoryPreference)
			throws InvalidValueException {
		super(id, name, age, (gender != null && !gender.trim().isEmpty()) ? gender.trim().toUpperCase().charAt(0) : '?');
		this.seatCategoryPreference = String.format("%-15s", seatCategoryPreference != null ? seatCategoryPreference.trim() : "Bleachers");
	}

	public String getSeatCategoryPreference() {
		return seatCategoryPreference.trim();
	}

	public void setSeatCategoryPreference(String seatCategoryPreference) {
		this.seatCategoryPreference = String.format("%-15s", seatCategoryPreference != null ? seatCategoryPreference.trim() : "Bleachers");
	}

	@Override
	public String getDetails() {
		return toString();
	}

	@Override
	public String toString() {
		return "Spectator ID: " + getId() + 
				"\nName: " + getName() + 
				"\nAge: " + getAge() + 
				"\nGender: " + getGender() + 
				"\nPreferred Seating: " + seatCategoryPreference.trim() + 
				"\n";
	}
}
