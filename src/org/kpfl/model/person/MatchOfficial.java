package org.kpfl.model.person;

import org.kpfl.exception.InvalidValueException;

public class MatchOfficial extends Person {
	private String officiatingRole;	//30 (15 * 2) bytes
	private int rank;				//4 bytes
	private boolean isAvailable;	//1 byte TOTAL: 35 BYTES (+66 super = 101 BYTES)

	public MatchOfficial() {
		super();
		this.officiatingRole = String.format("%-15s", "Referee");
		this.rank = 1;
		this.isAvailable = true;
	}

	public MatchOfficial(MatchOfficial m) {
		super(m);
		if (m != null) {
			this.officiatingRole = String.format("%-15s", m.officiatingRole);
			this.rank = m.rank;
			this.isAvailable = m.isAvailable;
		}
	}

	public MatchOfficial(String id, String name, int age, char gender, String officiatingRole, int rank)
			throws InvalidValueException {
		super(id, name, age, gender);
		if (rank < 1) {
			throw new InvalidValueException("Official rank tier must be a positive integer.");
		}
		this.officiatingRole = String.format("%-15s", officiatingRole != null ? officiatingRole.trim() : "Referee");
		this.rank = rank;
		this.isAvailable = true;
	}

	public MatchOfficial(String id, String name, int age, String gender, String officiatingRole, int rank)
			throws InvalidValueException {
		this(id, name, age, (gender != null && !gender.trim().isEmpty()) ? gender.trim().toUpperCase().charAt(0) : '?', officiatingRole, rank);
	}

	public String getOfficiatingRole() {
		return officiatingRole.trim();
	}

	public void setOfficiatingRole(String officiatingRole) {
		this.officiatingRole = String.format("%-15s", officiatingRole != null ? officiatingRole.trim() : "Referee");
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) throws InvalidValueException {
		if (rank < 1) {
			throw new InvalidValueException("Official rank tier must be a positive integer.");
		}
		this.rank = rank;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	@Override
	public String getDetails() {
		return toString();
	}

	@Override
	public String toString() {
		return "Official ID: " + getId() + 
				"\nName: " + getName() + 
				"\nAge: " + getAge() + 
				"\nGender: " + getGender() + 
				"\nRole: " + officiatingRole.trim() + 
				"\nRank: Tier " + rank + 
				"\nAvailable: " + (isAvailable ? "Yes" : "No") + 
				"\n";
	}
}
