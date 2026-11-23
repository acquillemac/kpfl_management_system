package org.kpfl.model.league;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kpfl.exception.InvalidValueException;
import org.kpfl.exception.PlayerAgeRequirementException;
import org.kpfl.model.person.Coach;
import org.kpfl.model.person.Player;

public class FootballTeam implements Comparable<FootballTeam> {
	private String registrationNumber;	//30 (15 * 2) bytes
	private String name;				//50 (25 * 2) bytes
	private String parish;				//30 (15 * 2) bytes
	private int squadSize;				//4 bytes
	private String leagueStatus;		//20 (10 * 2) bytes
	private int matchesPlayed;			//4 bytes
	private int wins;					//4 bytes
	private int draws;					//4 bytes
	private int losses;					//4 bytes
	private int goalsFor;				//4 bytes
	private int goalsAgainst;			//4 bytes
	private int points;					//4 bytes TOTAL: 162 BYTES

	private List<Player> players;
	private Coach headCoach;

	public FootballTeam() {
		this.registrationNumber = String.format("%-15s", "");
		this.name = String.format("%-25s", "");
		this.parish = String.format("%-15s", "Kingston");
		this.squadSize = 0;
		this.leagueStatus = String.format("%-10s", "ACTIVE");
		this.matchesPlayed = 0;
		this.wins = 0;
		this.draws = 0;
		this.losses = 0;
		this.goalsFor = 0;
		this.goalsAgainst = 0;
		this.points = 0;
		this.players = new ArrayList<>();
		this.headCoach = null;
	}

	public FootballTeam(FootballTeam t) {
		if (t != null) {
			this.registrationNumber = String.format("%-15s", t.registrationNumber);
			this.name = String.format("%-25s", t.name);
			this.parish = String.format("%-15s", t.parish);
			this.squadSize = t.squadSize;
			this.leagueStatus = String.format("%-10s", t.leagueStatus);
			this.matchesPlayed = t.matchesPlayed;
			this.wins = t.wins;
			this.draws = t.draws;
			this.losses = t.losses;
			this.goalsFor = t.goalsFor;
			this.goalsAgainst = t.goalsAgainst;
			this.points = t.points;
			this.players = new ArrayList<>(t.players);
			this.headCoach = t.headCoach;
		}
	}

	public FootballTeam(String registrationNumber, String name, String parish) throws InvalidValueException {
		if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
			throw new InvalidValueException("Team registration number cannot be empty.");
		}
		if (name == null || name.trim().isEmpty()) {
			throw new InvalidValueException("Team name cannot be empty.");
		}
		this.registrationNumber = String.format("%-15s", registrationNumber.trim());
		this.name = String.format("%-25s", name.trim());
		this.parish = String.format("%-15s", parish != null ? parish.trim() : "Kingston");
		this.squadSize = 0;
		this.leagueStatus = String.format("%-10s", "ACTIVE");
		this.matchesPlayed = 0;
		this.wins = 0;
		this.draws = 0;
		this.losses = 0;
		this.goalsFor = 0;
		this.goalsAgainst = 0;
		this.points = 0;
		this.players = new ArrayList<>();
		this.headCoach = null;
	}

	public String getRegistrationNumber() {
		return registrationNumber.trim();
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = String.format("%-15s", registrationNumber != null ? registrationNumber.trim() : "");
	}

	public String getName() {
		return name.trim();
	}

	public void setName(String name) {
		this.name = String.format("%-25s", name != null ? name.trim() : "");
	}

	public String getParish() {
		return parish.trim();
	}

	public void setParish(String parish) {
		this.parish = String.format("%-15s", parish != null ? parish.trim() : "Kingston");
	}

	public int getSquadSize() {
		return players.size();
	}

	public String getLeagueStatus() {
		return leagueStatus.trim();
	}

	public void setLeagueStatus(String leagueStatus) {
		this.leagueStatus = String.format("%-10s", leagueStatus != null ? leagueStatus.trim().toUpperCase() : "ACTIVE");
	}

	public Coach getHeadCoach() {
		return headCoach;
	}

	public void setHeadCoach(Coach headCoach) {
		this.headCoach = headCoach;
		if (headCoach != null) {
			headCoach.setTeamAssignment(this.getName());
		}
	}

	public List<Player> getPlayers() {
		return Collections.unmodifiableList(players);
	}

	public void addPlayer(Player player) throws InvalidValueException, PlayerAgeRequirementException {
		if (player == null) {
			throw new InvalidValueException("Cannot add null player.");
		}
		if (player.getAge() < Player.MINIMUM_LEAGUE_AGE) {
			throw new PlayerAgeRequirementException(
				String.format("Player %s is %d years old. League minimum age is %d.",
					player.getName(), player.getAge(), Player.MINIMUM_LEAGUE_AGE)
			);
		}
		for (Player p : players) {
			if (p.getJerseyNumber() == player.getJerseyNumber()) {
				throw new InvalidValueException(
					String.format("Jersey number #%d is already assigned to %s on %s.",
						player.getJerseyNumber(), p.getName(), this.getName())
				);
			}
		}
		players.add(player);
		this.squadSize = players.size();
	}

	public void removePlayer(String playerId) {
		players.removeIf(p -> p.getId().equalsIgnoreCase(playerId));
		this.squadSize = players.size();
	}

	public void recordMatchResult(int scored, int conceded) throws InvalidValueException {
		if (scored < 0 || conceded < 0) {
			throw new InvalidValueException("Goals scored and conceded cannot be negative.");
		}
		this.matchesPlayed++;
		this.goalsFor += scored;
		this.goalsAgainst += conceded;

		if (scored > conceded) {
			this.wins++;
			this.points += 3;
		} else if (scored == conceded) {
			this.draws++;
			this.points += 1;
		} else {
			this.losses++;
		}
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}

	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getGoalsFor() {
		return goalsFor;
	}

	public void setGoalsFor(int goalsFor) {
		this.goalsFor = goalsFor;
	}

	public int getGoalsAgainst() {
		return goalsAgainst;
	}

	public void setGoalsAgainst(int goalsAgainst) {
		this.goalsAgainst = goalsAgainst;
	}

	public int getGoalDifference() {
		return goalsFor - goalsAgainst;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setManualStats(int played, int w, int d, int l, int gf, int ga, int pts) {
		this.matchesPlayed = played;
		this.wins = w;
		this.draws = d;
		this.losses = l;
		this.goalsFor = gf;
		this.goalsAgainst = ga;
		this.points = pts;
	}

	public void display() {
		System.out.println(this.toString());
	}

	@Override
	public int compareTo(FootballTeam other) {
		if (this.points != other.points) {
			return Integer.compare(other.points, this.points);
		}
		if (this.getGoalDifference() != other.getGoalDifference()) {
			return Integer.compare(other.getGoalDifference(), this.getGoalDifference());
		}
		return Integer.compare(other.goalsFor, this.goalsFor);
	}

	@Override
	public String toString() {
		return "Registration Number: " + registrationNumber.trim() + 
				"\nName: " + name.trim() + 
				"\nParish: " + parish.trim() + 
				"\nSquad Size: " + getSquadSize() + 
				"\nStatus: " + leagueStatus.trim() + 
				"\nMatches Played: " + matchesPlayed + 
				"\nWins: " + wins + 
				"\nDraws: " + draws + 
				"\nLosses: " + losses + 
				"\nGoals For: " + goalsFor + 
				"\nGoals Against: " + goalsAgainst + 
				"\nGoal Difference: " + getGoalDifference() + 
				"\nPoints: " + points + 
				"\n";
	}
}
