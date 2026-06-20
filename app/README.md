# CP3406 Assignment 1: Utility App Development

## Project Overview
This is a utility-style mobile application developed for CP3406 Assignment 1. The app provides focused, "at-a-glance" daily functionality by integrating real-time weather forecasting with Gaode Map location services. It aims to deliver rapid and structured lifestyle data to the user through a clean and responsive interface.

## Core Features
* **Secure Authentication**: Includes robust "Registration" and "Login" workflows with phone number format validation and a reactive verification code countdown.
* **Location & Map Services**: A real-time location-aware interface powered by the Gaode Map SDK.
* **Weather Dashboard**: Displays detailed daily metrics (temperature, wind speed/direction) alongside responsive expandable future weather forecast panels.
* **Personalized Recommendations**: Features a dynamic QR code generator for user-specific utility.
* **Settings & History**: A comprehensive panel allowing users to adjust visible history counts via a Slider, switch default cities, modify passwords, and perform secure logouts.

## Technical Implementation
This application was built strictly following clean Android architecture principles:
* **Architecture**: Implemented the **MVVM (Model-View-ViewModel)** and **Repository Pattern** to securely manage data flows and UI states across lifecycle events.
* **User Interface**: Developed using the **Traditional Android View System (XML Layouts and Fragments)** and styled with **Material Design 3** typography and components.
* **Networking**: Utilized **Retrofit** to parse dynamic JSON payloads from third-party weather and district APIs.
* **Local Data**: Handled persistent local states (like user history and settings) using modern Android database practices (e.g., Room).

## Ethical Reflection Note
During development, the integration of highly localized third-party data feeds (returning single-language JSON payloads) highlighted the engineering challenges of multi-language adaptation. This project served as a practical case study in the critical importance of digital inclusivity, accessibility, and early architectural planning for global scalable apps.

---
*Developed for CP3406 - Information Technology*