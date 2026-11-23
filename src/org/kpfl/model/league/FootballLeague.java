package org.kpfl.model.league;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.kpfl.exception.InvalidValueException;
import org.kpfl.exception.SchedulingConflictException;
import org.kpfl.exception.TeamIneligibleException;
import org.kpfl.model.person.LeagueCommissioner;
import org.kpfl.model.person.MatchOfficial;

/**
 * Root coordinator for the Kingston Premier Football League.
 */
public class FootballLeague {
    private String leagueName;
    private String season;
    private LeagueCommissioner commissioner;

    private List<FootballTeam> teams;
    private List<Stadium> stadiums;
    private List<Fixture> fixtures;
    private List<MatchOfficial> officials;

    public FootballLeague(String leagueName, String season) {
        this.leagueName = (leagueName != null) ? leagueName : "Kingston Premier Football League";
        this.season = (season != null) ? season : "2026/2027";
        this.teams = new ArrayList<>();
        this.stadiums = new ArrayList<>();
        this.fixtures = new ArrayList<>();
        this.officials = new ArrayList<>();
    }

    public String getLeagueName() {
        return leagueName;
    }

    public String getSeason() {
        return season;
    }

    public LeagueCommissioner getCommissioner() {
        return commissioner;
    }

    public void setCommissioner(LeagueCommissioner commissioner) {
        this.commissioner = commissioner;
    }

    public void registerTeam(FootballTeam team) throws InvalidValueException {
        if (team == null) {
            throw new InvalidValueException("Cannot register null team.");
        }
        for (FootballTeam t : teams) {
            if (t.getRegistrationNumber().equalsIgnoreCase(team.getRegistrationNumber())) {
                throw new InvalidValueException("Team with registration number " + team.getRegistrationNumber() + " already exists.");
            }
        }
        teams.add(team);
    }

    public void registerStadium(Stadium stadium) throws InvalidValueException {
        if (stadium == null) {
            throw new InvalidValueException("Cannot register null stadium.");
        }
        for (Stadium s : stadiums) {
            if (s.getStadiumId().equalsIgnoreCase(stadium.getStadiumId())) {
                throw new InvalidValueException("Stadium with ID " + stadium.getStadiumId() + " already exists.");
            }
        }
        stadiums.add(stadium);
    }

    public void registerOfficial(MatchOfficial official) throws InvalidValueException {
        if (official == null) {
            throw new InvalidValueException("Cannot register null official.");
        }
        for (MatchOfficial o : officials) {
            if (o.getId().equalsIgnoreCase(official.getId())) {
                throw new InvalidValueException("Official with ID " + official.getId() + " already exists.");
            }
        }
        officials.add(official);
    }

    /**
     * Schedules a fixture after enforcing conflict checks (BR-02: Team cannot play twice on same day/time).
     */
    public void scheduleFixture(String fixtureId, FootballTeam homeTeam, FootballTeam awayTeam,
                                Stadium venue, LocalDate date, LocalTime time)
            throws InvalidValueException, TeamIneligibleException, SchedulingConflictException {

        // Check for team scheduling conflicts
        for (Fixture f : fixtures) {
            if (f.getMatchDate().equals(date) && f.getMatchTime().equals(time)) {
                String fHome = f.getHomeTeam().getRegistrationNumber();
                String fAway = f.getAwayTeam().getRegistrationNumber();
                String newHome = homeTeam.getRegistrationNumber();
                String newAway = awayTeam.getRegistrationNumber();

                if (fHome.equalsIgnoreCase(newHome) || fAway.equalsIgnoreCase(newHome)) {
                    throw new SchedulingConflictException("Team " + homeTeam.getName() + " already has a fixture scheduled at " + date + " " + time);
                }
                if (fHome.equalsIgnoreCase(newAway) || fAway.equalsIgnoreCase(newAway)) {
                    throw new SchedulingConflictException("Team " + awayTeam.getName() + " already has a fixture scheduled at " + date + " " + time);
                }
            }
        }

        Fixture fixture = new Fixture(fixtureId, homeTeam, awayTeam, venue, date, time);
        fixtures.add(fixture);
    }

    public List<FootballTeam> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    public List<Stadium> getStadiums() {
        return Collections.unmodifiableList(stadiums);
    }

    public List<Fixture> getFixtures() {
        return Collections.unmodifiableList(fixtures);
    }

    public List<MatchOfficial> getOfficials() {
        return Collections.unmodifiableList(officials);
    }

    /**
     * Finds a team by exact registration number (case-insensitive).
     */
    public FootballTeam findTeamByRegistration(String regNo) {
        if (regNo == null) return null;
        for (FootballTeam t : teams) {
            if (t.getRegistrationNumber().equalsIgnoreCase(regNo.trim())) {
                return t;
            }
        }
        return null;
    }

    /**
     * Searches teams by parish.
     */
    public List<FootballTeam> searchTeamsByParish(String parish) {
        List<FootballTeam> result = new ArrayList<>();
        if (parish == null) return result;
        for (FootballTeam t : teams) {
            if (t.getParish().equalsIgnoreCase(parish.trim())) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Searches teams by name substring.
     */
    public List<FootballTeam> searchTeamsByName(String query) {
        List<FootballTeam> result = new ArrayList<>();
        if (query == null) return result;
        String q = query.trim().toLowerCase();
        for (FootballTeam t : teams) {
            if (t.getName().toLowerCase().contains(q)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Finds a stadium by ID.
     */
    public Stadium findStadiumById(String stadiumId) {
        if (stadiumId == null) return null;
        for (Stadium s : stadiums) {
            if (s.getStadiumId().equalsIgnoreCase(stadiumId.trim())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Finds a fixture by ID.
     */
    public Fixture findFixtureById(String fixtureId) {
        if (fixtureId == null) return null;
        for (Fixture f : fixtures) {
            if (f.getFixtureId().equalsIgnoreCase(fixtureId.trim())) {
                return f;
            }
        }
        return null;
    }

    /**
     * Books a pre-match stadium practice session for a team.
     */
    public void requestStadiumPractice(String teamRegNo, String stadiumId, String sessionTime)
            throws InvalidValueException {
        FootballTeam team = findTeamByRegistration(teamRegNo);
        if (team == null) {
            throw new InvalidValueException("Team not found with registration number: " + teamRegNo);
        }
        Stadium stadium = findStadiumById(stadiumId);
        if (stadium == null) {
            throw new InvalidValueException("Stadium not found with ID: " + stadiumId);
        }
        stadium.schedulePractice(team, sessionTime);
    }

    /**
     * Generates league standings sorted by points, goal difference, and goals scored.
     */
    public List<FootballTeam> getStandings() {
        List<FootballTeam> sorted = new ArrayList<>(teams);
        Collections.sort(sorted);
        return sorted;
    }
}
