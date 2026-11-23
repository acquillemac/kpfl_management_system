package org.kpfl.model.league;

import java.time.LocalDateTime;
import org.kpfl.exception.InvalidValueException;

public class MatchResult {
	private int homeScore;				//4 bytes
	private int awayScore;				//4 bytes
	private String outcomeDescription;	//20 (10 * 2) bytes TOTAL: 28 BYTES

	private LocalDateTime recordedAt;

	public MatchResult() {
		this.homeScore = 0;
		this.awayScore = 0;
		this.outcomeDescription = String.format("%-10s", "DRAW");
		this.recordedAt = LocalDateTime.now();
	}

	public MatchResult(MatchResult m) {
		if (m != null) {
			this.homeScore = m.homeScore;
			this.awayScore = m.awayScore;
			this.outcomeDescription = String.format("%-10s", m.outcomeDescription);
			this.recordedAt = m.recordedAt;
		}
	}

	public MatchResult(int homeScore, int awayScore) throws InvalidValueException {
		if (homeScore < 0 || awayScore < 0) {
			throw new InvalidValueException("Match scores cannot be negative.");
		}
		this.homeScore = homeScore;
		this.awayScore = awayScore;
		this.recordedAt = LocalDateTime.now();

		if (homeScore > awayScore) {
			this.outcomeDescription = String.format("%-10s", "HOME_WIN");
		} else if (awayScore > homeScore) {
			this.outcomeDescription = String.format("%-10s", "AWAY_WIN");
		} else {
			this.outcomeDescription = String.format("%-10s", "DRAW");
		}
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public String getOutcomeDescription() {
		return outcomeDescription.trim();
	}

	public void setOutcomeDescription(String outcomeDescription) {
		this.outcomeDescription = String.format("%-10s", outcomeDescription != null ? outcomeDescription.trim() : "DRAW");
	}

	public LocalDateTime getRecordedAt() {
		return recordedAt;
	}

	public void display() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		return "Home Score: " + homeScore + 
				"\nAway Score: " + awayScore + 
				"\nOutcome: " + outcomeDescription.trim() + 
				"\nRecorded At: " + recordedAt + 
				"\n";
	}
}
