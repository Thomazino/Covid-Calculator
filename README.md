# Covid-Calculator

> A native Android app that estimates a user's **personalized COVID-19 infection and mortality risk** from real epidemiological data.

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/Language-Java-orange?logo=java&logoColor=white)
![Database](https://img.shields.io/badge/Database-SQLite-003B57?logo=sqlite&logoColor=white)
![Build](https://img.shields.io/badge/Build-Gradle-02303A?logo=gradle&logoColor=white)

## Overview
Covid-Calculator is an Android application that turns real COVID-19 statistics into a clear, personalized risk assessment. The user enters a short health profile, and the app combines it with country-level and demographic epidemiological data — shipped locally in an SQLite database — to estimate their individual infection and mortality probabilities. Built as a practical, data-driven tool during the pandemic.

## How It Works
1. **Enter your profile** — gender, age, country, and any pre-existing conditions.
2. **The app queries a local database** of real COVID-19 statistics (cases, deaths, demographics).
3. **You get a personalized risk report** — computed and explained on-device, with links to the original data sources.

## Features
- **Infection probability** based on your country's case and population data, adjusted for gender distribution.
- **Mortality risk** weighted across country death rate, age, sex, and pre-existing conditions.
- **Pneumonia complication probability** as a secondary risk indicator.
- **COVID-19 vs. seasonal influenza** death-risk comparison for context.
- **Input validation** to keep results meaningful (e.g. realistic age range, required fields).
- **Transparency** — in-app links to the official sources the dataset was built from.

## Risk Model
- **Infection probability:** derived from country-level case and population figures, adjusted by the male/female case distribution.
- **Mortality probability:** a weighted combination of the country death rate (smoothed toward the global average), age-specific rates interpolated between 10-year brackets, sex-based ratios, and condition-specific death rates. Age interpolation avoids unrealistic jumps between groups.

## Data
The app ships with a bundled **SQLite** database (`CovidStats.db`) accessed through a custom data layer (`DatabaseAccess` / `DatabaseOpenHelper`). It contains three tables:

| Table | Contents |
|---|---|
| `Countries` | National figures — total cases, deaths, population |
| `CasesMeasurements` | Case data segmented by age bracket and sex |
| `Diseases` | Mortality rates per pre-existing condition |

Statistics were compiled from official global health and statistics sources, linked directly inside the app for transparency.

## Tech Stack
- **Java** + **Android SDK**
- **SQLite** with a custom `DatabaseOpenHelper` / `DatabaseAccess` data-access layer
- **Gradle** build system

## Project Structure
```
app/src/main/
├── java/com/example/helloapp/
│   ├── MainActivity.java        # Launcher & navigation
│   ├── UserData.java            # Profile input + validation
│   ├── Probabilities.java       # Risk calculation & results
│   ├── DatabaseAccess.java      # Query layer
│   ├── DatabaseOpenHelper.java  # SQLite setup
│   ├── Info.java / Explaining.java / Links.java
│   └── Item.java / MultiSelectionSpinner.java
├── assets/databases/CovidStats.db
└── res/layout/                  # Activity layouts
```

## Getting Started
1. Clone the repository:
   ```bash
   git clone https://github.com/Thomazino/Covid-Calculator.git
   ```
2. Open the project in **Android Studio**.
3. Let Gradle sync, then build and run on an emulator or a physical Android device.

## Disclaimer
Covid-Calculator is an **educational and statistical estimation tool**. Its results are probabilistic estimates based on aggregated public data and **are not medical advice**. For any health concern, consult a qualified medical professional.
