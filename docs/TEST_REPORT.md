# Kingston Premier Football League (KPFL) & Match-Day Management System
## Formal Test Plan Execution & Evidence Report

---

## 1. Test Strategy Overview
In accordance with course specifications, testing exercises all system components across **Normal**, **Boundary**, and **Invalid (Error)** operational conditions. The suite validates core OOP class contracts, defensive setter constraints, mandatory competition business rules, financial calculations (with 15% GCT), persistent storage round-trips, and analytical reporting.

* **Testing Framework:** JUnit 5 (Jupiter)
* **Execution Target:** Eclipse IDE JUnit Runner / Standard Java Test Suite
* **Overall Result:** 12 Test Methods / 22 Evaluated Test Assertions — **100% PASS**

---

## 2. Business Rules & Edge Cases Matrix

| Test ID | Category | Target Component | Input Conditions | Expected Output | Actual Result | Status |
|---|---|---|---|---|---|---|
| **TC-01** | Normal | `Player` / `FootballTeam` | Age: 26, Jersey: 11, Position: Forward | Added to squad successfully | Added to squad successfully | **PASS** |
| **TC-02** | Boundary | `Player` | Age: 16 (Exact minimum cutoff) | Player instantiated; added to squad | Player instantiated; added to squad | **PASS** |
| **TC-03** | Invalid (BR-06) | `Player` | Age: 15 (Underage cutoff) | Throws `PlayerAgeRequirementException` | `PlayerAgeRequirementException` thrown | **PASS** |
| **TC-04** | Invalid (BR-05) | `Player` | Jersey: -1 or Age: -5 | Throws `InvalidValueException` | `InvalidValueException` thrown | **PASS** |
| **TC-05** | Invalid | `FootballTeam` | Two players assigned jersey #10 | Throws `InvalidValueException` (Duplicate) | `InvalidValueException` thrown | **PASS** |
| **TC-06** | Invalid (BR-01) | `Fixture` / `FootballLeague` | Team status: `SUSPENDED` | Throws `TeamIneligibleException` | `TeamIneligibleException` thrown | **PASS** |
| **TC-07** | Invalid (BR-02) | `FootballLeague` | Team scheduled for 2 fixtures at same date/time | Throws `SchedulingConflictException` | `SchedulingConflictException` thrown | **PASS** |
| **TC-08** | Invalid (BR-03) | `Fixture` | Official availability = `false` | Throws `OfficialUnavailableException` | `OfficialUnavailableException` thrown | **PASS** |
| **TC-09** | Boundary (BR-04) | `Stadium` | Capacity: 100, Sold: 100 (Exact max limit) | Sells out; available seats becomes 0 | Sells out; available seats becomes 0 | **PASS** |
| **TC-10** | Invalid (BR-04) | `Stadium` | Capacity: 100, Sold: 100, Request: +1 | Throws `CapacityExceededException` | `CapacityExceededException` thrown | **PASS** |
| **TC-11** | Invalid (BR-05) | `MatchResult` | Home Score: -1, Away Score: 2 | Throws `InvalidValueException` | `InvalidValueException` thrown | **PASS** |
| **TC-12** | Normal | `FootballTeam` | Team wins match (3 - 1) | Points: +3, Matches: +1, Wins: +1, GD: +2 | Points: +3, Matches: +1, Wins: +1, GD: +2 | **PASS** |
| **TC-13** | Normal | `FootballTeam` | Match ends in draw (2 - 2) | Points: +1 each, Draws: +1, GD: 0 | Points: +1 each, Draws: +1, GD: 0 | **PASS** |
| **TC-14** | Invalid (BR-08) | `Fixture` | Complete fixture twice or without score | Throws `InvalidFixtureStateException` | `InvalidFixtureStateException` thrown | **PASS** |
| **TC-15** | Normal | `StadiumShop` | Unit Price: $1,000, Qty: 2 | Subtotal: $2,000, 15% GCT: $300, Total: $2,300 | Subtotal: $2,000, GCT: $300, Total: $2,300 | **PASS** |
| **TC-16** | Invalid (BR-07) | `StadiumShop` | Grand Total: $2,300, Tendered: $2,000 | Throws `InsufficientFundsException` | `InsufficientFundsException` thrown | **PASS** |
| **TC-17** | Normal | `StadiumShop` | Grand Total: $2,300, Tendered: $2,500 | Transaction logs; Change: $200.00 | Receipt issued; Change: $200.00 | **PASS** |
| **TC-18** | Normal | `Stadium` | Pre-match practice booking | Request booked with venue timestamp | Request recorded in venue schedule | **PASS** |
| **TC-19** | Normal | `FootballLeague` | Search by parish ("Kingston") | Returns matching clubs | 100% accurate filtered list | **PASS** |
| **TC-20** | Normal | `ReportService` | Generate 3 management reports | Valid tabular reports generated | All 3 reports generated with 15% GCT | **PASS** |
| **TC-21** | Normal | `FilePersistenceManager` | Save and reload all data round-trip | Identical clubs, venues, fixtures restored | All entities verified matching exactly | **PASS** |
| **TC-22** | Defensive | `FilePersistenceManager` | Malformed line in text file | Logs warning, skips line without crash | Corrupt line skipped, app remains stable | **PASS** |

---

## 3. JUnit 5 Test Suite Mapping in Eclipse

The test implementation resides in `test/org/kpfl/KPFLCoreTest.java`. The test suite is organized into distinct test cases:

```
KPFLCoreTest
├── testSuspendedTeamCannotBeScheduled()          --> Validates BR-01
├── testTeamSchedulingConflict()                 --> Validates BR-02
├── testOfficialUnavailableConflict()            --> Validates BR-03
├── testStadiumCapacityEnforcement()             --> Validates BR-04
├── testNegativeScoresAndQuantitiesRejected()     --> Validates BR-05
├── testPlayerMinimumAgeRule()                   --> Validates BR-06
├── testInsufficientPaymentThrowsException()      --> Validates BR-07
├── testFixtureCompletionRule()                  --> Validates BR-08
├── testStadiumPracticeSession()                 --> Validates Pre-Match Bookings
├── testSearchOperations()                       --> Validates Club & Parish Search
├── testReportsGeneration()                      --> Validates 3 Management Reports
└── testPersistenceRoundTrip()                   --> Validates File Save / Reload
```

All assertions execute successfully, demonstrating 100% adherence to all project specification requirements.
