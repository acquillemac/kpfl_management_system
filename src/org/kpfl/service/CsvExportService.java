package org.kpfl.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.kpfl.model.league.FootballLeague;
import org.kpfl.model.league.FootballTeam;
import org.kpfl.model.league.Stadium;
import org.kpfl.model.person.MatchOfficial;
import org.kpfl.model.person.Player;

/**
 * Extension feature (Section 15 of project brief):
 * Exports tabular reports directly into CSV format for external spreadsheet analysis.
 */
public class CsvExportService {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Exports league standings to CSV.
     */
    public static File exportStandingsCsv(FootballLeague league, String outputDir) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) dir.mkdirs();

        String filename = "Standings_" + LocalDateTime.now().format(DTF) + ".csv";
        File file = new File(dir, filename);

        try (FileWriter fw = new FileWriter(file)) {
            fw.write("Position,Club Name,Parish,Matches Played,Wins,Draws,Losses,Goals For,Goals Against,Goal Difference,Points\n");
            List<FootballTeam> standings = league.getStandings();
            int pos = 1;
            for (FootballTeam t : standings) {
                fw.write(String.format("%d,\"%s\",\"%s\",%d,%d,%d,%d,%d,%d,%d,%d\n",
                        pos++,
                        t.getName().replace("\"", "\"\""),
                        t.getParish().replace("\"", "\"\""),
                        t.getMatchesPlayed(),
                        t.getWins(),
                        t.getDraws(),
                        t.getLosses(),
                        t.getGoalsFor(),
                        t.getGoalsAgainst(),
                        t.getGoalDifference(),
                        t.getPoints()));
            }
        }
        return file;
    }

    /**
     * Exports match-day venue attendance and financial figures to CSV.
     */
    public static File exportFinancialAuditCsv(FootballLeague league, String outputDir) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) dir.mkdirs();

        String filename = "Financial_Audit_" + LocalDateTime.now().format(DTF) + ".csv";
        File file = new File(dir, filename);

        try (FileWriter fw = new FileWriter(file)) {
            fw.write("Stadium ID,Venue Name,Location,Seating Capacity,Tickets Sold,Occupancy Rate (%),Base Ticket Price,Gross Ticket Revenue,Ticket 15% GCT,Shop Transactions,Shop Gross Revenue,Shop 15% GCT,Total Match-Day Gross\n");
            for (Stadium s : league.getStadiums()) {
                double baseRev = s.getTicketsSold() * s.getBaseTicketPrice();
                double tGct = baseRev * 0.15;
                double tGross = baseRev + tGct;
                double occ = (s.getSeatingCapacity() > 0) ? ((double) s.getTicketsSold() / s.getSeatingCapacity()) * 100.0 : 0.0;
                double sGross = s.getStadiumShop().getTotalGrossRevenue();
                double sGct = s.getStadiumShop().getTotalGCTCollected();
                double venueTotal = tGross + sGross;

                fw.write(String.format("\"%s\",\"%s\",\"%s\",%d,%d,%.2f,%.2f,%.2f,%.2f,%d,%.2f,%.2f,%.2f\n",
                        s.getStadiumId(),
                        s.getName().replace("\"", "\"\""),
                        s.getLocation().replace("\"", "\"\""),
                        s.getSeatingCapacity(),
                        s.getTicketsSold(),
                        occ,
                        s.getBaseTicketPrice(),
                        tGross,
                        tGct,
                        s.getStadiumShop().getTransactionHistory().size(),
                        sGross,
                        sGct,
                        venueTotal));
            }
        }
        return file;
    }

    /**
     * Exports personnel & compliance audit data to CSV.
     */
    public static File exportComplianceAuditCsv(FootballLeague league, String outputDir) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) dir.mkdirs();

        String filename = "Compliance_Audit_" + LocalDateTime.now().format(DTF) + ".csv";
        File file = new File(dir, filename);

        try (FileWriter fw = new FileWriter(file)) {
            fw.write("Registration No,Club Name,Status,Squad Size,Head Coach,Coach Qualification,Min Age Compliance\n");
            for (FootballTeam t : league.getTeams()) {
                boolean allEligible = true;
                for (Player p : t.getPlayers()) {
                    if (p.getAge() < Player.MINIMUM_LEAGUE_AGE) {
                        allEligible = false;
                        break;
                    }
                }
                String cName = (t.getHeadCoach() != null) ? t.getHeadCoach().getName() : "UNASSIGNED";
                String cQual = (t.getHeadCoach() != null) ? t.getHeadCoach().getQualification() : "N/A";

                fw.write(String.format("\"%s\",\"%s\",\"%s\",%d,\"%s\",\"%s\",\"%s\"\n",
                        t.getRegistrationNumber(),
                        t.getName().replace("\"", "\"\""),
                        t.getLeagueStatus(),
                        t.getSquadSize(),
                        cName.replace("\"", "\"\""),
                        cQual.replace("\"", "\"\""),
                        allEligible ? "PASS" : "FAIL"));
            }
        }
        return file;
    }
}
