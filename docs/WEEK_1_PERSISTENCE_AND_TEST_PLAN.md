# KPFL Management System: Week 1 Deliverable
## Part 3: Data Persistence Approach, Validation Strategy & Test Plan

---

## 1. Data Persistence Architecture

To satisfy course guidelines requiring sequential access files without external database overhead, the system implements a robust **File I/O Manager** using standard Java I/O (`BufferedReader`, `BufferedWriter`, `Scanner`, and `FileWriter`).

### 1.1 File Storage Schema & Delimiter Standard
All sequential files are stored in the `./data/` folder and use the pipe character (`|`) as a delimiter. This avoids commas clashing with descriptive text or team names.

#### 1. `data/teams.txt`
* **Format:** `RegistrationNumber|Name|Parish|SquadSize|Status|MatchesPlayed|Wins|Draws|Losses|GoalsFor|GoalsAgainst|Points`
* **Sample Record:** `KPFL-TM-01|Harbour View FC|Kingston|22|ACTIVE|5|3|1|1|8|4|10`

#### 2. `data/players.txt`
* **Format:** `PersonId|Name|Age|Gender|TeamRegNo|Position|JerseyNumber|GoalsScored|PlayingStatus`
* **Sample Record:** `P-101|Andre Blake|33|Male|KPFL-TM-01|Goalkeeper|1|0|ACTIVE`

#### 3. `data/stadiums.txt`
* **Format:** `StadiumId|Name|Location|SeatingCapacity|TicketsSold|FieldCondition|BaseTicketPrice`
* **Sample Record:** `STD-01|National Stadium|Independence Park, Kingston|35000|0|EXCELLENT|1500.00`

#### 4. `data/fixtures.txt`
* **Format:** `FixtureId|HomeTeamRegNo|AwayTeamRegNo|StadiumId|Date(YYYY-MM-DD)|Time(HH:MM)|Status|HomeScore|AwayScore|LeadOfficialId`
* **Sample Record:** `FIX-2026-01|KPFL-TM-01|KPFL-TM-02|STD-01|2026-04-12|16:00|COMPLETED|2|1|OFF-01`

#### 5. `data/products.txt`
* **Format:** `ProductId|ItemType(MERCHANDISE/FOOD)|Name|UnitPrice|StockQuantity|Detail1|Detail2`
* **Sample Records:**
  * `PRD-M01|MERCHANDISE|Official Home Jersey|4500.00|150|L|Apparel`
  * `PRD-F01|FOOD|Tastee Beef Patty|350.00|300|2026-04-15|false`

#### 6. `data/transactions.txt`
* **Format:** `TransactionId|Timestamp|Subtotal|GCT(15%)|GrandTotal|AmountTendered|Change`
* **Sample Record:** `TX-9001|2026-04-12T17:30:00|5200.00|780.00|5980.00|6000.00|20.00`

---

## 2. Validation & Defensive Design Strategy

The system enforces validation at two distinct architectural levels:
1. **Model-Level (Encapsulation Guardrails):**
   * Setters and constructors strictly enforce domain boundaries.
   * Invalid primitive arguments (e.g., negative score, empty ID, negative stock) immediately trigger an `InvalidValueException`.
2. **Service-Level (Business Rules Engine):**
   * Pre-execution checks verify domain state before executing actions (e.g., checking if stadium capacity is exceeded or if an official is double-booked).

```
[ UI / Controller ]
         |
         v
[ Service Layer: Business Rule Checks ] ---> Fails? Throws Business Exception (e.g. SchedulingConflictException)
         | Pass
         v
[ Domain Entity: Setter Validation ]    ---> Fails? Throws InvalidValueException
         | Pass
         v
[ Storage Layer: Sequential File Write ]
```

---

## 3. Comprehensive Initial Test Plan

The test plan targets **Normal**, **Boundary**, and **Invalid (Error)** conditions across all major modules.

### Category A: Core Personnel & Team Management

