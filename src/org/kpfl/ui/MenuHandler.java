package org.kpfl.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.kpfl.exception.*;
import org.kpfl.model.league.*;
import org.kpfl.model.person.*;
import org.kpfl.model.shop.*;
import org.kpfl.service.CsvExportService;
import org.kpfl.service.ReportService;
import org.kpfl.storage.FilePersistenceManager;

public class MenuHandler {

    private FootballLeague league;
    private Scanner scanner;
    private static final String DATA_DIR = "./data";

    public MenuHandler(FootballLeague league, Scanner scanner) {
        this.league = league;
        this.scanner = scanner;
    }

    public void start() {
        int choice = -1;
        do {
            printMainMenu();
            choice = getIntInput("Enter your choice (0 - 6): ");

            switch (choice) {
                case 1:
                    handleTeamSubMenu();
                    break;
                case 2:
                    handleStadiumSubMenu();
                    break;
                case 3:
                    handleFixtureSubMenu();
                    break;
                case 4:
                    handleMatchDaySubMenu();
                    break;
                case 5:
                    handleReportsSubMenu();
                    break;
                case 6:
                    handlePersistenceSubMenu();
                    break;
                case 0:
                    System.out.println("\nExiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid selection! Please choose an option between 0 and 6.");
            }
        } while (choice != 0);
    }

    private void printMainMenu() {
        System.out.println("\n=============================================================");
        System.out.println("  KINGSTON PREMIER FOOTBALL LEAGUE (KPFL) MANAGEMENT SYSTEM  ");
        System.out.println("                         Main Menu                           ");
        System.out.println("=============================================================");
        System.out.println("1. Teams & Personnel Management");
        System.out.println("2. Stadium & Venue Operations");
        System.out.println("3. Fixture Scheduling & Match Operations");
        System.out.println("4. Match-Day Services (Ticketing & Stadium Shop)");
        System.out.println("5. Management Reports & CSV Analytics");
        System.out.println("6. Persistent Storage (File I/O)");
        System.out.println("0. Exit Application");
        System.out.println("=============================================================");
    }

