# CVScreening_HR
# CV Screening System for HR

## Overview
This project implements a **CV Screening System for Human Resources** using Java and Maven.
The system supports candidate registration, job applications, automated CV screening,
interview management, notifications, and reporting.

The application follows a **layered architecture** and uses an **H2 in-memory database**
for persistence.

---

## Technologies Used
- Java 21
- Maven
- JDBC
- H2 Database
- JUnit 5
- AssertJ
- IntelliJ IDEA

---

## Architecture
The system is organized into the following layers:

- **UI Layer**
  - Console-based user interface (CLI)
- **Service Layer**
  - Business logic (screening, applications, interviews, reports)
- **DAO Layer**
  - Data access using JDBC
- **Model Layer**
  - Domain entities (Perdorues, Kandidat, CV, Aplikim, PozicionPune, etc.)
- **Database**
  - H2 in-memory database initialized via `schema.sql`

---

## Main Functionalities

### Candidate
- Register and login
- Upload CV
- View job positions
- Apply to job positions
- View application status
- Receive notifications

### Recruiter
- Login
- View applications
- Run CV screening and scoring
- Schedule interviews
- Set interview results
- Generate reports

---

## CV Screening Logic
The screening process calculates a **matching score** based on:
- Required job keywords
- Candidate skills and experience

The final score is stored in both the CV and application records.

---
