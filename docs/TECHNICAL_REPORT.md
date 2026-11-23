# Kingston Premier Football League (KPFL) & Match-Day Management System
## Technical Report & System Design

---

## 1. Project Overview
The Kingston Premier Football League (KPFL) Match-Day Management System is an Object-Oriented Java application developed for the 6-week OOP project. The system manages football clubs, player rosters, stadium venues, fixture schedules, match scores, ticketing, stadium shop transactions (including 15% Jamaican GCT), persistent file storage, and management reports.

### Key Implementation Goals:
1. **Object-Oriented Design:** Proper use of Abstraction, Encapsulation, Inheritance, and Polymorphism.
2. **Business Rules & Exceptions:** Using custom exceptions to handle invalid data and competition rule violations.
3. **Financial Calculations:** Accurate calculation of subtotals, 15% GCT, cash payments, change, and receipt generation.
4. **File Persistence:** Saving and reloading data using sequential text files.
5. **Reporting:** Generating formatted standings tables, financial summaries, and compliance audit reports, with CSV export.

---

## 2. Object-Oriented Design

### 2.1 Abstraction
* **`Person` (Abstract Class):** Holds common attributes (`id`, `name`, `age`, `gender`) and defines the abstract method `public abstract String getDetails()`.
* **`Product` (Abstract Class):** Represents items sold in the stadium shop (`productId`, `name`, `unitPrice`, `stockQuantity`) and defines `public abstract String getProductSummary()`. Contains common pricing methods for calculating tax and totals.

### 2.2 Inheritance
Two main class hierarchies are implemented:
1. **Person Hierarchy:**
   * `Person` -> `Player` (adds position, jersey number, goals, playing status)
   * `Person` -> `Coach` (adds qualification, coaching role, team assignment)
   * `Person` -> `MatchOfficial` (adds officiating role, rank tier, availability)
   * `Person` -> `LeagueCommissioner` (adds accreditation code)
   * `Person` -> `Spectator` (adds preferred seating category)
2. **Product Hierarchy:**
   * `Product` -> `Merchandise` (adds size and apparel category)
   * `Product` -> `FoodItem` (adds expiration date and vegetarian flag)

### 2.3 Polymorphism
* `Person.getDetails()` and `display()`: When called on a list of `Person` objects, each person displays their specific role information.
* `Product.getProductSummary()`: Displays specific product details depending on whether the item is merchandise or food.

### 2.4 Encapsulation & Validation
All instance variables are declared `private` or `protected`. Public getters and setters enforce data rules:
* Player age must be 16 or older.
* Jersey numbers must be between 1 and 99 and unique per team.
* Scores, prices, and quantities cannot be negative.

### 2.5 Class Relationships
* **Aggregation (`FootballTeam` and `Player`):** A team has players, but players exist independently and can be traded or released.
* **Composition (`Fixture` and `MatchResult`):** A match result belongs strictly to its fixture and cannot exist without it.
* **Composition (`Stadium` and `StadiumShop`):** The stadium shop is a permanent facility within the stadium venue.
* **Association (`Fixture` and `FootballTeam` / `Stadium` / `MatchOfficial`):** Fixtures link two teams, a stadium venue, and assigned officials.

---

## 3. Business Rules and Custom Exceptions

| Rule | Constraint | Exception | Where Handled |
|---|---|---|---|
| **BR-01** | Unregistered or suspended teams cannot be scheduled. | `TeamIneligibleException` | `FootballLeague.scheduleFixture()` |
| **BR-02** | A team cannot play two matches on the same date and time. | `SchedulingConflictException` | `FootballLeague.scheduleFixture()` |
| **BR-03** | An official cannot be assigned to conflicting matches. | `OfficialUnavailableException` | `Fixture.assignOfficial()` |
| **BR-04** | Ticket sales cannot exceed stadium seating capacity. | `CapacityExceededException` | `Stadium.allocateTickets()` |
| **BR-05** | Scores, stock, and prices cannot be negative. | `InvalidValueException` | Model Setters and Constructors |
| **BR-06** | Players must be at least 16 years old. | `PlayerAgeRequirementException` | `Player` and `FootballTeam.addPlayer()` |
| **BR-07** | Payment tendered must be greater than or equal to total with 15% GCT. | `InsufficientFundsException` | `StadiumShop.processSale()` |
| **BR-08** | A fixture cannot be marked completed without valid scores. | `InvalidFixtureStateException` | `Fixture.recordResultAndComplete()` |

---

## 4. Financial Calculations & GCT

All match-day sales (tickets and shop concessions) include Jamaica's 15% General Consumption Tax (GCT):
* `Subtotal = Unit Price * Quantity`
* `GCT = Subtotal * 0.15`
* `Grand Total = Subtotal + GCT`
* `Change = Amount Tendered - Grand Total`

If payment tendered is less than the grand total, an `InsufficientFundsException` is thrown and the transaction is cancelled.

---

## 5. File Persistence

Data is stored in `./data/` using pipe-delimited text files (`|`). This makes records easy to read, verify, and inspect.
* `teams.txt`: Team registration, name, parish, record, and points.
* `players.txt`: Player ID, name, age, gender, team, position, jersey, goals, status.
* `stadiums.txt`: Stadium ID, name, location, capacity, tickets sold, condition, price.
* `fixtures.txt`: Fixture ID, teams, venue, date, time, status, scores.
* `products.txt`: Product ID, type, name, price, stock, specific attributes.

The `FilePersistenceManager` class handles saving and reloading all data. If an individual line in a file has bad formatting, it logs a warning and continues loading the rest of the file without crashing.

---

## 6. Management Reports & CSV Export

The system generates three management reports:
1. **KPFL League Table:** Displays games played, wins, draws, losses, goals for, goals against, goal difference, and points.
2. **Financial & Attendance Audit:** Shows venue capacity, tickets sold, occupancy rate, ticket revenues, shop revenues, and total 15% GCT collected.
3. **Compliance Audit:** Verifies squad sizes, player age compliance, coach qualifications, and referee availability.

An optional CSV export feature saves these reports to `./data/reports/` for viewing in Microsoft Excel or Google Sheets.