| Test ID | Test Case Description | Input Data | Expected Output / Exception | Status |
|---|---|---|---|---|
| **TC-01** | Add eligible player (Normal) | Age = 22, Jersey = 10, Position = "Forward" | Player added successfully to team roster | Planned |
| **TC-02** | Add underage player (Boundary/Invalid) | Age = 15 (Minimum age is 16) | Throws `PlayerAgeRequirementException` | Planned |
| **TC-03** | Add exact minimum age player (Boundary) | Age = 16 | Player added successfully | Planned |
| **TC-04** | Duplicate jersey number on same team | Player 1: Jersey 7; Player 2: Jersey 7 | Throws `DuplicateJerseyException` | Planned |
| **TC-05** | Negative player age or jersey | Age = -5 or Jersey = -1 | Throws `InvalidValueException` | Planned |

### Category B: Fixtures & League Scheduling

| Test ID | Test Case Description | Input Data | Expected Output / Exception | Status |
|---|---|---|---|---|
| **TC-06** | Schedule valid fixture (Normal) | Registered Team A vs Team B, Date: 2026-04-12 15:00 | Fixture scheduled with status `SCHEDULED` | Planned |
| **TC-07** | Team double-booking conflict (Invalid) | Team A scheduled for Match 1 & Match 2 at same date/time | Throws `SchedulingConflictException` | Planned |
| **TC-08** | Referee double-booking conflict (Invalid) | Referee 1 assigned to two distinct matches at same date/time | Throws `OfficialUnavailableException` | Planned |
| **TC-09** | Assign unregistered / suspended team | Team status = `SUSPENDED` | Throws `TeamIneligibleException` | Planned |
| **TC-10** | Fixture with same team as home & away | Home = Team A, Away = Team A | Throws `IllegalArgumentException` | Planned |

### Category C: Match Results & Standings

| Test ID | Test Case Description | Input Data | Expected Output / Exception | Status |
|---|---|---|---|---|
| **TC-11** | Home Team Wins (Normal) | Home Score: 3, Away Score: 1 | Home +3 pts, +1 win; Away +0 pts, +1 loss | Planned |
| **TC-12** | Match Draw (Normal) | Home Score: 2, Away Score: 2 | Both teams +1 pt, +1 draw; Goal Diff unchanged | Planned |
| **TC-13** | Negative Score Input (Invalid) | Home Score = -1, Away Score = 2 | Throws `InvalidValueException` | Planned |
| **TC-14** | Complete Fixture with Null Result | Attempt to complete fixture without scores | Throws `InvalidFixtureStateException` | Planned |

### Category D: Match-Day Ticketing, Shop & 15% GCT

| Test ID | Test Case Description | Input Data | Expected Output / Exception | Status |
|---|---|---|---|---|
| **TC-15** | Normal Ticket Sale | Capacity = 10,000, Sold = 100, Buy = 2 | Sold becomes 102; Grand Total = Base + 15% GCT | Planned |
| **TC-16** | Exact Venue Capacity (Boundary) | Capacity = 1,000, Sold = 998, Buy = 2 | Sold becomes 1,000; Transaction succeeds | Planned |
| **TC-17** | Exceed Stadium Capacity (Invalid) | Capacity = 1,000, Sold = 999, Buy = 2 | Throws `CapacityExceededException` | Planned |
| **TC-18** | GCT Calculation Verification | Subtotal = $1,000.00 | Tax = $150.00, Grand Total = $1,150.00 | Planned |
| **TC-19** | Exact Cash Payment (Boundary) | Total = $1,150.00, Tendered = $1,150.00 | Change = $0.00; Receipt issued | Planned |
| **TC-20** | Insufficient Payment Tendered | Total = $1,150.00, Tendered = $1,000.00 | Throws `InsufficientFundsException` | Planned |

### Category E: Persistence (Save & Reload)

| Test ID | Test Case Description | Input Data | Expected Output / Exception | Status |
|---|---|---|---|---|
| **TC-21** | Save and Reload Round-Trip | 4 Teams, 2 Stadiums, 2 Fixtures saved to text files | Reload restores identical state, counts, and stats | Planned |
| **TC-22** | Reload Corrupted Record Line | One line has missing fields | Logs warning, skips line gracefully without crash | Planned |
