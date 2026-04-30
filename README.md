# 💳 FlowPay

<div align="center">

![FlowPay Logo](https://img.shields.io/badge/FlowPay-Fintech%20MVP-brightgreen)
![Platform](https://img.shields.io/badge/Platform-Android-blue)
![Language](https://img.shields.io/badge/Language-Kotlin-purple)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20Architecture-orange)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-blueviolet)

**A Modern Financial Management App Built for Competition**

</div>

---

## 📱 About FlowPay

FlowPay is an innovative fintech mobile application designed to simplify financial management for businesses and individuals. Built as a Minimum Viable Product (MVP) for fintech competitions, it combines cutting-edge technology with intuitive design to deliver a seamless financial experience.

### 🎯 Key Features

- **📊 Dashboard** - Real-time financial overview with balance tracking
- **👥 Client Management** - Comprehensive client database with payment tracking
- **📄 Invoice Creation** - Manual and AI-powered invoice generation
- **💰 Expense Tracking** - Categorized expense management
- **📈 Financial Reports** - Detailed analytics and insights
- **🤖 AI Assistant** - Intelligent chatbot for financial guidance
- **🔐 Secure Authentication** - Secure login and user management

---

## 🏗️ Architecture

FlowPay follows **Clean Architecture** principles with **MVVM** pattern for a scalable, maintainable, and testable codebase.

### Architecture Layers

```
┌─────────────────────────────────────────────────────────────────┐
│                        Presentation Layer                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │   Screens    │  │  ViewModels  │  │  Navigation  │         │
│  │  (Compose)   │  │   (MVVM)     │  │   (NavHost)  │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                         Domain Layer                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │   Models     │  │  Use Cases   │  │  Repository  │         │
│  │  (Entities)  │  │ (Business)   │  │  Interfaces  │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│                          Data Layer                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐         │
│  │ Repositories │  │   Data       │  │   Network    │         │
│  │ (Impl)       │  │  Sources     │  │   (API)      │         │
│  └──────────────┘  └──────────────┘  └──────────────┘         │
└─────────────────────────────────────────────────────────────────┘
```

### Design Patterns

- **MVVM (Model-View-ViewModel)** - Separates UI from business logic
- **Repository Pattern** - Abstracts data sources
- **Use Case Pattern** - Encapsulates business logic
- **Dependency Injection** - Hilt for managing dependencies
- **Unidirectional Data Flow** - Predictable state management

---

## 🛠️ Tech Stack

### Core Technologies

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Kotlin | Latest Stable |
| **Min SDK** | Android 8.0 (API 26) | 26+ |
| **Target SDK** | Android 14 (API 34) | 34 |
| **Build System** | Gradle (Kotlin DSL) | 8.x |

### UI Framework

| Component | Technology |
|-----------|-----------|
| **UI Toolkit** | Jetpack Compose (Material 3) |
| **Navigation** | Jetpack Navigation Compose |
| **State Management** | StateFlow, Flow |
| **Image Loading** | Coil |
| **Animations** | Compose Animation |

### Architecture & DI

| Component | Technology |
|-----------|-----------|
| **Architecture** | Clean Architecture |
| **Pattern** | MVVM |
| **DI Framework** | Hilt (Dagger) |
| **Async** | Coroutines + Flow |

### Data Layer

| Component | Technology |
|-----------|-----------|

| **Networking** | Retrofit + OkHttp |

### Security

| Component | Technology |
|-----------|-----------|

| **API Security** | Auth Interceptor |

---

## 📁 Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/abdallamusa/flowpay/
│   │   │   ├── data/
│   │   │   │   ├── repository/          # Repository implementations
│   │   │   │   │   ├── FakeFlowPayRepositoryImpl.kt
│   │   │   │   │   ├── AiChatRepositoryImpl.kt
│   │   │   │   │   └── ExpenseRepositoryImpl.kt
│   │   │   │   └── ...
│   │   │   ├── domain/
│   │   │   │   ├── model/               # Domain entities
│   │   │   │   │   ├── User.kt
│   │   │   │   │   ├── Invoice.kt
│   │   │   │   │   ├── Expense.kt
│   │   │   │   │   ├── Client.kt
│   │   │   │   │   ├── ChatMessage.kt
│   │   │   │   │   └── ExpenseCategory.kt
│   │   │   │   ├── repository/          # Repository interfaces
│   │   │   │   ├── usecase/             # Business logic use cases
│   │   │   │   │   └── GenerateAiInvoiceUseCase.kt
│   │   │   │   └── ...
│   │   │   ├── presentation/
│   │   │   │   ├── components/         # Reusable UI components
│   │   │   │   │   ├── CustomTextField.kt
│   │   │   │   │   ├── FlowPayTopAppBar.kt
│   │   │   │   │   └── FlowPayBottomBar.kt
│   │   │   │   ├── dashboard/           # Dashboard screen
│   │   │   │   │   └── DashboardScreen.kt
│   │   │   │   ├── clients/             # Clients screen
│   │   │   │   │   └── ClientsScreen.kt
│   │   │   │   ├── invoice/             # Invoice screens
│   │   │   │   │   └── CreateInvoiceScreen.kt
│   │   │   │   ├── expense/             # Expense tracking
│   │   │   │   │   └── ExpenseTrackerScreen.kt
│   │   │   │   ├── aichat/              # AI chat screen
│   │   │   │   │   └── AiChatScreen.kt
│   │   │   │   ├── reports/             # Reports screen
│   │   │   │   │   └── ReportsScreen.kt
│   │   │   │   ├── auth/                # Authentication
│   │   │   │   │   └── AuthScreen.kt
│   │   │   │   ├── splash/              # Splash screen
│   │   │   │   │   └── SplashScreen.kt
│   │   │   │   ├── viewmodel/           # ViewModels
│   │   │   │   │   ├── ReportsViewModel.kt
│   │   │   │   │   ├── ClientsViewModel.kt
│   │   │   │   │   ├── CreateInvoiceViewModel.kt
│   │   │   │   │   ├── ExpenseTrackerViewModel.kt
│   │   │   │   │   ├── AiChatViewModel.kt
│   │   │   │   │   └── AiSmartInvoiceViewModel.kt
│   │   │   │   ├── navigation/          # Navigation setup
│   │   │   │   │   └── FlowPayNavigation.kt
│   │   │   │   └── ...
│   │   │   ├── ui/
│   │   │   │   └── theme/               # App theme
│   │   │   │       ├── Color.kt
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   └── utils/
│   │   │       └── Strings.kt           # String resources
│   │   └── res/                         # Android resources
├── build.gradle.kts                      # App-level build config
├── proguard-rules.pro                   # ProGuard rules
└── .gitignore
```

---

## 🧭 Navigation Structure

### Overview
All screens are properly connected through `FlowPayNavGraph` using Jetpack Navigation Compose.

### Navigation Flow

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            ROOT NAVIGATION                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ┌──────────────┐     ┌──────────────┐     ┌──────────────────────────┐   │
│   │   Splash     │────▶│    Auth      │────▶│         Main             │   │
│   │   Screen     │     │   Screen     │     │    (Bottom Nav Host)     │   │
│   └──────────────┘     └──────────────┘     └──────────────────────────┘   │
│                                                        │                    │
│                              ┌─────────────────────────┘                    │
│                              ▼                                              │
│                   ┌────────────────────┐                                    │
│                   │   Sub-screens      │                                    │
│                   │  (Full Screen)     │                                    │
│                   ├────────────────────┤                                    │
│                   │ • CreateInvoice    │                                    │
│                   │ • AiSmartInvoice   │                                    │
│                   │ • ExpenseTracker   │                                    │
│                   └────────────────────┘                                    │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────┐
│                         BOTTOM NAVIGATION (Main)                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│    ┌─────────┐   ┌─────────┐   ┌─────────┐   ┌─────────┐                  │
│    │Dashboard│   │ Clients │   │ Reports │   │ AI Chat │                  │
│    │  (Home) │   │         │   │         │   │         │                  │
│    └────┬────┘   └─────────┘   └─────────┘   └─────────┘                  │
│         │                                                                   │
│    ┌────┴────┐                                                              │
│    │ Actions │                                                              │
│    ├─────────┤                                                              │
│    │• Receive│───▶ CreateInvoice                                            │
│    │• Send   │───▶ ExpenseTracker                                           │
│    └─────────┘                                                              │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Route Definitions

| Route | Screen | Description |
|-------|--------|-------------|
| `splash` | Splash | App entry point with logo |
| `auth` | Auth | Login/Register screen |
| `main` | Main | Bottom nav host screen |
| `dashboard` | Dashboard | Home with balance, actions |
| `clients` | Clients | Client management |
| `reports` | Reports | Financial reports |
| `ai_chat` | AI Chat | AI assistant |
| `create_invoice` | CreateInvoice | Manual invoice creation |
| `ai_smart_invoice` | AiSmartInvoice | AI-powered invoice |
| `expense_tracker` | ExpenseTracker | Add expenses |

### Navigation Details

**1. Splash → Auth → Main**
- Splash navigates to Auth after delay
- Auth navigates to Main on successful login
- Previous screens are popped from backstack

**2. Bottom Navigation (4 Tabs)**
| Tab | Route | Screen |
|-----|-------|--------|
| الرئيسية | `dashboard` | DashboardScreen |
| العملاء | `clients` | ClientsScreen |
| التقارير | `reports` | ReportsScreen |
| المساعد | `ai_chat` | AiChatScreen |

**3. Dashboard Actions → Sub-screens**
- Receive button → `create_invoice` (CreateInvoiceScreen)
- Send button → `expense_tracker` (ExpenseTrackerScreen)

**4. Sub-screens → Back**
- All sub-screens call `navController.popBackStack()` on completion
- Returns to previous bottom nav tab

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or later
- Android SDK 34
- Minimum SDK 26 (Android 8.0)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/FlowPay.git
cd FlowPay
```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for the sync to complete

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the Run button in Android Studio
   - Or use command line:
```bash
./gradlew installDebug
```

---

## 🎨 UI/UX Highlights

### Design System
- **Material 3** - Latest Material Design guidelines
- **Dark Theme** - Modern dark mode support
- **Glassmorphism** - Glass-like card effects
- **Arabic RTL** - Full right-to-left support
- **Custom Components** - Reusable UI components

### Key Screens

- **Dashboard** - Financial overview with balance cards and quick actions
- **Clients** - Client list with payment status indicators
- **Reports** - Visual charts and financial insights
- **AI Chat** - Conversational AI assistant with streaming responses
- **Invoice Creation** - Form-based invoice generation
- **Expense Tracker** - Categorized expense entry with dropdown selection


---

## 📊 Data Models

### Core Entities

- **User** - User profile and authentication
- **Invoice** - Invoice with status tracking (DRAFT, PENDING, PAID, CANCELLED)
- **Expense** - Expense with category (FOOD, TRANSPORT, ENTERTAINMENT, BILLS, OTHER)
- **Client** - Client information with payment status
- **ChatMessage** - AI chat messages with timestamps
- **Transaction** - Financial transaction records


---

## 📦 Dependencies

Key dependencies managed via Version Catalog (`gradle/libs.versions.toml`):

```kotlin
[versions]
compose = "1.5.4"
hilt = "2.48"

retrofit = "2.9.0"
kotlinx-coroutines = "1.7.3"
```

---

## 🤝 Contributing

This is an MVP project for fintech competition. Contributions are welcome!

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👨‍💻 Author

**Abdallamusa**
- Fintech Competition MVP
- Built with ❤️ using Kotlin & Jetpack Compose

---

## 🙏 Acknowledgments

- Google for Jetpack Compose and Android best practices
- Material Design team for design guidelines
- Open-source community for amazing libraries

---

<div align="center">

**Built for the Future of Finance** 🚀

</div>
