# KPFL Management System: Week 1 Deliverable
## Part 1: Requirements Analysis, Actors, Constraints & Detailed Use Cases

---

## 1. System Overview & Scope
The Kingston Premier Football League (KPFL) and Match-Day Management System is an Object-Oriented software solution developed in Java to oversee league governance, football club registrations, match scheduling, official assignments, stadium operations, ticketing, stadium-shop merchandising, and financial accounting (including 15% GCT).

---

## 2. Stakeholders & System Actors

| Actor | Type | Description / Responsibilities |
|---|---|---|
| **League Commissioner** | Primary Administrative Actor | Approves team registrations, inspects match venues, oversees league standings, and assigns certified Match Officials without scheduling overlaps. |
| **Team Manager / Coach** | Administrative Actor | Manages squad rosters, adds/updates players, ensures age compliance, and requests stadium practice times. |
| **Match Official (Referee / Linesman)** | Operational Actor | Officiates fixtures, ensures game compliance, and reports final scores and disciplinary events. |
| **Ticketing Agent / Match-Day Operator** | Operational Actor | Sells match tickets, validates venue capacity constraints, and generates spectator admission receipts. |
| **Stadium Shop Cashier** | Operational Actor | Processes sales of official merchandise (jerseys, flags) and food/beverages, computes 15% GCT, tenders cash, and issues change. |
| **System Administrator / Auditor** | Governance Actor | Executes data persistence routines (saving/reloading from sequential files) and exports management reports. |

---

## 3. Comprehensive Requirements

### 3.1 Functional Requirements (FR)
* **FR-01: Team & Personnel Governance:** Register, update, search, and view football clubs. Manage coaching staff and player rosters with position, squad number, and eligibility checks.
* **FR-02: Stadium & Venue Management:** Register stadiums with seating capacities, field conditions, and base pricing. Support pre-match practice scheduling.
* **FR-03: Fixture Scheduling & Conflict Resolution:** Create fixtures pairing two eligible teams at a designated stadium, date, and time. Ensure neither team nor assigned match officials have overlapping fixtures.
* **FR-04: Match Result Processing & Points System:** Record final scores for completed fixtures. Automatically update league table using standard KPFL scoring:
  * **Win:** 3 points
  * **Draw:** 1 point
  * **Loss:** 0 points
* **FR-05: Capacity-Constrained Ticketing:** Sell tickets per category (e.g., Grandstand, Bleachers) while verifying that total tickets sold never exceed the venue's seating capacity.
* **FR-06: Stadium Shop POS:** Maintain inventory for merchandise and food items. Process shopping cart transactions, computing subtotal, 15% GCT, grand total, and change.
* **FR-07: Persistent File Storage:** Save and reload the state of the league (teams, personnel, stadiums, fixtures, inventory) using sequential access data files.
* **FR-08: Analytical Management Reporting:** Produce at least three management reports:
  1. Official League Standings Table
  2. Match-Day Financial & Stadium Attendance Summary
  3. Team Compliance & Official Schedule Audit Report

### 3.2 Non-Functional Requirements (NFR)
* **NFR-01: Strict OOP Design:** High cohesion, low coupling, extensive encapsulation (private attributes with validated accessors/mutators), abstraction, polymorphism, and distinct inheritance trees.
* **NFR-02: Robust Exception Handling:** Prohibited business operations must throw explicit custom checked/unchecked exceptions rather than crashing or terminating unexpectedly.
* **NFR-03: Data Integrity:** Financial figures must be rounded to two decimal places; scores and quantities must be non-negative integers.
* **NFR-04: Platform Portability:** Runs seamlessly on Standard Java SE (17+) inside Eclipse IDE without external proprietary libraries.

---

## 4. Mandatory Business Rules & Constraints

1. **BR-01 (Team Eligibility):** Unregistered or suspended teams cannot be scheduled for any fixture.
2. **BR-02 (Team Non-Duplication):** A team cannot be scheduled for more than one fixture on the same calendar date and time.
3. **BR-03 (Official Availability):** A match official cannot be assigned to more than one fixture on the same day and time.
4. **BR-04 (Capacity Safety):** Ticket sales for any fixture must not exceed the maximum seating capacity of the host stadium.
5. **BR-05 (Domain Boundaries):** Match scores, inventory quantities, ticket quantities, and prices cannot be negative.
6. **BR-06 (Player Age Rule):** All registered players must meet the league minimum age requirement ($\ge 16$ years of age).
7. **BR-07 (Financial Sufficiency):** A shop or ticket transaction cannot proceed if the amount tendered is less than the grand total (including 15% GCT).
8. **BR-08 (Fixture Lifecycle):** A fixture cannot be marked as "COMPLETED" without recording a valid, non-negative match score.

---

## 5. Five Detailed Use Cases

