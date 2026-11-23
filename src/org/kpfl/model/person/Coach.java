package org.kpfl.model.person;

import org.kpfl.exception.InvalidValueException;

public class Coach extends Person {
	private String qualification;	//40 (20 * 2) bytes
	private String coachingRole;	//30 (15 * 2) bytes
	private String teamAssignment;	//40 (20 * 2) bytes TOTAL: 110 BYTES (+66 super = 176 BYTES)

	public Coach() {
		super();
		this.qualification = String.format("%-20s", "Standard Certificate");
		this.coachingRole = String.format("%-15s", "Head Coach");
		this.teamAssignment = String.format("%-20s", "Unassigned");
	}

	public Coach(Coach c) {
		super(c);
		if (c != null) {
			this.qualification = String.format("%-20s", c.qualification);
			this.coachingRole = String.format("%-15s", c.coachingRole);
			this.teamAssignment = String.format("%-20s", c.teamAssignment);
		}
	}

	public Coach(String id, String name, int age, char gender, String qualification, String coachingRole) {
		super(id, name, age, gender);
		this.qualification = String.format("%-20s", qualification != null ? qualification.trim() : "Standard Certificate");
		this.coachingRole = String.format("%-15s", coachingRole != null ? coachingRole.trim() : "Head Coach");
		this.teamAssignment = String.format("%-20s", "Unassigned");
	}

	public Coach(String id, String name, int age, String gender, String qualification, String coachingRole)
			throws InvalidValueException {
		super(id, name, age, (gender != null && !gender.trim().isEmpty()) ? gender.trim().toUpperCase().charAt(0) : '?');
		this.qualification = String.format("%-20s", qualification != null ? qualification.trim() : "Standard Certificate");
		this.coachingRole = String.format("%-15s", coachingRole != null ? coachingRole.trim() : "Head Coach");
		this.teamAssignment = String.format("%-20s", "Unassigned");
	}

	public String getQualification() {
		return qualification.trim();
	}

	public void setQualification(String qualification) {
		this.qualification = String.format("%-20s", qualification != null ? qualification.trim() : "Standard Certificate");
	}

	public String getCoachingRole() {
		return coachingRole.trim();
	}

	public void setCoachingRole(String coachingRole) {
		this.coachingRole = String.format("%-15s", coachingRole != null ? coachingRole.trim() : "Head Coach");
	}

	public String getTeamAssignment() {
		return teamAssignment.trim();
	}

	public void setTeamAssignment(String teamAssignment) {
		this.teamAssignment = String.format("%-20s", teamAssignment != null ? teamAssignment.trim() : "Unassigned");
	}

	@Override
	public String getDetails() {
		return toString();
	}

	@Override
	public String toString() {
		return "Coach ID: " + getId() + 
				"\nName: " + getName() + 
				"\nAge: " + getAge() + 
				"\nGender: " + getGender() + 
				"\nRole: " + coachingRole.trim() + 
				"\nQualification: " + qualification.trim() + 
				"\nAssigned Team: " + teamAssignment.trim() + 
				"\n";
	}
}
