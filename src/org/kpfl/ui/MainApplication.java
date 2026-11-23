package org.kpfl.ui;

import java.util.Scanner;
import org.kpfl.model.league.FootballLeague;
import org.kpfl.model.person.LeagueCommissioner;
import org.kpfl.service.ReportService;
import org.kpfl.storage.FilePersistenceManager;

/**
 * Main application launcher for the Kingston Premier Football League System.
 * Supports both interactive live console input and automated demonstration mode.
 */
public class MainApplication {

    private static final String DATA_DIR = "./data";

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("   KINGSTON PREMIER FOOTBALL LEAGUE (KPFL) SYSTEM");
        System.out.println("                 Eclipse & Java OOP Project");
        System.out.println("=============================================================\n");

        FootballLeague league = new FootballLeague("Kingston Premier Football League", "2026/2027");
        try {
            LeagueCommissioner commissioner = new LeagueCommissioner("COMM-01", "Hon. Michael Stewart", 52, "Male", "JFF-KPFL-ACC-001");
            league.setCommissioner(commissioner);

            // Automatically load prepared sample data from files
            System.out.println("[INIT] Loading baseline league data from " + DATA_DIR + "...");
            FilePersistenceManager.loadAllData(league, DATA_DIR);
            System.out.printf("[INIT] Successfully loaded: %d Teams, %d Stadiums, %d Fixtures.\n\n",
                    league.getTeams().size(), league.getStadiums().size(), league.getFixtures().size());

            Scanner scanner = new Scanner(System.in);
            System.out.println("Select operational mode:");
            System.out.println("1. Launch Interactive Management Console (Live Input)");
            System.out.println("2. Run Automated Verification Demonstration (All Reports & POS)");
            System.out.print("Enter choice (1 or 2): ");

            String mode = scanner.nextLine().trim();
            if ("2".equals(mode)) {
                runAutomatedDemonstration(league);
            } else {
                MenuHandler menuHandler = new MenuHandler(league, scanner);
                menuHandler.start();
            }

        } catch (Exception e) {
            System.err.println("Critical System Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runAutomatedDemonstration(FootballLeague league) {
        System.out.println("\n---------------- RUNNING AUTOMATED DEMO ----------------");
        System.out.println(ReportService.generateLeagueStandingsReport(league));
        System.out.println(ReportService.generateFinancialAndAttendanceReport(league));
        System.out.println(ReportService.generateComplianceAuditReport(league));
        System.out.println("---------------- DEMONSTRATION COMPLETE ----------------");
    }
}