### Use Case 1: Register Team and Validate Player Roster
* **Primary Actor:** Team Manager / League Commissioner
* **Pre-conditions:** Team registration details are prepared; league season is active.
* **Main Success Scenario:**
  1. Commissioner enters team name, home parish, registration number, and assigned head coach.
  2. System validates that the registration number is unique and marks status as `REGISTERED`.
  3. Manager submits player details (name, ID, age, gender, jersey number, position).
  4. System verifies each player is $\ge 16$ years old and that jersey numbers are unique within the squad.
  5. System adds players to the team roster and updates current squad size.
* **Extensions / Exceptions:**
  * *4a. Player age < 16:* System throws `PlayerAgeRequirementException` and rejects player addition.
  * *4b. Duplicate Jersey Number:* System alerts manager and prompts for a distinct number.
* **Post-conditions:** Team is registered and eligible for fixture scheduling.

---

### Use Case 2: Schedule Fixture with Conflict Checking
* **Primary Actor:** League Commissioner
* **Pre-conditions:** Both teams are in `REGISTERED` status; stadium is active; officials are registered.
* **Main Success Scenario:**
  1. Commissioner selects Home Team, Away Team, Stadium, Date, and Kick-off Time.
  2. System verifies Home and Away teams are different clubs.
  3. System validates neither team has another fixture scheduled on the same date/time.
  4. System confirms the stadium is available on the specified date/time.
  5. Commissioner assigns Lead Referee and Assistant Referees.
  6. System validates none of the assigned officials are double-booked.
  7. Fixture is created with status `SCHEDULED`.
* **Extensions / Exceptions:**
  * *1a. Either team is SUSPENDED or UNREGISTERED:* System throws `TeamIneligibleException`.
  * *3a. Team has another match at that time:* System throws `SchedulingConflictException`.
  * *6a. Match Official has another match:* System throws `OfficialUnavailableException`.
* **Post-conditions:** Fixture is locked into the league schedule.

---

### Use Case 3: Sell Match-Day Tickets with Capacity Monitoring
* **Primary Actor:** Ticketing Agent / Spectator
* **Pre-conditions:** Fixture is in `SCHEDULED` status; stadium seating capacity is defined.
* **Main Success Scenario:**
  1. Spectator requests $N$ tickets for a specific scheduled fixture.
  2. System queries the stadium's total capacity and current tickets sold.
  3. System verifies $(\text{ticketsSold} + N) \le \text{seatingCapacity}$.
  4. System calculates base price, applies 15% GCT, and computes grand total.
  5. Cashier enters amount tendered; system validates $\text{tendered} \ge \text{total}$.
  6. System increments tickets sold, creates `Ticket` records, and issues itemized receipt showing change.
* **Extensions / Exceptions:**
  * *3a. Tickets requested exceed remaining seats:* System throws `CapacityExceededException` with available seat count.
  * *5a. Insufficient payment:* System throws `InsufficientFundsException` and aborts issuance.
* **Post-conditions:** Stadium occupancy increases; ticket revenue and tax are logged.

---

### Use Case 4: Process Stadium Shop Transaction
* **Primary Actor:** Stadium Shop Cashier / Spectator
* **Pre-conditions:** Product inventory is loaded with current stock and unit prices.
* **Main Success Scenario:**
  1. Cashier scans/selects merchandise (e.g., KPFL Jersey) and food items (e.g., Patties, Drinks) with quantities.
  2. System verifies adequate stock is available for each requested item.
  3. System computes $\text{Subtotal} = \sum (\text{Price} \times \text{Qty})$.
  4. System applies $\text{GCT} = \text{Subtotal} \times 0.15$ and computes $\text{Grand Total}$.
  5. Cashier receives payment and enters amount tendered.
  6. System validates $\text{Tendered} \ge \text{Grand Total}$, calculates change, deducts inventory, and prints itemized receipt.
* **Extensions / Exceptions:**
  * *2a. Requested quantity > available stock:* System notifies cashier of insufficient inventory and displays current stock.
  * *5a. Payment tendered is less than Grand Total:* System raises `InsufficientFundsException`.
* **Post-conditions:** Inventory stock updated; transaction appended to sales history.

---

### Use Case 5: Record Match Result and Update League Standings
* **Primary Actor:** Match Official / League Commissioner
* **Pre-conditions:** Fixture is in `SCHEDULED` status.
* **Main Success Scenario:**
  1. Official enters final score for Home Team and Away Team.
  2. System validates scores are non-negative integers ($\ge 0$).
  3. System creates a `MatchResult` and attaches it to the `Fixture`.
  4. System updates match status to `COMPLETED`.
  5. System updates team statistics:
     * If Home Score > Away Score: Home Team points $+3$, wins $+1$; Away Team losses $+1$.
     * If Away Score > Home Score: Away Team points $+3$, wins $+1$; Home Team losses $+1$.
     * If Home Score == Away Score: Both teams points $+1$, draws $+1$.
  6. Goals for, goals against, and goal differences are recalculated for both teams.
  7. League table rankings are re-sorted automatically.
* **Extensions / Exceptions:**
  * *2a. Negative score entered:* System throws `InvalidValueException`.
  * *4a. Attempt to complete fixture without scores:* System throws `InvalidFixtureStateException`.
* **Post-conditions:** Match result finalized; league standings reflect the latest outcome.