    private void handleTeamSubMenu() {
        int choice = -1;
        do {
            System.out.println("\n--- SUB-MENU: TEAMS & PERSONNEL ---");
            System.out.println("1. Register New Football Club");
            System.out.println("2. Add Player to Club Roster (Min. Age >= 16)");
            System.out.println("3. Assign Head Coach to Club");
            System.out.println("4. Search Clubs (by Parish or Club Name)");
            System.out.println("5. View All Clubs and Full Rosters");
            System.out.println("0. Return to Main Menu");

            choice = getIntInput("Select an option (0 - 5): ");
            try {
                switch (choice) {
                    case 1:
                        String reg = getStringInput("Enter Club Registration Number (e.g. KPFL-TM-06): ");
                        String name = getStringInput("Enter Club Name: ");
                        String parish = getStringInput("Enter Home Parish (e.g. Kingston, St. Andrew): ");
                        FootballTeam team = new FootballTeam(reg, name, parish);
                        league.registerTeam(team);
                        System.out.println("SUCCESS: Registered " + team.getName());
                        break;
                    case 2:
                        String tReg = getStringInput("Enter Club Registration Number: ");
                        FootballTeam target = league.findTeamByRegistration(tReg);
                        if (target == null) {
                            System.out.println("ERROR: Club not found.");
                            break;
                        }
                        String pid = getStringInput("Enter Player ID (e.g. PLY-601): ");
                        String pName = getStringInput("Enter Player Full Name: ");
                        int age = getIntInput("Enter Player Age (League rule requires >= 16): ");
                        String gender = getStringInput("Enter Gender (Male/Female): ");
                        String pos = getStringInput("Enter Position (Forward, Midfielder, Defender, Goalkeeper): ");
                        int jersey = getIntInput("Enter Jersey Number (1 - 99): ");

                        Player player = new Player(pid, pName, age, gender, pos, jersey);
                        target.addPlayer(player);
                        System.out.println("SUCCESS: Added player " + player.getName() + " to " + target.getName());
                        break;
                    case 3:
                        String cReg = getStringInput("Enter Club Registration Number: ");
                        FootballTeam cTeam = league.findTeamByRegistration(cReg);
                        if (cTeam == null) {
                            System.out.println("ERROR: Club not found.");
                            break;
                        }
                        String cid = getStringInput("Enter Coach ID (e.g. CCH-05): ");
                        String cName = getStringInput("Enter Coach Name: ");
                        int cAge = getIntInput("Enter Coach Age: ");
                        String cGen = getStringInput("Enter Gender: ");
                        String qual = getStringInput("Enter Qualification (e.g. CONCACAF A, FIFA Pro): ");
                        Coach coach = new Coach(cid, cName, cAge, cGen, qual, "Head Coach");
                        cTeam.setHeadCoach(coach);
                        System.out.println("SUCCESS: Assigned Coach " + cName + " to " + cTeam.getName());
                        break;
                    case 4:
                        int sType = getIntInput("Search by: (1) Parish or (2) Club Name: ");
                        if (sType == 1) {
                            String pQuery = getStringInput("Enter Parish Name: ");
                            List<FootballTeam> pList = league.searchTeamsByParish(pQuery);
                            System.out.println("Found " + pList.size() + " matching club(s):");
                            for (FootballTeam t : pList) System.out.println("  " + t);
                        } else {
                            String nQuery = getStringInput("Enter Search Keyword: ");
                            List<FootballTeam> nList = league.searchTeamsByName(nQuery);
                            System.out.println("Found " + nList.size() + " matching club(s):");
                            for (FootballTeam t : nList) System.out.println("  " + t);
                        }
                        break;
                    case 5:
                        System.out.println("\n--- ALL REGISTERED CLUBS & SQUADS ---");
                        for (FootballTeam t : league.getTeams()) {
                            t.display();
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.err.println("OPERATION FAILED: " + e.getMessage());
            }
            if (choice != 0) pressEnterToContinue();
        } while (choice != 0);
    }

    private void handleStadiumSubMenu() {
        int choice = -1;
        do {
            System.out.println("\n--- SUB-MENU: STADIUM & VENUE OPERATIONS ---");
            System.out.println("1. Register New Stadium");
            System.out.println("2. View All Stadiums & Capacity Details");
            System.out.println("3. Book Pre-Match Practice Session for a Club");
            System.out.println("0. Return to Main Menu");

            choice = getIntInput("Select an option (0 - 3): ");
            try {
                switch (choice) {
                    case 1:
                        String sid = getStringInput("Enter Stadium ID (e.g. STD-06): ");
                        String sname = getStringInput("Enter Stadium Name: ");
                        String sloc = getStringInput("Enter Location/Parish: ");
                        int cap = getIntInput("Enter Seating Capacity: ");
                        String cond = getStringInput("Enter Field Condition (EXCELLENT, GOOD, FAIR): ");
                        double price = getDoubleInput("Enter Base Ticket Price ($): ");

                        Stadium stadium = new Stadium(sid, sname, sloc, cap, cond, price);
                        league.registerStadium(stadium);
                        System.out.println("SUCCESS: Registered " + stadium.getName());
                        break;
                    case 2:
                        System.out.println("\n--- APPROVED KPFL VENUES ---");
                        for (Stadium s : league.getStadiums()) {
                            s.display();
                            for (String ps : s.getPracticeSessions()) {
                                System.out.println("    [PRACTICE BOOKING] " + ps);
                            }
                        }
                        break;
                    case 3:
                        String tReg = getStringInput("Enter Club Registration Number: ");
                        String sTarget = getStringInput("Enter Stadium ID: ");
                        String sessionTime = getStringInput("Enter Requested Date & Time (e.g. 2026-04-18 09:00 AM): ");
                        league.requestStadiumPractice(tReg, sTarget, sessionTime);
                        System.out.println("SUCCESS: Pre-match practice session approved and recorded.");
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.err.println("OPERATION FAILED: " + e.getMessage());
            }
            if (choice != 0) pressEnterToContinue();
        } while (choice != 0);
    }

    private void handleFixtureSubMenu() {
        int choice = -1;
        do {
            System.out.println("\n--- SUB-MENU: FIXTURES & MATCH RESULTS ---");
            System.out.println("1. Schedule New Fixture (with Conflict Validation)");
            System.out.println("2. Record Final Match Score & Update Standings");
            System.out.println("3. View All Fixtures");
            System.out.println("0. Return to Main Menu");

            choice = getIntInput("Select an option (0 - 3): ");
            try {
                switch (choice) {
                    case 1:
                        String fid = getStringInput("Enter Fixture ID (e.g. FIX-05): ");
                        String homeReg = getStringInput("Enter Home Club Registration Number: ");
                        String awayReg = getStringInput("Enter Away Club Registration Number: ");
                        String sid = getStringInput("Enter Stadium Venue ID: ");
                        String dateStr = getStringInput("Enter Match Date (YYYY-MM-DD): ");
                        String timeStr = getStringInput("Enter Kick-off Time (HH:MM): ");

                        FootballTeam home = league.findTeamByRegistration(homeReg);
                        FootballTeam away = league.findTeamByRegistration(awayReg);
                        Stadium venue = league.findStadiumById(sid);

                        if (home == null || away == null || venue == null) {
                            System.out.println("ERROR: Specified clubs or venue do not exist.");
                            break;
                        }

                        league.scheduleFixture(fid, home, away, venue,
                                LocalDate.parse(dateStr), LocalTime.parse(timeStr));
                        System.out.println("SUCCESS: Scheduled fixture " + fid);
                        break;
                    case 2:
                        String fixId = getStringInput("Enter Fixture ID to Finalize: ");
                        Fixture fix = league.findFixtureById(fixId);
                        if (fix == null) {
                            System.out.println("ERROR: Fixture not found.");
                            break;
                        }
                        System.out.printf("Recording score for: %s vs %s\n", fix.getHomeTeam().getName(), fix.getAwayTeam().getName());
                        int hGoals = getIntInput("Enter Home Club Goals: ");
                        int aGoals = getIntInput("Enter Away Club Goals: ");

                        fix.recordResultAndComplete(hGoals, aGoals);
                        System.out.println("SUCCESS: Result recorded! Points and standings updated.");
                        break;
                    case 3:
                        System.out.println("\n--- KPFL FIXTURE LIST ---");
                        for (Fixture f : league.getFixtures()) {
                            f.display();
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.err.println("OPERATION FAILED: " + e.getMessage());
            }
            if (choice != 0) pressEnterToContinue();
        } while (choice != 0);
    }

    private void handleMatchDaySubMenu() {
        int choice = -1;
        do {
            System.out.println("\n--- SUB-MENU: MATCH-DAY TICKETING & SHOP POS ---");
            System.out.println("1. Purchase Match Tickets (Capacity-Checked, 15% GCT)");
            System.out.println("2. View Stadium Shop Inventory");
            System.out.println("3. Purchase Concessions & Merchandise (with Receipt & Change)");
            System.out.println("0. Return to Main Menu");

            choice = getIntInput("Select an option (0 - 3): ");
            try {
                switch (choice) {
                    case 1:
                        String sid = getStringInput("Enter Stadium ID: ");
                        Stadium s = league.findStadiumById(sid);
                        if (s == null) {
                            System.out.println("ERROR: Stadium not found.");
                            break;
                        }
                        System.out.printf("Venue: %s | Capacity: %d | Available: %d | Ticket Base: $%.2f\n",
                                s.getName(), s.getSeatingCapacity(), s.getAvailableSeats(), s.getBaseTicketPrice());
                        int numTickets = getIntInput("Enter Number of Tickets to Purchase: ");

                        s.allocateTickets(numTickets);
                        double subtotal = numTickets * s.getBaseTicketPrice();
                        double gct = subtotal * 0.15;
                        double total = subtotal + gct;

                        System.out.printf("Ticket Order Summary:\n  Subtotal:     $%10.2f\n  GCT (15%%):    $%10.2f\n  TOTAL DUE:    $%10.2f\n",
                                subtotal, gct, total);
                        double cash = getDoubleInput("Enter Amount Tendered ($): ");

                        if (cash < total) {
                            throw new InsufficientFundsException("Insufficient payment tendered. Transaction cancelled.");
                        }
                        System.out.printf("SUCCESS: Tickets issued! Change: $%.2f\n", (cash - total));
                        break;
                    case 2:
                        if (league.getStadiums().isEmpty()) {
                            System.out.println("No stadiums registered.");
                            break;
                        }
                        StadiumShop shop = league.getStadiums().get(0).getStadiumShop();
                        System.out.println("\n--- SHOP INVENTORY: " + shop.getStadiumName() + " ---");
                        for (Product p : shop.getInventory()) {
                            p.display();
                        }
                        break;
                    case 3:
                        if (league.getStadiums().isEmpty()) {
                            System.out.println("No stadiums registered.");
                            break;
                        }
                        StadiumShop posShop = league.getStadiums().get(0).getStadiumShop();
                        List<OrderItem> cart = new ArrayList<>();
                        boolean shopping = true;

                        while (shopping) {
                            String pid = getStringInput("Enter Product ID to buy (or 'DONE'): ");
                            if ("DONE".equalsIgnoreCase(pid)) break;
                            Product product = posShop.findProductById(pid);
                            if (product == null) {
                                System.out.println("Product not found.");
                                continue;
                            }
                            System.out.printf("Selected: %s | Price: $%.2f | Stock: %d\n",
                                    product.getName(), product.getUnitPrice(), product.getStockQuantity());
                            int qty = getIntInput("Enter Quantity: ");
                            cart.add(new OrderItem(product, qty));

                            String more = getStringInput("Add another item? (Y/N): ");
                            if (!"Y".equalsIgnoreCase(more)) shopping = false;
                        }

                        if (cart.isEmpty()) {
                            System.out.println("Cart is empty.");
                            break;
                        }

                        double cartSub = 0.0;
                        for (OrderItem oi : cart) cartSub += oi.getSubtotal();
                        double cartTax = cartSub * 0.15;
                        double grandTotal = cartSub + cartTax;

                        System.out.printf("\nCart Grand Total (inc. 15%% GCT): $%.2f\n", grandTotal);
                        double payment = getDoubleInput("Enter Cash Tendered ($): ");

                        SaleTransaction tx = posShop.processSale(cart, payment);
                        System.out.println("\n" + tx.generateReceipt());
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.err.println("TRANSACTION FAILED: " + e.getMessage());
            }
            if (choice != 0) pressEnterToContinue();
        } while (choice != 0);
    }

    private void handleReportsSubMenu() {
        int choice = -1;
        do {
            System.out.println("\n--- SUB-MENU: REPORTS & ANALYTICS ---");
            System.out.println("1. Official KPFL Standings Table");
            System.out.println("2. Match-Day Financial & Attendance Audit");
            System.out.println("3. Regulatory & Personnel Compliance Audit");
            System.out.println("4. Export All Reports to CSV Files (Optional Extension)");
            System.out.println("0. Return to Main Menu");

            choice = getIntInput("Select an option (0 - 4): ");
            try {
                switch (choice) {
                    case 1:
                        System.out.println(ReportService.generateLeagueStandingsReport(league));
                        break;
                    case 2:
                        System.out.println(ReportService.generateFinancialAndAttendanceReport(league));
                        break;
                    case 3:
                        System.out.println(ReportService.generateComplianceAuditReport(league));
                        break;
                    case 4:
                        String exportDir = "./data/reports";
                        java.io.File f1 = CsvExportService.exportStandingsCsv(league, exportDir);
                        java.io.File f2 = CsvExportService.exportFinancialAuditCsv(league, exportDir);
                        java.io.File f3 = CsvExportService.exportComplianceAuditCsv(league, exportDir);
                        System.out.println("SUCCESS: Exported CSV reports to " + exportDir);
                        System.out.println("  1. " + f1.getName());
                        System.out.println("  2. " + f2.getName());
                        System.out.println("  3. " + f3.getName());
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.err.println("REPORT GENERATION FAILED: " + e.getMessage());
            }
            if (choice != 0) pressEnterToContinue();
        } while (choice != 0);
    }

    private void handlePersistenceSubMenu() {
        int choice = -1;
        do {
            System.out.println("\n--- SUB-MENU: PERSISTENT STORAGE ---");
            System.out.println("1. Save Current League State to Sequential Files");
            System.out.println("2. Reload League State from Sequential Files");
            System.out.println("0. Return to Main Menu");

            choice = getIntInput("Select an option (0 - 2): ");
            try {
                switch (choice) {
                    case 1:
                        FilePersistenceManager.saveAllData(league, DATA_DIR);
                        System.out.println("SUCCESS: Saved all records to " + DATA_DIR);
                        break;
                    case 2:
                        FilePersistenceManager.loadAllData(league, DATA_DIR);
                        System.out.println("SUCCESS: Reloaded all records from " + DATA_DIR);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.err.println("STORAGE FAILED: " + e.getMessage());
            }
            if (choice != 0) pressEnterToContinue();
        } while (choice != 0);
    }

    // Helper methods for reading inputs
    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer! Please enter a valid number.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric amount! Please enter a valid number (e.g. 1500.00).");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private void pressEnterToContinue() {
        System.out.print("\nPress [Enter] to continue...");
        scanner.nextLine();
    }
}
