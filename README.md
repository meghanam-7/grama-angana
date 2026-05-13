# Grama-Angana 🏛️
### Community Space Manager Android App

A smart Android application built to digitize and streamline **community hall (Angana / Samudaya Bhavana) booking and management** for villages and local communities.

This project was developed as part of the **MindMatrix Internship – Android App Development using Generative AI (Infrastructure Track)**.

---

## 📌 Problem Statement

Community halls are often underutilized because citizens lack visibility into:
- current booking availability
- event schedules
- how to contact the key holder/admin

This leads to:
- unused public infrastructure
- manual booking conflicts
- lack of transparency
- poor community engagement

**Grama-Angana solves this by creating a transparent digital booking platform for rural communities.**

---

## ✨ Features

### 📅 Smart Booking Calendar
- Monthly and weekly calendar view
- Color-coded booking slots:
  - 🟢 Free
  - 🔴 Booked
  - 🟡 Pending
- Prevents double booking using conflict detection

---

### 📝 Booking Request System
Users can:
- submit hall booking requests
- select date and time slots
- mention purpose/event details

Admins can:
- approve or reject requests in real time

---

### 🛠 Maintenance Jar
Community-powered maintenance support:
- maintenance item listing
- pledge contribution system
- animated funding progress tracker

---

### 📢 Event Board
Displays:
- today’s events
- upcoming confirmed events
- organizer and event details

---

### 🤖 GenAI Assistant (Gemini API)
Natural language assistant for:
- checking free hall slots
- suggesting alternative timings

Example:
> "Is the community hall available this Sunday evening?"

---

## 🧱 Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | Jetpack Compose / XML |
| Architecture | MVVM + Clean Architecture |
| Database | Firebase Firestore |
| Local Storage | Room Database |
| Authentication | Firebase Authentication |
| Notifications | Firebase Cloud Messaging |
| Dependency Injection | Hilt |
| Async | Kotlin Coroutines + Flow |
| AI Integration | Gemini API |



---

## 🚀 How to Run

### 1. Clone Repository
```bash
git clone https://github.com/meghanam-7/grama-angana.git
```

### 2. Open in Android Studio

Open the project folder in Android Studio.

---

### 3. Sync Gradle

Wait for Gradle build to complete.

---

### 4. Configure Firebase

Download your `google-services.json` file from Firebase Console and place it inside:

```text
app/google-services.json
```

---

### 5. Run App

Run on:
- Android Emulator
- Physical Android Device

---

## 📂 Project Structure

```text
app/
 ├── ui/
 ├── data/
 ├── domain/
 ├── viewmodel/
 ├── firebase/
 └── utils/
```

---

## 🎯 Project Impact

This app helps:
- improve public infrastructure utilization
- increase transparency in booking
- reduce favoritism/manual allocation
- empower rural communities digitally
- encourage civic participation

---

## 👩‍💻 Developer

**Meghana M**  
B.E. Computer Science and Engineering  
ACS College of Engineering

GitHub: https://github.com/meghanam-7

---

## 📄 License

This project is developed for **educational and academic purposes only**.

---

## ⭐ Support

If you found this project useful, please consider **starring ⭐ this repository**.
