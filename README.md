# Swift Flyers Workout App (Kotlin)

This repository contains a Jetpack Compose based Android application that mirrors the core flows of the [Swift Flyers Weight Loss Workout](https://play.google.com/store/apps/details?id=swiftflyers.weightloss.workout.athome) experience. It provides:

- Curated workout collections with detail screens
- A structured multi-day training program overview
- Progress tracking summary cards for motivation

## Project Structure

```
Workout-App/
├── app/
│   ├── build.gradle.kts
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/swiftflyers/weightloss/
│       │   ├── MainActivity.kt
│       │   ├── data/
│       │   │   ├── Models.kt
│       │   │   └── WorkoutRepository.kt
│       │   └── ui/theme/
│       │       ├── Color.kt
│       │       ├── Theme.kt
│       │       └── Type.kt
│       └── res/
│           ├── values/
│           │   ├── colors.xml
│           │   ├── strings.xml
│           │   └── themes.xml
│           └── xml/
│               ├── backup_rules.xml
│               └── data_extraction_rules.xml
├── build.gradle.kts
└── settings.gradle.kts
```

## Getting Started

1. Open the project in **Android Studio Giraffe** (or newer).
2. Let Gradle sync resolve dependencies.
3. Deploy the `app` module to an emulator or Android device running API 24+.

The app launches into the dashboard tab. Use the bottom navigation to explore the plan and progress sections.
