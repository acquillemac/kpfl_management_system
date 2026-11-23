package org.kpfl.model.league;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kpfl.exception.InvalidFixtureStateException;
import org.kpfl.exception.InvalidValueException;
import org.kpfl.exception.OfficialUnavailableException;
import org.kpfl.exception.TeamIneligibleException;
import org.kpfl.model.person.MatchOfficial;

public class Fixture {
	private String fixtureId;		//20 (10 * 2) bytes
	private String status;			//20 (10 * 2) bytes TOTAL: 40 BYTES

	private FootballTeam homeTeam;
	private FootballTeam awayTeam;
	private Stadium venue;
	private LocalDate matchDate;
	private LocalTime matchTime;
	private List<MatchOfficial> assignedOfficials;
	private MatchResult matchResult;

	public Fixture() {
		this.fixtureId = String.format("%-10s", "");
		this.homeTeam = null;
		this.awayTeam = null;
		this.venue = null;
		this.matchDate = LocalDate.now();
		this.matchTime = LocalTime.of(15, 0);
		this.status = String.format("%-10s", "PENDING");
		this.assignedOfficials = new ArrayList<>();
		this.matchResult = null;
	}

	public Fixture(Fixture f) {
		if (f != null) {
			this.fixtureId = String.format("%-10s", f.fixtureId);
			this.homeTeam = f.homeTeam;
			this.awayTeam = f.awayTeam;
			this.venue = f.venue;
			this.matchDate = f.matchDate;
			this.matchTime = f.matchTime;
			this.status = String.format("%-10s", f.status);
			this.assignedOfficials = new ArrayList<>(f.assignedOfficials);
			this.matchResult = f.matchResult;
		}
	}

	public Fixture(String fixtureId, FootballTeam homeTeam, FootballTeam awayTeam,
				   Stadium venue, LocalDate matchDate, LocalTime matchTime)
			throws InvalidValueException, TeamIneligibleException {
		if (fixtureId == null || fixtureId.trim().isEmpty()) {
			throw new InvalidValueException("Fixture ID cannot be empty.");
		}
		if (homeTeam == null || awayTeam == null) {
			throw new InvalidValueException("Both Home and Away teams must be specified.");
		}
		if (homeTeam.getRegistrationNumber().equalsIgnoreCase(awayTeam.getRegistrationNumber())) {
			throw new InvalidValueException("A club cannot play against itself.");
		}
		if (!"ACTIVE".equalsIgnoreCase(homeTeam.getLeagueStatus())) {
			throw new TeamIneligibleException("Home team " + homeTeam.getName() + " is suspended/inactive.");
		}
		if (!"ACTIVE".equalsIgnoreCase(awayTeam.getLeagueStatus())) {
			throw new TeamIneligibleException("Away team " + awayTeam.getName() + " is suspended/inactive.");
		}
		if (venue == null) {
			throw new InvalidValueException("Venue stadium must be provided.");
		}
		if (matchDate == null || matchTime == null) {
			throw new InvalidValueException("Match date and kick-off time must be specified.");
		}

		this.fixtureId = String.format("%-10s", fixtureId.trim());
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.venue = venue;
		this.matchDate = matchDate;
		this.matchTime = matchTime;
		this.status = String.format("%-10s", "SCHEDULED");
		this.assignedOfficials = new ArrayList<>();
		this.matchResult = null;
	}

	public String getFixtureId() {
		return fixtureId.trim();
	}

	public void setFixtureId(String fixtureId) {
		this.fixtureId = String.format("%-10s", fixtureId != null ? fixtureId.trim() : "");
	}

	public FootballTeam getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(FootballTeam homeTeam) {
		this.homeTeam = homeTeam;
	}

	public FootballTeam getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(FootballTeam awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Stadium getVenue() {
		return venue;
	}

	public void setVenue(Stadium venue) {
		this.venue = venue;
	}

	public LocalDate getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(LocalDate matchDate) {
		this.matchDate = matchDate;
	}

	public LocalTime getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(LocalTime matchTime) {
		this.matchTime = matchTime;
	}

	public String getStatus() {
		return status.trim();
	}

	public void setStatus(String status) {
		this.status = String.format("%-10s", status != null ? status.trim().toUpperCase() : "SCHEDULED");
	}

	public List<MatchOfficial> getAssignedOfficials() {
		return Collections.unmodifiableList(assignedOfficials);
	}

	public MatchResult getMatchResult() {
		return matchResult;
	}

	public void assignOfficial(MatchOfficial official) throws InvalidValueException, OfficialUnavailableException {
		if (official == null) {
			throw new InvalidValueException("Cannot assign null official.");
		}
		if (!official.isAvailable()) {
			throw new OfficialUnavailableException("Official " + official.getName() + " is currently unavailable.");
		}
		for (MatchOfficial o : assignedOfficials) {
			if (o.getId().equalsIgnoreCase(official.getId())) {
				throw new InvalidValueException("Official is already assigned to this match.");
			}
		}
		assignedOfficials.add(official);
	}

	public void recordResultAndComplete(int homeGoals, int awayGoals)
			throws InvalidValueException, InvalidFixtureStateException {
		if (!"SCHEDULED".equalsIgnoreCase(this.getStatus())) {
			throw new InvalidFixtureStateException("Fixture is not scheduled (Current: " + getStatus() + ").");
		}
		this.matchResult = new MatchResult(homeGoals, awayGoals);
		this.homeTeam.recordMatchResult(homeGoals, awayGoals);
		this.awayTeam.recordMatchResult(awayGoals, homeGoals);
		this.setStatus("COMPLETED");
	}

	public void display() {
		System.out.println(this.toString());
	}

	@Override
	public String toString() {
		String res = (matchResult != null) ? (matchResult.getHomeScore() + " - " + matchResult.getAwayScore()) : "Pending";
		return "Fixture ID: " + fixtureId.trim() + 
				"\nHome Team: " + (homeTeam != null ? homeTeam.getName() : "TBD") + 
				"\nAway Team: " + (awayTeam != null ? awayTeam.getName() : "TBD") + 
				"\nVenue: " + (venue != null ? venue.getName() : "TBD") + 
				"\nDate: " + matchDate + 
				"\nTime: " + matchTime + 
				"\nStatus: " + getStatus() + 
				"\nResult: " + res + 
				"\n";
	}
}
