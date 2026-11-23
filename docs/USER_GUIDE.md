# Kingston Premier Football League (KPFL) & Match-Day Management System
## Comprehensive User Guide & Operating Manual

---

## 1. System Requirements & Setup in Eclipse

### Prerequisites
* **Java Development Kit (JDK):** Java SE 17 or higher.
* **Integrated Development Environment:** Eclipse IDE for Java Developers (2022-09 or newer recommended).
* **Operating System:** Windows, macOS, or Linux.

### Importing Project into Eclipse
1. Launch **Eclipse IDE**.
2. Select **File** $\rightarrow$ **Open Projects from File System...** (or **File** $\rightarrow$ **Import...** $\rightarrow$ **Existing Projects into Workspace**).
3. In the dialog, click **Directory...** next to *Import source*, and navigate to `C:\Users\acqui\Documents\Jfl`.
4. Verify that `KPFL_Management_System` is checked in the project list.
5. Click **Finish**.
6. In the **Package Explorer**, verify the folder layout:
   * `src/`: Java source packages under `org.kpfl.*`
   * `test/`: JUnit test suite `org.kpfl.KPFLCoreTest`
   * `data/`: Delimited persistent text data files
   * `docs/`: Documentation and project reports

---

## 2. Launching and Running the Application

### Running the System
1. In the Eclipse **Package Explorer**, expand:
   `src` $\rightarrow$ `org.kpfl.ui` $\rightarrow$ `MainApplication.java`.
2. Right-click `MainApplication.java` $\rightarrow$ **Run As** $\rightarrow$ **Java Application**.
3. The Eclipse **Console** tab will open with the startup prompt:
   ```
   =============================================================
      KINGSTON PREMIER FOOTBALL LEAGUE (KPFL) SYSTEM
                    Eclipse & Java OOP Project
   =============================================================

   [INIT] Loading baseline league data from ./data...
   [INIT] Successfully loaded: 5 Teams, 5 Stadiums, 4 Fixtures.

   Select operational mode:
   1. Launch Interactive Management Console (Live Input)
   2. Run Automated Verification Demonstration (All Reports & POS)
   Enter choice (1 or 2):
   ```

---

## 3. Step-by-Step Feature Walkthrough

### Menu 1: Teams & Personnel Management
* **Option 1 (Register New Football Team):**
  * Enter unique Registration Number (e.g., `KPFL-TM-06`), Club Name (`Tivoli Gardens FC`), and Parish (`Kingston`).
  * Enforces uniqueness; duplicate registration numbers are rejected.
* **Option 2 (Add Player to Team Roster):**
  * Enter team registration number, player ID, name, age, gender, position, and jersey number (1–99).
  * **Age Rule Enforcement:** If age is under 16, system throws `PlayerAgeRequirementException` and halts addition.
  * **Jersey Number Enforcement:** Duplicate jersey numbers on the same squad are prohibited.
* **Option 3 (Assign Head Coach):**
  * Assigns head coach with professional qualifications (e.g., CONCACAF Pro License).
* **Option 4 (Search Teams):**
  * Allows searching clubs by parish (e.g., `Kingston`, `St. Andrew`) or club name substring.
* **Option 5 (View Teams & Rosters):**
  * Displays complete squad rosters, player positions, goal tallies, and playing statuses.

---

### Menu 2: Stadium & Venue Operations
* **Option 1 (Register New Stadium):**
  * Enter venue ID, stadium name, location, seating capacity, field condition, and base ticket price.
* **Option 2 (View Stadiums):**
  * Lists all registered stadiums, current occupancy, available seats, and booked practice sessions.
* **Option 3 (Request Pre-Match Practice Session):**
  * Enter team registration number, stadium ID, and requested time slot (e.g., `2026-04-14 09:00 AM`).
  * Suspended clubs are barred from reserving practice sessions.

---

### Menu 3: Fixture Scheduling & Match Operations
* **Option 1 (Schedule New Fixture):**
  * Select Home Team, Away Team, Stadium Venue, Date, and Time.
  * **Conflict Checks:** Prevents double-booking clubs or venues on the same date/time.
* **Option 2 (Record Match Result & Finalize):**
  * Select Fixture ID and enter final scores (e.g., Home: `2`, Away: `1`).
  * System updates match status to `COMPLETED`, adds 3 points to the winner (or 1 each for a draw), and recalculates goal differences.
* **Option 3 (View Fixtures):**
  * Lists all scheduled, in-progress, and completed matches.

---

### Menu 4: Match-Day Services (Ticketing & Stadium Shop POS)
* **Option 1 (Sell Match Tickets):**
  * Select venue and enter number of tickets.
  * **Capacity Guard:** Verifies remaining seats. If requested tickets exceed capacity, throws `CapacityExceededException`.
  * Computes subtotal, applies 15% GCT, prompts for cash tendered, and calculates change.
* **Option 2 (View Stadium Shop Inventory):**
  * Displays official merchandise (jerseys, flags, scarves) and concessions (patties, drinks).
* **Option 3 (Purchase Shop Items):**
  * Interactive cart: Enter Product ID (e.g., `PRD-M01`) and quantity.
  * Computes subtotal + 15% GCT.
  * Prompts for payment. If cash tendered is insufficient, throws `InsufficientFundsException`.
  * Prints a formatted, itemized cash receipt.

---

### Menu 5: Management Reports & Analytics
* **Option 1:** Outputs official KPFL League Standings Table (PTS, GD, GF, GA, W, D, L).
* **Option 2:** Outputs Match-Day Financial & Attendance Audit (Gross revenues, occupancy %, 15% GCT collected).
* **Option 3:** Outputs Regulatory & Compliance Audit (Minimum age verification, coaching licenses, official availability).
* **Option 4 (CSV Export Extension):** Exports all three reports to `./data/reports/` in standard CSV format for Excel/spreadsheet analysis.

---

### Menu 6: Persistent Storage
* **Option 1 (Save Current State):** Serializes all teams, players, venues, fixtures, and shop items into `./data/*.txt`.
* **Option 2 (Reload State):** Restores saved state from files.

---

## 4. Running Unit Tests in Eclipse
1. In the Package Explorer, locate:
   `test` $\rightarrow$ `org.kpfl` $\rightarrow$ `KPFLCoreTest.java`.
2. Right-click `KPFLCoreTest.java` $\rightarrow$ **Run As** $\rightarrow$ **JUnit Test**.
3. The **JUnit View** in Eclipse will display a **Green Bar** verifying all test cases passed.
