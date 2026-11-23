# Kingston Premier Football League (KPFL) & Match-Day Management System
## Group Contribution Log & Individual Member Reflections

---

## 1. Group Role Allocation Overview
As specified in Section 10 of the project brief, team responsibilities are mapped to dedicated modular areas. This structure supports groups of 3 to 5 members (or individual execution mapped across core modules).

| Member / Role | Primary Responsibilities | Core Deliverables & Source Files |
|---|---|---|
| **Member 1: Project Lead & Architecture** | Project management, system design, requirements analysis, core OOP architecture, and commissioner governance. | UML Diagrams, Requirements, `Person.java`, `LeagueCommissioner.java`, `MainApplication.java`. |
| **Member 2: Team & Personnel Lead** | Player and coaching staff hierarchies, squad validations, age constraints, and team standings logic. | `Player.java`, `Coach.java`, `FootballTeam.java`, age verification (`PlayerAgeRequirementException`). |
| **Member 3: Fixtures & Stadiums Lead** | Match scheduling, official coordination, venue capacity tracking, and pre-match practice reservations. | `Stadium.java`, `Fixture.java`, `MatchResult.java`, `SchedulingConflictException`, `CapacityExceededException`. |
| **Member 4: Match-Day, Ticketing & Shop Lead** | Stadium shop merchandising, concessions, POS cart engine, 15% GCT calculations, and cash receipts. | `Product.java`, `Merchandise.java`, `FoodItem.java`, `Ticket.java`, `StadiumShop.java`, `SaleTransaction.java`. |
| **Member 5: Persistence, QA & Reporting Lead** | Sequential file storage, exception handling, analytical reporting, CSV export extension, and JUnit testing. | `FilePersistenceManager.java`, `ReportService.java`, `CsvExportService.java`, `KPFLCoreTest.java`. |

---

## 2. Weekly Contribution Log

### Week 1: Requirements Analysis & System Design
* **Member 1:** Authored system requirements, actors, constraints, and project timeline.
* **Member 2:** Drafted initial domain class sketches and person hierarchy models.
* **Member 3:** Formulated fixture conflict rules and stadium practice workflow.
* **Member 4:** Defined stadium-shop catalog and 15% GCT tax formula.
* **Member 5:** Initialized Eclipse repository, `.project`, `.classpath`, and data storage format.

### Week 2: Core Domain Implementation
* **Member 1:** Implemented abstract class `Person` and accessors/mutators with defensive validation.
* **Member 2:** Implemented `Player`, `Coach`, and `FootballTeam` with minimum age checks.
* **Member 3:** Implemented `Stadium` and `MatchOfficial` with seating capacity boundaries.
* **Member 4:** Implemented abstract `Product`, `Merchandise`, and `FoodItem`.
* **Member 5:** Developed initial baseline unit tests for entity constructors and accessors.

### Week 3: League Operations & Business Rules
* **Member 1:** Implemented `FootballLeague` coordinator and team registration logic.
* **Member 2:** Implemented points system (Win = 3, Draw = 1, Loss = 0) and goal difference sorting.
* **Member 3:** Implemented `Fixture` scheduling with team/official conflict detection.
* **Member 4:** Built `Ticket` entity and linked ticket pricing with stadium capacities.
* **Member 5:** Implemented custom exception hierarchy (`KPFLException`, `SchedulingConflictException`, etc.).

### Week 4: Match-Day Services & Persistence
* **Member 1:** Implemented team and club search functions (by parish and name).
* **Member 2:** Implemented pre-match practice scheduling on stadium venues.
* **Member 3:** Implemented `MatchResult` recording and automatic league standings refresh.
* **Member 4:** Implemented `StadiumShop` transaction engine, order cart, and receipt generation.
* **Member 5:** Implemented `FilePersistenceManager` for sequential text file loading/saving.

### Week 5: Integration, Testing & Reporting
* **Member 1:** Integrated all subsystems into `MainApplication.java` and `MenuHandler.java`.
* **Member 2:** Built Report 1: Official KPFL League Standings Table.
* **Member 3:** Built Report 3: Regulatory & Personnel Compliance Audit Report.
* **Member 4:** Built Report 2: Match-Day Financial & Stadium Attendance Report.
* **Member 5:** Implemented `CsvExportService` and executed comprehensive 22-case JUnit 5 test suite.

### Week 6: Technical Documentation & Oral Defense Preparation
* **All Members:** Compiled Technical Report, User Guide, Test Execution Report, and rehearsed technical oral defense questions.

---

## 3. Individual Member Reflections

### Reflection: Project Lead & Architecture
> *"Developing the KPFL system provided invaluable experience in translating real-world sports administration rules into an object-oriented Java architecture. Balancing high cohesion with loose coupling required careful modeling of associations versus compositions—particularly ensuring that players remained aggregated within clubs rather than composed. Designing the dual-mode console interface also highlighted the importance of user experience in systems administration."*

### Reflection: Team & Personnel Lead
> *"Implementing encapsulation with defensive programming was the highlight of my contribution. Enforcing the league's minimum age rule ($\ge 16$ years) directly inside the Player constructor and FootballTeam methods prevented invalid data from ever polluting our collections. Overriding `compareTo` to rank teams dynamically by points, goal difference, and goals scored demonstrated the practical power of Java interfaces."*

### Reflection: Fixtures & Stadiums Lead
> *"The most challenging aspect was preventing scheduling conflicts for both clubs and match officials. Implementing conflict detection algorithms that inspect existing fixture dates and kickoff times before approving a new match taught me how critical algorithmic rigor is in preventing double-bookings. Linking stadium capacity checks to ticketing ensured physical venue safety was enforced in code."*

### Reflection: Match-Day & Ticketing Lead
> *"Developing the Stadium Shop transaction engine reinforced the importance of transactional integrity in financial software. Calculating the 15% General Consumption Tax (GCT) accurately and validating that cash tendered met or exceeded grand totals before updating stock levels was essential. Generating itemized, formatted receipts was satisfying and brought the match-day experience to life."*

### Reflection: Persistence, QA & Reporting Lead
> *"Implementing persistent sequential file I/O taught me how to handle real-world data storage without relying on external databases. Designing a pipe-delimited schema with defensive parsing ensured our system could recover gracefully from corrupt records. Writing the JUnit 5 test suite covering normal, boundary, and invalid scenarios gave our group confidence that every mandatory business rule was completely satisfied."*
