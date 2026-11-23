package org.kpfl.model.person;

import org.kpfl.exception.InvalidValueException;

public class LeagueCommissioner extends Person {
	private String accreditationCode;	//40 (20 * 2) bytes TOTAL: 40 BYTES (+66 super = 106 BYTES)

	public LeagueCommissioner() {
		super();
		this.accreditationCode = String.format("%-20s", "JFF-KPFL-ACC-001");
	}

	public LeagueCommissioner(LeagueCommissioner l) {
		super(l);
		if (l != null) {
			this.accreditationCode = String.format("%-20s", l.accreditationCode);
		}
	}

	public LeagueCommissioner(String id, String name, int age, char gender, String accreditationCode) {
		super(id, name, age, gender);
		this.accreditationCode = String.format("%-20s", accreditationCode != null ? accreditationCode.trim() : "JFF-KPFL-ACC-001");
	}

	public LeagueCommissioner(String id, String name, int age, String gender, String accreditationCode)
			throws InvalidValueException {
		super(id, name, age, (gender != null && !gender.trim().isEmpty()) ? gender.trim().toUpperCase().charAt(0) : '?');
		this.accreditationCode = String.format("%-20s", accreditationCode != null ? accreditationCode.trim() : "JFF-KPFL-ACC-001");
	}

	public String getAccreditationCode() {
		return accreditationCode.trim();
	}

	public void setAccreditationCode(String accreditationCode) {
		this.accreditationCode = String.format("%-20s", accreditationCode != null ? accreditationCode.trim() : "JFF-KPFL-ACC-001");
	}

	@Override
	public String getDetails() {
		return toString();
	}

	@Override
	public String toString() {
		return "Commissioner ID: " + getId() + 
				"\nName: " + getName() + 
				"\nAge: " + getAge() + 
				"\nGender: " + getGender() + 
				"\nAccreditation: " + accreditationCode.trim() + 
				"\n";
	}
}
