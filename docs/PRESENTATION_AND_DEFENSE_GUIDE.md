# Kingston Premier Football League (KPFL) & Match-Day Management System
## Presentation Guide & Oral Defense Preparation Manual

---

## 1. 10–15 Minute Demonstration & Presentation Script

In accordance with Section 14 of the project specification:
* *Every group member must speak and answer at least one technical question.*
* *The demonstration must use prepared sample data but should also accept live input.*

### Timing Breakdown (12 Minutes Target)

| Segment | Time | Speaker | Topics Covered & Actions |
|---|---|---|---|
| **1. Introduction & Architecture** | 0:00 – 2:30 | Member 1 | Problem overview, KPFL background, UML class architecture, Person and Product inheritance trees. |
| **2. Teams, Rosters & Practice** | 2:30 – 4:30 | Member 2 | Live creation of a club, adding a player with minimum age validation ($\ge 16$), booking pre-match stadium practice. |
| **3. Fixtures & Conflict Resolution** | 4:30 – 6:30 | Member 3 | Scheduling a fixture, demonstrating scheduling conflict detection (BR-02 & BR-03), recording match scores, updating standings. |
| **4. Ticketing, Shop POS & GCT** | 6:30 – 9:00 | Member 4 | Selling tickets with capacity limits, adding merchandise to cart, calculating 15% GCT, cash tendering, and receipt printing. |
| **5. Reports, Persistence & Tests** | 9:00 – 11:30 | Member 5 | Generating the 3 management reports, exporting to CSV, saving/reloading data, running JUnit 5 green bar in Eclipse. |
| **6. Conclusion & Q&A Transition** | 11:30 – 12:00 | Member 1 | Summary of achievements, transition to oral defense questions from the lecturer. |

---

## 2. Live Demonstration Script for Lecturers

When demonstrating the system in Eclipse:
1. **Launch `MainApplication.java`:**
   * Select Option `2` first to run the **Automated Demonstration**. Show the lecturer that all prepared sample data (teams, venues, fixtures, standings) loads and computes immediately without typing.
2. **Launch `MainApplication.java` again and select Option `1` (Interactive Live Input):**
   * Demonstrate **Menu 1 $\rightarrow$ Option 2 (Add Player):** Enter an age of `15`. Show the console catching `PlayerAgeRequirementException` with a clear user message. Then enter `16` or `22` to show successful addition.
   * Demonstrate **Menu 3 $\rightarrow$ Option 1 (Schedule Fixture):** Schedule a match for a team that already has a fixture at that date and time. Show `SchedulingConflictException` firing.
   * Demonstrate **Menu 4 $\rightarrow$ Option 1 (Sell Tickets):** Attempt to buy more tickets than available in a small stadium. Show `CapacityExceededException`.
   * Demonstrate **Menu 4 $\rightarrow$ Option 3 (Stadium Shop POS):** Buy an official jersey ($4,500.00). System shows $675.00 (15% GCT) = $5,175.00 Total. Enter $5,000.00 cash. Show `InsufficientFundsException`. Then tender $6,000.00 and display the formatted receipt with $825.00 change.
   * Demonstrate **Menu 5 $\rightarrow$ Option 4 (CSV Export):** Show the generated CSV files created in `./data/reports/`.
   * Open `KPFLCoreTest.java` in Eclipse, click **Run As $\rightarrow$ JUnit Test**, and present the Green Bar with 12 passing test methods.

---

## 3. Oral Defense: Anticipated Technical Questions & Model Answers

As highlighted in Section 13, individual accountability is verified by technical questions directed to each member.

### Question 1: Why did you make `Person` and `Product` abstract classes rather than interfaces?
* **Model Answer:**
  > *"We used abstract classes because both Person and Product contain shared state (attributes such as `id`, `name`, `age` in Person, and `productId`, `unitPrice`, `stockQuantity` in Product) and common concrete implementations (such as basic setters and financial calculations like `calculateSubtotal` and `calculateGCT`). Interfaces in Java prior to default methods only declare behavior, whereas abstract classes permit state encapsulation alongside abstract methods like `getDetails()` and `getProductSummary()`."*

### Question 2: What is the difference between Aggregation and Composition in your design, and where did you apply them?
* **Model Answer:**
  > *"Composition represents a 'has-a' relationship with life-cycle dependency: the child entity cannot exist without the parent. We applied Composition between `Fixture` and `MatchResult` (a score sheet cannot exist without its fixture), and `Stadium` and `StadiumShop`. Conversely, Aggregation represents a looser 'has-a' relationship where the child retains an independent lifecycle. We used Aggregation between `FootballTeam` and `Player` because players can be transferred or unassigned without being destroyed."*

### Question 3: How does your system enforce the 15% General Consumption Tax (GCT)?
* **Model Answer:**
  > *"We defined a centralized constant `GCT_RATE = 0.15` in the `Product` and `Ticket` classes. When calculating transactions, subtotal is computed first as $\sum(\text{UnitPrice} \times \text{Quantity})$, then tax is evaluated as $\text{Subtotal} \times 0.15$, yielding a grand total of $\text{Subtotal} + \text{GCT}$. We also validate that amount tendered meets or exceeds grand total before any stock is deducted or tickets are issued."*

### Question 4: How does your code prevent scheduling conflicts for teams and match officials?
* **Model Answer:**
  > *"In `FootballLeague.scheduleFixture()`, before instantiating a new `Fixture`, we iterate through all existing fixtures. If an existing fixture matches the requested date and kickoff time, we check whether either the proposed home team or away team is already participating. If an overlap is detected, a `SchedulingConflictException` is thrown. Similarly, in `Fixture.assignOfficial()`, if an official's `isAvailable()` flag is false or they are already booked for that time window, an `OfficialUnavailableException` is thrown."*

### Question 5: Why did you create custom exception classes rather than just throwing `RuntimeException`?
* **Model Answer:**
  > *"Creating custom domain exceptions (inheriting from `KPFLException` $\rightarrow$ `Exception`) provides meaningful, self-documenting error types specifically aligned with the league's business rules (such as `CapacityExceededException` or `PlayerAgeRequirementException`). This allows our service and UI layers to catch specific errors and present actionable recovery messages to the user without crashing the JVM."*

### Question 6: How does your persistent file storage handle corrupted or malformed lines?
* **Model Answer:**
  > *"In `FilePersistenceManager.java`, loading routines wrap each record parse in a granular `try-catch` block inside the loop. If a line is missing required fields or has non-numeric data where an integer is expected, the exception is caught, a descriptive warning is logged to standard error indicating the corrupted line, and execution continues to the next line without crashing the entire import."*
