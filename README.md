# iOS Calculator for Android

A clean, iOS-style calculator app built with Kotlin and Jetpack Compose. Features basic/scientific calculator, unit converter, currency converter, and calculation history.

## Features

- **Basic Calculator**: Standard arithmetic operations (+, −, ×, ÷, %)
- **Scientific Calculator**: Trigonometric (sin, cos, tan), logarithmic (ln, log), exponential (eˣ, 10ˣ), power (xʸ, x², √), factorial (x!), constants (π, e)
- **Angle Modes**: Degrees (DEG) and Radians (RAD)
- **Inverse Functions**: Toggle for inverse trigonometric functions
- **Memory**: M+, M−, MR, MC
- **Unit Converter**: Length, Area, Volume, Weight, Temperature, Time (50+ units)
- **Currency Converter**: 160+ currencies with offline cached rates
- **History**: Persistent calculation history with Room database
- **Settings**: Theme (Light/Dark/System), Angle mode
- **iOS-style UI**: Dark theme by default, circular buttons, haptic feedback

## Screenshots

<!-- Add screenshots here -->

## Architecture

```
app/
├── engine/           # CalculatorEngine - core math logic
├── converter/        # ConverterEngine - unit conversion
├── currency/         # CurrencyEngine - currency conversion
├── data/             # Room database (HistoryEntry, HistoryDao)
├── preferences/      # DataStore preferences
├── ui/
│   ├── theme/        # Material3 theme (iOS colors)
│   ├── components/   # Reusable UI components
│   └── screen/       # Compose screens
└── MainActivity.kt   # Navigation host
```

## Building

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 21
- Android SDK 34

### Local Build
```bash
./gradlew assembleDebug
# APK at: app/build/outputs/apk/debug/app-debug.apk
```

### CI/CD (GitHub Actions)
Push to `main` branch or create a tag to trigger automatic builds.
Debug APK uploaded as artifact on every push.
Release APK attached to GitHub Releases on tag push.

## Dependencies

- **Kotlin** 1.9.24
- **Jetpack Compose** 2024.05.00 (Material3)
- **Navigation Compose** 2.8.3
- **Room** 2.6.1
- **DataStore** 1.1.1
- **Coroutines** 1.8.1
- **Ktor Client** 2.3.11 (for future API integration)
- **Kotlinx Serialization** 1.6.3

## License

MIT License - Feel free to use and modify.

## Credits

- Calculator logic inspired by iOS Calculator and Transsion Calculator analysis
- Icons from Material Icons
- Built with ❤️ using Android Studio