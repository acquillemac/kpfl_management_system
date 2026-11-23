# Kingston Premier Football League (KPFL) Management System
### Object-Oriented Programming (OOP) Group Project

A Java application developed to manage football clubs, players, fixtures, stadium operations, ticketing, and stadium shop sales (including 15% GCT) for the Kingston Premier Football League.

Developed in **Java SE 17** using the **Eclipse IDE**.

---

## Project Overview

This project was built according to the 6-week OOP project specifications for the KPFL. It includes:
* **Person Hierarchy:** An abstract base class `Person` extended by `Player`, `Coach`, `MatchOfficial`, `LeagueCommissioner`, and `Spectator`.
* **Product Hierarchy:** An abstract base class `Product` extended by `Merchandise` and `FoodItem`.
* **Core Entities:** `FootballTeam`, `Stadium`, `Fixture`, `MatchResult`, and `StadiumShop`.
* **Business Rules & Custom Exceptions:** Handles minimum player age, stadium capacity limits, team/referee scheduling conflicts, negative scores, and insufficient payment.
* **15% GCT Calculations:** Applies Jamaica's General Consumption Tax to all ticket and shop sales with itemized receipts.
* **Sequential File Storage:** Saves and reloads all team, player, venue, and match data from text files (`.txt`).
* **Reports:** Standings table, financial and attendance audit, regulatory compliance audit, and CSV export.
* **JUnit 5 Tests:** Unit tests covering all business rules and edge cases.

---

## Project Structure

```
KPFL_Management_System/
├── src/
│   └── org/
│       └── kpfl/
│           ├── exception/     # Custom business exceptions
│           ├── model/
│           │   ├── league/    # FootballTeam, Stadium, Fixture, MatchResult, FootballLeague
│           │   ├── person/    # Person, Player, Coach, MatchOfficial, Commissioner, Spectator
│           │   └── shop/      # Product, Merchandise, FoodItem, Ticket, StadiumShop, OrderItem
│           ├── service/       # ReportService, CsvExportService
│           ├── storage/       # FilePersistenceManager (text file I/O)
│           └── ui/            # MainApplication, MenuHandler
├── test/
│   └── org/
│       └── kpfl/              # KPFLCoreTest (JUnit 5 test suite)
├── data/                      # Data files (teams.txt, players.txt, stadiums.txt, etc.)
├── docs/                      # Technical Report, User Guide, Test Plan, Contribution Log
├── .classpath
├── .project
└── README.md
```

---

## How to Run in Eclipse

1. Open **Eclipse**.
2. Go to **File** -> **Open Projects from File System...**
3. Select this folder as the source directory and click **Finish**.
4. To run the program:
   * Open `src/org/kpfl/ui/MainApplication.java`
   * Right-click -> **Run As** -> **Java Application**
5. To run the tests:
   * Open `test/org/kpfl/KPFLCoreTest.java`
   * Right-click -> **Run As** -> **JUnit Test**

---

## Authors & Group Members
* Member 1: Project Lead & Core Architecture
* Member 2: Teams & Personnel Lead
* Member 3: Fixtures & Stadium Venues Lead
* Member 4: Match-Day Ticketing & Concessions Lead
* Member 5: Persistence, Testing & Reporting Lead
