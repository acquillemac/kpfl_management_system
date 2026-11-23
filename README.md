# Kingston Premier Football League (KPFL) Management System
## Object-Oriented Programming (OOP) Java Project

A comprehensive Java application modeling league governance, football club management, fixture scheduling, match-day operations, ticketing, stadium-shop merchandising (with 15% Jamaican GCT), persistent file storage, and analytical management reports.

Developed in **Java (SE 17+)** and designed for the **Eclipse IDE**.

---

## 🌟 Core Features & Architecture

* **Two Complete Inheritance Hierarchies:**
  * **`Person` (Abstract Superclass):** Extended by `Player`, `Coach`, `MatchOfficial`, `LeagueCommissioner`, and `Spectator`.
  * **`Product` (Abstract Superclass):** Extended by `Merchandise` and `FoodItem`.
* **Classroom OOP Standard:**
  * Every model entity implements the standard academic constructor trio (**Default Constructor**, **Copy Constructor**, and **Primary Constructor**).
  * Field byte annotations for fixed-length sequential/random access persistence.
  * Dual presentation methods: clean multi-line `toString()` and direct `display()`.
* **8 Mandatory Business Rules & Custom Exceptions:**
  * `TeamIneligibleException` (BR-01)
  * `SchedulingConflictException` (BR-02 & BR-03)
  * `OfficialUnavailableException` (BR-03)
  * `CapacityExceededException` (BR-04)
  * `InvalidValueException` (BR-05)
  * `PlayerAgeRequirementException` (BR-06: Min. Age $\ge 16$)
  * `InsufficientFundsException` (BR-07: Cash tendered $\ge$ Total with 15% GCT)
  * `InvalidFixtureStateException` (BR-08)
* **Venue Concessions & Ticketing:**
  * Capacity monitoring ensuring ticket sales never exceed stadium limits.
  * Complete Stadium Shop POS calculating subtotal, 15% GCT, cash tender, change, and itemized receipts.
* **Persistent File Storage:**
  * Sequential text file reader and writer (`teams.txt`, `players.txt`, `stadiums.txt`, `fixtures.txt`, `products.txt`).
* **Analytical Management Reporting & CSV Export:**
  * Official KPFL Standings Table (Points 3-1-0, GD, GF, GA).
  * Match-Day Financial & Stadium Attendance Audit.
  * Regulatory & Personnel Compliance Audit.
  * Instant export to spreadsheet `.csv` files.
* **Dual-Mode User Interface:**
  * Interactive live management console with validated submenus.
  * Instant automated demonstration mode using prepared sample data.

---

## 📁 Project Directory Layout

```
KPFL_Management_System/
├── src/
│   └── org/
│       └── kpfl/
│           ├── exception/     # Custom checked exception hierarchy
│           ├── model/
│           │   ├── league/    # FootballTeam, Stadium, Fixture, MatchResult, FootballLeague
│           │   ├── person/    # Person, Player, Coach, MatchOfficial, Commissioner, Spectator
│           │   └── shop/      # Product, Merchandise, FoodItem, Ticket, StadiumShop, OrderItem
│           ├── service/       # ReportService, CsvExportService
│           ├── storage/       # FilePersistenceManager (Sequential file I/O)
│           └── ui/            # MainApplication, MenuHandler
├── test/
│   └── org/
│       └── kpfl/              # KPFLCoreTest (JUnit 5 test suite)
├── data/                      # Baseline sequential text files (teams, players, stadiums, etc.)
├── docs/                      # Technical Report, User Guide, Test Report, Contribution Log
├── .classpath                 # Eclipse classpath descriptor (JavaSE-17 + JUnit 5)
├── .project                   # Eclipse project definition
└── README.md
```

---

## 🚀 How to Run in Eclipse

1. **Import:** In Eclipse, go to **File** $\rightarrow$ **Open Projects from File System...** $\rightarrow$ browse to this directory and click **Finish**.
2. **Run App:** Navigate to `src/org/kpfl/ui/MainApplication.java` $\rightarrow$ Right-click $\rightarrow$ **Run As** $\rightarrow$ **Java Application**.
3. **Run Tests:** Navigate to `test/org/kpfl/KPFLCoreTest.java` $\rightarrow$ Right-click $\rightarrow$ **Run As** $\rightarrow$ **JUnit Test**.

---

## 👥 Authors
* Developed for the Jamaican Football League & Match-Day Management System 6-Week OOP Project.
