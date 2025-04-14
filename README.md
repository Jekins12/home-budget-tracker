# Home Budget Tracker

**Home Budget Tracker** is a Kotlin Android app built using Jetpack Compose. It allows you to track household expenses and bus rides on a daily basis. Data is stored locally in JSON files instead of a traditional database, making it lightweight and offline-friendly.

## ✨ Features

- 📅 Daily expense logging for two apartments:
  - Electricity, rent, gas, and other expenses
- 🚗 Car tracking:
  - Fuel cost and mileage
- 🚍 Public transport tracking:
  - Bus number and arrival time per day
- 📊 Monthly summaries with category totals
- 📆 Detailed day-by-day views
- ✏️ Edit or delete any day or ride
- 🗃 Data saved in local JSON files
- 🇵🇱 Polish interface

## 🛠 Tech Stack

- Kotlin
- Jetpack Compose (Material 3)
- ViewModel + State
- Local JSON-based storage (no database)
- Android SDK 34+

## 📁 Structure

- `ui/screens/` – UI for main sections (Budget, Rides, Summary)
- `data/model/` – Data models for expenses and bus rides
- `data/storage/` – Local storage manager for JSON persistence
- `ui/components/` – Reusable components (dialogs, pickers)

## 🚀 Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/home-budget-tracker.git
   ```
2. Open in Android Studio (Electric Eel+)
3. Build & run on Android 8+ (API 26+)

## 📌 TODO / Planned Features

- Export to CSV or PDF
- Multi-language support (EN/PL)
- Cloud sync (optional)
- Graphs & charts for trends

## 📸 Screenshots

_(Coming soon)_

## 📄 License

MIT License. Free to use and modify.

---

Made with ❤️ for practical home budgeting.
