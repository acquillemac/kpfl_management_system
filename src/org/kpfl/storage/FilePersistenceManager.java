package org.kpfl.storage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.kpfl.model.league.*;
import org.kpfl.model.person.Player;
import org.kpfl.model.shop.FoodItem;
import org.kpfl.model.shop.Merchandise;
import org.kpfl.model.shop.Product;

/**
 * Handles persistent sequential text/data file operations for the KPFL system.
 */
public class FilePersistenceManager {

    /**
     * Saves all league and stadium shop data to designated text files.
     */
    public static void saveAllData(FootballLeague league, String dataDir) throws IOException {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        saveTeams(league.getTeams(), new File(dir, "teams.txt"));
        savePlayers(league.getTeams(), new File(dir, "players.txt"));
        saveStadiums(league.getStadiums(), new File(dir, "stadiums.txt"));
        saveFixtures(league.getFixtures(), new File(dir, "fixtures.txt"));

        // Save products from stadiums
        if (!league.getStadiums().isEmpty()) {
            Stadium primaryStadium = league.getStadiums().get(0);
            saveProducts(primaryStadium.getStadiumShop().getInventory(), new File(dir, "products.txt"));
        }
    }

    private static void saveTeams(List<FootballTeam> teams, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (FootballTeam t : teams) {
                // RegistrationNumber|Name|Parish|SquadSize|Status|MatchesPlayed|Wins|Draws|Losses|GoalsFor|GoalsAgainst|Points
                bw.write(String.format("%s|%s|%s|%d|%s|%d|%d|%d|%d|%d|%d|%d\n",
                        t.getRegistrationNumber(), t.getName(), t.getParish(), t.getSquadSize(),
                        t.getLeagueStatus(), t.getMatchesPlayed(), t.getWins(), t.getDraws(),
                        t.getLosses(), t.getGoalsFor(), t.getGoalsAgainst(), t.getPoints()));
            }
        }
    }

    private static void savePlayers(List<FootballTeam> teams, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (FootballTeam t : teams) {
                for (Player p : t.getPlayers()) {
                    // PersonId|Name|Age|Gender|TeamRegNo|Position|JerseyNumber|GoalsScored|PlayingStatus
                    bw.write(String.format("%s|%s|%d|%s|%s|%s|%d|%d|%s\n",
                            p.getId(), p.getName(), p.getAge(), p.getGender(),
                            t.getRegistrationNumber(), p.getPosition(), p.getJerseyNumber(),
                            p.getGoalsScored(), p.getPlayingStatus()));
                }
            }
        }
    }

    private static void saveStadiums(List<Stadium> stadiums, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Stadium s : stadiums) {
                // StadiumId|Name|Location|SeatingCapacity|TicketsSold|FieldCondition|BaseTicketPrice
                bw.write(String.format("%s|%s|%s|%d|%d|%s|%.2f\n",
                        s.getStadiumId(), s.getName(), s.getLocation(),
                        s.getSeatingCapacity(), s.getTicketsSold(), s.getFieldCondition(), s.getBaseTicketPrice()));
            }
        }
    }

    private static void saveFixtures(List<Fixture> fixtures, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Fixture f : fixtures) {
                int hs = (f.getMatchResult() != null) ? f.getMatchResult().getHomeScore() : 0;
                int as = (f.getMatchResult() != null) ? f.getMatchResult().getAwayScore() : 0;
                // FixtureId|HomeTeamRegNo|AwayTeamRegNo|StadiumId|Date|Time|Status|HomeScore|AwayScore
                bw.write(String.format("%s|%s|%s|%s|%s|%s|%s|%d|%d\n",
                        f.getFixtureId(), f.getHomeTeam().getRegistrationNumber(),
                        f.getAwayTeam().getRegistrationNumber(), f.getVenue().getStadiumId(),
                        f.getMatchDate().toString(), f.getMatchTime().toString(),
                        f.getStatus(), hs, as));
            }
        }
    }

    private static void saveProducts(List<Product> products, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Product p : products) {
                if (p instanceof Merchandise) {
                    Merchandise m = (Merchandise) p;
                    // ProductId|Type|Name|Price|Stock|Size|Category
                    bw.write(String.format("%s|MERCHANDISE|%s|%.2f|%d|%s|%s\n",
                            m.getProductId(), m.getName(), m.getUnitPrice(), m.getStockQuantity(),
                            m.getSize(), m.getCategory()));
                } else if (p instanceof FoodItem) {
                    FoodItem f = (FoodItem) p;
                    // ProductId|Type|Name|Price|Stock|Expiry|IsVegetarian
                    bw.write(String.format("%s|FOOD|%s|%.2f|%d|%s|%b\n",
                            f.getProductId(), f.getName(), f.getUnitPrice(), f.getStockQuantity(),
                            f.getExpirationDate(), f.isVegetarian()));
                }
            }
        }
    }

    /**
     * Loads system state from sequential files in dataDir into the provided league.
     */
    public static void loadAllData(FootballLeague league, String dataDir) {
        File dir = new File(dataDir);
        if (!dir.exists()) {
            System.out.println("[STORAGE] Data directory does not exist: " + dataDir);
            return;
        }

        loadStadiums(league, new File(dir, "stadiums.txt"));
        loadTeams(league, new File(dir, "teams.txt"));
        loadPlayers(league, new File(dir, "players.txt"));
        loadFixtures(league, new File(dir, "fixtures.txt"));

        if (!league.getStadiums().isEmpty()) {
            loadProducts(league.getStadiums().get(0).getStadiumShop(), new File(dir, "products.txt"));
        }
    }

    private static void loadStadiums(FootballLeague league, File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    try {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        String loc = parts[2].trim();
                        int cap = Integer.parseInt(parts[3].trim());
                        int sold = Integer.parseInt(parts[4].trim());
                        String cond = parts[5].trim();
                        double price = Double.parseDouble(parts[6].trim());

                        Stadium stadium = new Stadium(id, name, loc, cap, cond, price);
                        if (sold > 0 && sold <= cap) {
                            stadium.allocateTickets(sold);
                        }
                        league.registerStadium(stadium);
                    } catch (Exception ex) {
                        System.err.println("[STORAGE WARNING] Skipping corrupt stadium record: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[STORAGE ERROR] Failed reading stadiums file: " + e.getMessage());
        }
    }

    private static void loadTeams(FootballLeague league, File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 12) {
                    try {
                        String reg = parts[0].trim();
                        String name = parts[1].trim();
                        String parish = parts[2].trim();
                        String status = parts[4].trim();
                        int played = Integer.parseInt(parts[5].trim());
                        int wins = Integer.parseInt(parts[6].trim());
                        int draws = Integer.parseInt(parts[7].trim());
                        int losses = Integer.parseInt(parts[8].trim());
                        int gf = Integer.parseInt(parts[9].trim());
                        int ga = Integer.parseInt(parts[10].trim());
                        int pts = Integer.parseInt(parts[11].trim());

                        FootballTeam team = new FootballTeam(reg, name, parish);
                        team.setLeagueStatus(status);
                        team.setManualStats(played, wins, draws, losses, gf, ga, pts);
                        league.registerTeam(team);
                    } catch (Exception ex) {
                        System.err.println("[STORAGE WARNING] Skipping corrupt team record: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[STORAGE ERROR] Failed reading teams file: " + e.getMessage());
        }
    }

    private static void loadPlayers(FootballLeague league, File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 9) {
                    try {
                        String id = parts[0].trim();
                        String name = parts[1].trim();
                        int age = Integer.parseInt(parts[2].trim());
                        String gender = parts[3].trim();
                        String teamReg = parts[4].trim();
                        String position = parts[5].trim();
                        int jersey = Integer.parseInt(parts[6].trim());
                        int goals = Integer.parseInt(parts[7].trim());
                        String status = parts[8].trim();

                        FootballTeam team = league.findTeamByRegistration(teamReg);
                        if (team != null) {
                            Player player = new Player(id, name, age, gender, position, jersey);
                            player.setGoalsScored(goals);
                            player.setPlayingStatus(status);
                            team.addPlayer(player);
                        }
                    } catch (Exception ex) {
                        System.err.println("[STORAGE WARNING] Skipping corrupt player record: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[STORAGE ERROR] Failed reading players file: " + e.getMessage());
        }
    }

    private static void loadFixtures(FootballLeague league, File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 9) {
                    try {
                        String fixId = parts[0].trim();
                        String homeReg = parts[1].trim();
                        String awayReg = parts[2].trim();
                        String stdId = parts[3].trim();
                        LocalDate date = LocalDate.parse(parts[4].trim());
                        LocalTime time = LocalTime.parse(parts[5].trim());
                        String status = parts[6].trim();
                        int homeScore = Integer.parseInt(parts[7].trim());
                        int awayScore = Integer.parseInt(parts[8].trim());

                        FootballTeam home = league.findTeamByRegistration(homeReg);
                        FootballTeam away = league.findTeamByRegistration(awayReg);
                        Stadium venue = league.findStadiumById(stdId);

                        if (home != null && away != null && venue != null) {
                            league.scheduleFixture(fixId, home, away, venue, date, time);
                            Fixture f = league.findFixtureById(fixId);
                            if (f != null && "COMPLETED".equalsIgnoreCase(status)) {
                                f.recordResultAndComplete(homeScore, awayScore);
                            }
                        }
                    } catch (Exception ex) {
                        System.err.println("[STORAGE WARNING] Skipping corrupt fixture record: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[STORAGE ERROR] Failed reading fixtures file: " + e.getMessage());
        }
    }

    private static void loadProducts(org.kpfl.model.shop.StadiumShop shop, File file) {
        if (!file.exists() || shop == null) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    try {
                        String id = parts[0].trim();
                        String type = parts[1].trim();
                        String name = parts[2].trim();
                        double price = Double.parseDouble(parts[3].trim());
                        int stock = Integer.parseInt(parts[4].trim());

                        if ("MERCHANDISE".equalsIgnoreCase(type)) {
                            String size = parts[5].trim();
                            String category = parts[6].trim();
                            shop.addProduct(new Merchandise(id, name, price, stock, size, category));
                        } else if ("FOOD".equalsIgnoreCase(type)) {
                            String expiry = parts[5].trim();
                            boolean isVeg = Boolean.parseBoolean(parts[6].trim());
                            shop.addProduct(new FoodItem(id, name, price, stock, expiry, isVeg));
                        }
                    } catch (Exception ex) {
                        System.err.println("[STORAGE WARNING] Skipping corrupt product record: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[STORAGE ERROR] Failed reading products file: " + e.getMessage());
        }
    }
}
