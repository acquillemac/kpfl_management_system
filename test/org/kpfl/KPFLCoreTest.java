package org.kpfl;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kpfl.exception.*;
import org.kpfl.model.league.*;
import org.kpfl.model.person.*;
import org.kpfl.model.shop.*;
import org.kpfl.service.ReportService;
import org.kpfl.storage.FilePersistenceManager;

public class KPFLCoreTest {

    private FootballLeague league;
    private FootballTeam teamA;
    private FootballTeam teamB;
    private Stadium stadium;
    private MatchOfficial official;

    @BeforeEach
    void setUp() throws Exception {
        league = new FootballLeague("KPFL Test League", "2026/2027");
        teamA = new FootballTeam("TM-01", "Harbour View FC", "Kingston");
        teamB = new FootballTeam("TM-02", "Waterhouse FC", "St. Andrew");
        stadium = new Stadium("STD-01", "National Stadium", "Kingston", 5000, "EXCELLENT", 1000.00);
        official = new MatchOfficial("OFF-01", "Daneon Parchment", 40, 'M', "Referee", 1);

        league.registerTeam(teamA);
        league.registerTeam(teamB);
        league.registerStadium(stadium);
        league.registerOfficial(official);
    }

    @Test
    @DisplayName("Verify Default, Copy, and Parameterized Constructors")
    void testConstructorsAndToString() throws Exception {
        // Default constructor
        Player pDefault = new Player();
        assertEquals("", pDefault.getId());
        assertEquals("Unassigned", pDefault.getPosition());
        assertEquals('?', pDefault.getGender());

        // Primary constructor
        Player pPrimary = new Player("PLY-09", "Leon Bailey", 27, 'M', "Forward", 7);
        assertEquals("PLY-09", pPrimary.getId());
        assertEquals("Leon Bailey", pPrimary.getName());
        assertEquals(7, pPrimary.getJerseyNumber());
        assertEquals('M', pPrimary.getGender());

        // Copy constructor
        Player pCopy = new Player(pPrimary);
        assertEquals(pPrimary.getId(), pCopy.getId());
        assertEquals(pPrimary.getName(), pCopy.getName());
        assertEquals(pPrimary.getJerseyNumber(), pCopy.getJerseyNumber());
        assertEquals(pPrimary.getGender(), pCopy.getGender());

        // toString format check
        String pStr = pPrimary.toString();
        assertTrue(pStr.contains("Player ID: PLY-09"));
        assertTrue(pStr.contains("Name: Leon Bailey"));
        assertTrue(pStr.contains("Gender: M"));
        assertTrue(pStr.contains("Position: Forward"));

        // Product copy check
        Merchandise mOrig = new Merchandise("M-01", "Match Jersey", 4500.00, 20, "L", "Apparel");
        Merchandise mCopy = new Merchandise(mOrig);
        assertEquals(mOrig.getProductId(), mCopy.getProductId());
        assertEquals(mOrig.getUnitPrice(), mCopy.getUnitPrice());
        assertTrue(mOrig.toString().contains("Product ID: M-01"));
        assertTrue(mOrig.toString().contains("Unit Price: $4500.0"));

        // 6. Stadium Default, Copy, and Primary
        Stadium sCopy = new Stadium(stadium);
        assertEquals(stadium.getStadiumId(), sCopy.getStadiumId());
        assertTrue(stadium.toString().contains("Stadium ID: STD-01"));
    }

    @Test
    @DisplayName("BR-01: Ineligible/Suspended Team cannot be scheduled in a fixture")
    void testSuspendedTeamCannotBeScheduled() throws Exception {
        FootballTeam suspendedTeam = new FootballTeam("TM-SUSP", "Suspended FC", "Kingston");
        suspendedTeam.setLeagueStatus("SUSPENDED");
        league.registerTeam(suspendedTeam);

        assertThrows(TeamIneligibleException.class, () -> {
            league.scheduleFixture("FIX-FAIL", suspendedTeam, teamB, stadium,
                    LocalDate.of(2026, 5, 1), LocalTime.of(15, 0));
        });
    }

    @Test
    @DisplayName("BR-02: Team cannot be scheduled for two fixtures at the same date and time")
    void testTeamSchedulingConflict() throws Exception {
        LocalDate matchDate = LocalDate.of(2026, 5, 1);
        LocalTime matchTime = LocalTime.of(15, 0);

        league.scheduleFixture("FIX-01", teamA, teamB, stadium, matchDate, matchTime);

        FootballTeam teamC = new FootballTeam("TM-03", "Cavalier SC", "Kingston");
        league.registerTeam(teamC);

        assertThrows(SchedulingConflictException.class, () -> {
            league.scheduleFixture("FIX-CONFLICT", teamA, teamC, stadium, matchDate, matchTime);
        });
    }

    @Test
    @DisplayName("BR-03: Official cannot be assigned to conflicting fixtures")
    void testOfficialUnavailableConflict() throws Exception {
        Fixture fix = new Fixture("FIX-OFF", teamA, teamB, stadium, LocalDate.of(2026, 5, 2), LocalTime.of(16, 0));
        official.setAvailable(false);

        assertThrows(OfficialUnavailableException.class, () -> {
            fix.assignOfficial(official);
        });
    }

