Campus Course & Records Manager (CCRM)

A lightweight Java application to manage students, courses, enrollments, grades, and transcripts. CCRM includes both a graphical user interface (GUI) and a command‑line interface (CLI).

## Table of Contents
- Features
- Requirements
- Getting Started
  - Build
  - Run (GUI)
  - Run (CLI)
- Data & Persistence
- Project Structure
- Troubleshooting
- Contributing

## Features
- Student Management: add , edit, list, and search student records
- Course Management: create and modify courses, set enrollment capacity
- Enrollment: register and manage student enrollments per course
- Grade Management: assign grades to enrolled students
- Transcripts: generate per‑student transcripts and GPA
- File Utilities: import/export and backup data
- Reports & Statistics: totals , counts, and GPA summaries

## Requirements
- Java 8+  (JDK)
- Windows, macOS or Linux (tested primarily on Windows)

## Getting Started
### Build
From the project root, compile the sources :

```bash
javac *.java
```

This will produce `.class` files alongside the sources.

### Run (GUI)
Launch the GUI application :

```bash
java CCRMApp
```

### Run (CLI)
Launch the text‑based menu system:

```bash
java CCRMApp --cli
```

## Data & Persistence
- Working data is stored under `data/` (for example, `data/students`, `data/students.csv`).
- Automatic/manual backups may be placed under `backups/` (for example, `backups/students_*.dat`, `backups/courses_*.dat`)
- Ensure the app has read/write access to these directories when running

## Project Structure
- `CCRMApp.java`: Application entry point, decides GUI vs CLI based on arguments
- `CCRMGui.java`: Swing‑based GUI with tabbed views and tables
- `CCRMSystem.java`: CLI flow, menus, and user input handling
- `Course.java`: Course domain model and enrollment logic
- `Student.java`: Student domain model, grades, and transcript utilities
- `FileManager.java`: Save/load utilities for persistence and backups
- `data/`: Runtime data files (CSV and/or serialized data)
- backups/: Timestamped backup snapshots

## Troubleshooting
- "java is not recognized": Install the JDK and ensure `java` and `javac` are on your PATH.
- Classes not found after editing code: Rebuild with `javac *.java` before running
- Permission issues on save/load: Verify your user has write access to `data/` and `backups/`.
- Corrupted data: Restore from a file in `backups/` by copying it into data/

## Contributing
- Keep code readable and consistent with existing style.
- Prefer clear, descriptive names for classes, methods, and variables.
- Test both GUI and CLI flows for changes that affect shared logic.

Have fun using CCRM!