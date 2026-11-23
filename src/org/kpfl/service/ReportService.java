package org.kpfl.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.kpfl.model.league.*;
import org.kpfl.model.person.MatchOfficial;
import org.kpfl.model.person.Player;
import org.kpfl.model.shop.SaleTransaction;
import org.kpfl.model.shop.StadiumShop;

/**
 * Generates official KPFL management, financial, and regulatory reports.
 */
public class ReportService {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Report 1: KPFL Official Standings & Table Report
     */
    public static String generateLeagueStandingsReport(FootballLeague league) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                 KINGSTON PREMIER FOOTBALL LEAGUE (KPFL) OFFICIAL STANDINGS             \n");
        sb.append("                                Season: ").append(league.getSeason()).append("\n");
        sb.append("                           Generated: ").append(LocalDateTime.now().format(DTF)).append("\n");
        sb.append("========================================================================================\n");
        sb.append(String.format("%-4s %-25s %-12s %3s %3s %3s %3s %4s %4s %5s %4s\n",
                "POS", "CLUB", "PARISH", "P", "W", "D", "L", "GF", "GA", "GD", "PTS"));
        sb.append("----------------------------------------------------------------------------------------\n");

        List<FootballTeam> standings = league.getStandings();
        int pos = 1;
        for (FootballTeam t : standings) {
            sb.append(String.format("%-4d %-25s %-12s %3d %3d %3d %3d %4d %4d %+5d %4d\n",
                    pos++,
                    t.getName(),
                    t.getParish(),
                    t.getMatchesPlayed(),
                    t.getWins(),
                    t.getDraws(),
                    t.getLosses(),
                    t.getGoalsFor(),
                    t.getGoalsAgainst(),
                    t.getGoalDifference(),
                    t.getPoints()));
        }
        sb.append("========================================================================================\n");
        sb.append("Points Rules: Win = 3 points | Draw = 1 point | Loss = 0 points\n");
        sb.append("Tie-breakers: Points -> Goal Difference -> Goals For\n");
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    /**
     * Report 2: Match-Day Financial, Ticketing & Attendance Report
     */
    public static String generateFinancialAndAttendanceReport(FootballLeague league) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                   KPFL MATCH-DAY FINANCIAL & ATTENDANCE AUDIT REPORT                   \n");
        sb.append("                           Generated: ").append(LocalDateTime.now().format(DTF)).append("\n");
        sb.append("========================================================================================\n");

        double totalTicketGross = 0.0;
        double totalTicketGCT = 0.0;
        int totalSeatsOccupied = 0;
        int totalCapacity = 0;

        sb.append(String.format("%-25s %10s %10s %10s %13s %11s\n",
                "STADIUM VENUE", "CAPACITY", "ATTENDANCE", "OCCUPANCY", "TICKET GROSS", "15% GCT"));
        sb.append("----------------------------------------------------------------------------------------\n");

        for (Stadium s : league.getStadiums()) {
            double baseRevenue = s.getTicketsSold() * s.getBaseTicketPrice();
            double gct = baseRevenue * 0.15;
            double gross = baseRevenue + gct;
            double occRate = (s.getSeatingCapacity() > 0) ? ((double) s.getTicketsSold() / s.getSeatingCapacity()) * 100.0 : 0.0;

            totalSeatsOccupied += s.getTicketsSold();
            totalCapacity += s.getSeatingCapacity();
            totalTicketGross += gross;
            totalTicketGCT += gct;

            sb.append(String.format("%-25s %10d %10d %9.1f%% $%12.2f $%10.2f\n",
                    s.getName(), s.getSeatingCapacity(), s.getTicketsSold(), occRate, gross, gct));
        }

        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(String.format("TOTAL VENUE CAPACITY:        %10d seats\n", totalCapacity));
        sb.append(String.format("TOTAL ATTENDANCE:            %10d tickets sold\n", totalSeatsOccupied));
        double overallOcc = (totalCapacity > 0) ? ((double) totalSeatsOccupied / totalCapacity) * 100.0 : 0.0;
        sb.append(String.format("OVERALL LEAGUE OCCUPANCY:    %9.1f%%\n", overallOcc));
        sb.append(String.format("TOTAL TICKET REVENUE (GROSS):$%12.2f (includes 15%% GCT: $%.2f)\n",
                totalTicketGross, totalTicketGCT));

