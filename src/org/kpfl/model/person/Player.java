package org.kpfl.model.person;

import org.kpfl.exception.InvalidValueException;
import org.kpfl.exception.PlayerAgeRequirementException;

public class Player extends Person {
	public static final int MINIMUM_LEAGUE_AGE = 16;

	private String position;		//30 (15 * 2) bytes
	private int jerseyNumber;		//4 bytes
	private int goalsScored;		//4 bytes
	private String playingStatus;	//20 (10 * 2) bytes TOTAL: 58 BYTES (+66 super = 124 BYTES)

	public Player() {
		super();
		this.position = String.format("%-15s", "");
		this.jerseyNumber = 1;
		this.goalsScored = 0;
		this.playingStatus = String.format("%-10s", "ACTIVE");
	}

	public Player(Player p) {
		super(p);
		if (p != null) {
			this.position = String.format("%-15s", p.position);
			this.jerseyNumber = p.jerseyNumber;
			this.goalsScored = p.goalsScored;
			this.playingStatus = String.format("%-10s", p.playingStatus);
		}
	}

	public Player(String id, String name, int age, char gender, String position, int jerseyNumber)
			throws InvalidValueException, PlayerAgeRequirementException {
		super(id, name, age, gender);
		if (age < MINIMUM_LEAGUE_AGE) {
			throw new PlayerAgeRequirementException(
				String.format("Player %s is %d years old. League minimum age is %d.", name, age, MINIMUM_LEAGUE_AGE)
			);
		}
		if (jerseyNumber < 1 || jerseyNumber > 99) {
			throw new InvalidValueException("Jersey number must be between 1 and 99.");
		}
		this.position = String.format("%-15s", position != null ? position.trim() : "Unassigned");
		this.jerseyNumber = jerseyNumber;
		this.goalsScored = 0;
		this.playingStatus = String.format("%-10s", "ACTIVE");
	}

	public Player(String id, String name, int age, String gender, String position, int jerseyNumber)
			throws InvalidValueException, PlayerAgeRequirementException {
		this(id, name, age, (gender != null && !gender.trim().isEmpty()) ? gender.trim().toUpperCase().charAt(0) : '?', position, jerseyNumber);
	}

	public String getPosition() {
		return position.trim();
	}

	public void setPosition(String position) {
		this.position = String.format("%-15s", position != null ? position.trim() : "Unassigned");
	}

	public int getJerseyNumber() {
		return jerseyNumber;
	}

	public void setJerseyNumber(int jerseyNumber) throws InvalidValueException {
		if (jerseyNumber < 1 || jerseyNumber > 99) {
			throw new InvalidValueException("Jersey number must be between 1 and 99.");
		}
		this.jerseyNumber = jerseyNumber;
	}

	public int getGoalsScored() {
		return goalsScored;
	}

	public void setGoalsScored(int goalsScored) throws InvalidValueException {
		if (goalsScored < 0) {
			throw new InvalidValueException("Goals scored cannot be negative.");
		}
		this.goalsScored = goalsScored;
	}

	public void recordGoal() {
		this.goalsScored++;
	}

	public String getPlayingStatus() {
		return playingStatus.trim();
	}

	public void setPlayingStatus(String playingStatus) {
		this.playingStatus = String.format("%-10s", playingStatus != null ? playingStatus.trim().toUpperCase() : "ACTIVE");
	}

	@Override
	public String getDetails() {
		return toString();
	}

	@Override
	public String toString() {
		return "Player ID: " + getId() + 
				"\nName: " + getName() + 
				"\nAge: " + getAge() + 
				"\nGender: " + getGender() + 
				"\nPosition: " + position.trim() + 
				"\nJersey Number: #" + jerseyNumber + 
				"\nGoals Scored: " + goalsScored + 
				"\nPlaying Status: " + playingStatus.trim() + 
				"\n";
	}
}
