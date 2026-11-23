# Kingston Premier Football League (KPFL) & Match-Day Management System
## Group Contribution Log & Individual Member Reflections

---

## 1. Group Role Allocation

| Member Name | Student ID | Assigned Role | Primary Modules & Deliverables |
|---|---|---|---|
| Member 1 | [Student ID 1] | Project Lead & Coordinator | Requirements, UML design, `Person`, `LeagueCommissioner`, `MainApplication.java`. |
| Member 2 | [Student ID 2] | Team & Personnel Lead | `Player`, `Coach`, `FootballTeam`, age verification, roster validation. |
| Member 3 | [Student ID 3] | Fixtures & Venues Lead | `Stadium`, `Fixture`, `MatchResult`, conflict resolution, practice bookings. |
| Member 4 | [Student ID 4] | Match-Day Services Lead | `Product`, `Merchandise`, `FoodItem`, `Ticket`, `StadiumShop`, 15% GCT calculations. |
| Member 5 | [Student ID 5] | Persistence & Testing Lead | `FilePersistenceManager`, `ReportService`, `CsvExportService`, `KPFLCoreTest` suite. |

---

## 2. Weekly Progress Log

### Week 1: Planning and Requirements
* Team reviewed the project specification from Mr. Clarke.
* Identified the 5 core use cases, business rules, and constraints.
* Designed the initial UML class diagram and relationship types (inheritance, composition, aggregation).
* Set up the shared Git repository and Eclipse project workspace.

### Week 2: Core Classes Implementation
* Implemented the `Person` hierarchy: abstract base class `Person`, along with `Player`, `Coach`, `MatchOfficial`, and `LeagueCommissioner`.
* Implemented `FootballTeam` and `Stadium` classes with full encapsulation.
* Added default constructors, copy constructors, and primary constructors with input checks.
* Wrote initial tests to verify object creation and display methods.

### Week 3: League Operations
* Built the `Fixture` and `MatchResult` classes.
* Implemented the points system (Win = 3, Draw = 1, Loss = 0).
* Created custom exception classes for business rules (e.g., `SchedulingConflictException`, `TeamIneligibleException`).
* Implemented conflict detection to ensure teams and referees cannot be double-booked.

### Week 4: Match-Day Operations & File Storage
* Developed the `Product` hierarchy (`Merchandise` and `FoodItem`) and `Ticket` class.
* Built `StadiumShop` POS logic to calculate subtotals, 15% GCT, payment tendered, and change.
* Implemented `FilePersistenceManager` to save and load data from sequential text files.
* Created the 3 management reports (League Table, Financial Summary, and Compliance Audit).

### Week 5: Integration and Testing
* Connected all modules into `MenuHandler.java` with submenus and input validation.
* Tested boundary cases (e.g. stadium capacity exact limit, minimum player age cutoff).
* Added optional CSV export feature for spreadsheet viewing.
* Ran full JUnit 5 test suite to verify all business rules.

### Week 6: Final Documentation & Presentation
* Prepared the User Guide, Technical Report, and Presentation walkthrough.
* Rehearsed oral defense questions and system demonstration.

---

## 3. Individual Member Reflections

### Member 1: Project Lead
> "Working on the overall structure and the Person hierarchy taught me a lot about when to use abstract classes versus interfaces. Coordinating the group tasks and making sure our packages all connected smoothly in Eclipse was challenging at first, but having our weekly checkpoints kept us on schedule."

### Member 2: Team & Personnel Lead
> "My focus was on the FootballTeam and Player classes. Enforcing the minimum age of 16 and preventing duplicate jersey numbers in the roster was tricky to get right, especially ensuring our custom exceptions were caught properly in the menu instead of crashing the program."

### Member 3: Fixtures & Venues Lead
> "The fixture scheduling logic was the most complex part of my section because I had to check both team dates and referee availability to make sure no one was double-booked. Also linking the stadium seating capacity to ticket sales helped make the match-day operations realistic."

### Member 4: Match-Day Services Lead
> "I worked on the StadiumShop and Product classes. Calculating the 15% GCT accurately and handling the payment tendered versus change given took some testing, but generating the formatted receipts and seeing the cart update inventory was really rewarding."

### Member 5: Persistence & Testing Lead
> "Writing the file manager using sequential files gave me hands-on practice with BufferedReader and BufferedWriter. Designing the unit test suite in JUnit 5 helped our whole group catch edge cases, like when tickets sold matched the exact stadium capacity."