        // Stadium Shop Concessions Section
        sb.append("\n-------------------------------- STADIUM SHOP CONCESSIONS ------------------------------\n");
        double shopGross = 0.0;
        double shopGCT = 0.0;
        for (Stadium s : league.getStadiums()) {
            StadiumShop shop = s.getStadiumShop();
            shopGross += shop.getTotalGrossRevenue();
            shopGCT += shop.getTotalGCTCollected();
            sb.append(String.format("Shop: %-25s | Sales: %2d transactions | Gross: $%9.2f | GCT: $%7.2f\n",
                    shop.getStadiumName(), shop.getTransactionHistory().size(),
                    shop.getTotalGrossRevenue(), shop.getTotalGCTCollected()));
        }

        sb.append("----------------------------------------------------------------------------------------\n");
        sb.append(String.format("COMBINED LEAGUE MATCH-DAY GROSS:  $%12.2f\n", (totalTicketGross + shopGross)));
        sb.append(String.format("TOTAL GENERAL CONSUMPTION TAX (GCT): $%9.2f\n", (totalTicketGCT + shopGCT)));
        sb.append("========================================================================================\n");
        return sb.toString();
    }

    /**
     * Report 3: Official & Team Regulatory Compliance Audit Report
     */
    public static String generateComplianceAuditReport(FootballLeague league) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================================\n");
        sb.append("                 KPFL REGULATORY & PERSONNEL COMPLIANCE AUDIT REPORT                    \n");
        sb.append("                           Generated: ").append(LocalDateTime.now().format(DTF)).append("\n");
        sb.append("========================================================================================\n");

        sb.append("SECTION 1: CLUB ROSTER & COACHING AUDIT\n");
        sb.append(String.format("%-12s %-25s %-10s %-8s %-20s %s\n",
                "REG NO", "CLUB NAME", "STATUS", "SQUAD", "HEAD COACH", "MIN AGE COMPLIANCE"));
        sb.append("----------------------------------------------------------------------------------------\n");

        for (FootballTeam t : league.getTeams()) {
            boolean allEligible = true;
            for (Player p : t.getPlayers()) {
                if (p.getAge() < Player.MINIMUM_LEAGUE_AGE) {
                    allEligible = false;
                    break;
                }
            }
            String coachName = (t.getHeadCoach() != null) ? t.getHeadCoach().getName() : "UNASSIGNED";
            sb.append(String.format("%-12s %-25s %-10s %-8d %-20s %s\n",
                    t.getRegistrationNumber(), t.getName(), t.getLeagueStatus(),
                    t.getSquadSize(), coachName, allEligible ? "PASS (>=16 yrs)" : "FAIL (Underage)"));
        }

        sb.append("\nSECTION 2: CERTIFIED MATCH OFFICIALS ROSTER\n");
        sb.append(String.format("%-10s %-25s %-18s %-10s %-10s\n",
                "ID", "NAME", "ROLE", "TIER RANK", "AVAILABLE"));
        sb.append("----------------------------------------------------------------------------------------\n");
        for (MatchOfficial o : league.getOfficials()) {
            sb.append(String.format("%-10s %-25s %-18s Tier %-5d %-10s\n",
                    o.getId(), o.getName(), o.getOfficiatingRole(), o.getRank(), o.isAvailable() ? "YES" : "NO"));
        }

        sb.append("\nSECTION 3: SCHEDULED FIXTURES CONFLICT AUDIT\n");
        sb.append(String.format("%-12s %-25s %-25s %-12s %-8s\n",
                "FIXTURE ID", "HOME TEAM", "AWAY TEAM", "DATE", "STATUS"));
        sb.append("----------------------------------------------------------------------------------------\n");
        for (Fixture f : league.getFixtures()) {
            sb.append(String.format("%-12s %-25s %-25s %-12s %-8s\n",
                    f.getFixtureId(), f.getHomeTeam().getName(), f.getAwayTeam().getName(),
                    f.getMatchDate().toString(), f.getStatus()));
        }
        sb.append("========================================================================================\n");
        return sb.toString();
    }
}