    @Test
    @DisplayName("BR-04: Ticket sales cannot exceed stadium seating capacity")
    void testStadiumCapacityEnforcement() throws Exception {
        Stadium smallVenue = new Stadium("STD-SMALL", "Local Park", "Kingston", 100, "GOOD", 500.00);
        smallVenue.allocateTickets(100);

        assertEquals(0, smallVenue.getAvailableSeats());
        assertThrows(CapacityExceededException.class, () -> {
            smallVenue.allocateTickets(1);
        });
    }

    @Test
    @DisplayName("BR-05: Non-negative scores and negative quantities validation")
    void testNegativeScoresAndQuantitiesRejected() {
        assertThrows(InvalidValueException.class, () -> {
            new MatchResult(-1, 2);
        });
        assertThrows(InvalidValueException.class, () -> {
            new Merchandise("PRD-INV", "Jersey", -500.00, 10, "L", "Apparel");
        });
    }

    @Test
    @DisplayName("BR-06: Players must satisfy league minimum-age rule (>= 16 years)")
    void testPlayerMinimumAgeRule() {
        assertThrows(PlayerAgeRequirementException.class, () -> {
            new Player("PLY-UNDERAGE", "Junior Star", 15, 'M', "Forward", 10);
        });

        assertDoesNotThrow(() -> {
            Player p16 = new Player("PLY-16", "Eligible Youth", 16, 'M', "Midfielder", 22);
            teamA.addPlayer(p16);
        });
    }

    @Test
    @DisplayName("BR-07: Transaction cannot be completed when payment is insufficient (15% GCT)")
    void testInsufficientPaymentThrowsException() throws Exception {
        StadiumShop shop = stadium.getStadiumShop();
        Merchandise shirt = new Merchandise("M-01", "Supporter Shirt", 2000.00, 10, "M", "Apparel");
        shop.addProduct(shirt);

        List<OrderItem> cart = new ArrayList<>();
        cart.add(new OrderItem(shirt, 1)); // Subtotal: 2000, 15% GCT: 300, Grand Total: 2300

        assertThrows(InsufficientFundsException.class, () -> {
            shop.processSale(cart, 2000.00);
        });

        SaleTransaction tx = shop.processSale(cart, 2500.00);
        assertEquals(2300.00, tx.getGrandTotal(), 0.01);
        assertEquals(200.00, tx.getChangeDue(), 0.01);
    }

    @Test
    @DisplayName("BR-08: Fixture cannot be completed without valid scores")
    void testFixtureCompletionRule() throws Exception {
        Fixture fix = new Fixture("FIX-COMP", teamA, teamB, stadium, LocalDate.of(2026, 5, 10), LocalTime.of(15, 0));
        assertEquals("SCHEDULED", fix.getStatus());

        fix.recordResultAndComplete(2, 1);
        assertEquals("COMPLETED", fix.getStatus());
        assertEquals("2 - 1 (HOME_WIN)", fix.getMatchResult().toString().trim());

        assertThrows(InvalidFixtureStateException.class, () -> {
            fix.recordResultAndComplete(1, 1);
        });
    }

    @Test
    @DisplayName("Practice Session Booking Verification")
    void testStadiumPracticeSession() throws Exception {
        stadium.schedulePractice(teamA, "2026-04-10 09:00 AM");
        assertEquals(1, stadium.getPracticeSessions().size());
        assertTrue(stadium.getPracticeSessions().get(0).contains("Harbour View FC"));
    }

    @Test
    @DisplayName("Search Operations by Parish and Name")
    void testSearchOperations() {
        List<FootballTeam> kingstonClubs = league.searchTeamsByParish("Kingston");
        assertEquals(1, kingstonClubs.size());

        List<FootballTeam> waterhouseSearch = league.searchTeamsByName("water");
        assertEquals(1, waterhouseSearch.size());
    }

    @Test
    @DisplayName("Management Reports Generation Verification")
    void testReportsGeneration() {
        String standingsReport = ReportService.generateLeagueStandingsReport(league);
        String financialReport = ReportService.generateFinancialAndAttendanceReport(league);
        String auditReport = ReportService.generateComplianceAuditReport(league);

        assertNotNull(standingsReport);
        assertNotNull(financialReport);
        assertNotNull(auditReport);
        assertTrue(standingsReport.contains("KINGSTON PREMIER FOOTBALL LEAGUE"));
        assertTrue(financialReport.contains("15% GCT"));
        assertTrue(auditReport.contains("MIN AGE COMPLIANCE"));
    }

    @Test
    @DisplayName("Persistence: Save and Reload Round-Trip")
    void testPersistenceRoundTrip() throws Exception {
        String testDir = "./data_test_temp";
        FilePersistenceManager.saveAllData(league, testDir);

        FootballLeague reloadedLeague = new FootballLeague("Reloaded KPFL", "2026/2027");
        FilePersistenceManager.loadAllData(reloadedLeague, testDir);

        assertEquals(2, reloadedLeague.getTeams().size());
        assertEquals(1, reloadedLeague.getStadiums().size());

        File dir = new File(testDir);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) f.delete();
        }
        dir.delete();
    }
}
