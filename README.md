# MAL2017_Assessment2

# Restaurant Management System (Android)

## Overview
This project is an Android-based Restaurant Management System developed using **Android Studio**, with **Java** for application logic and **XML** for user interface design. The application is designed as an academic project to demonstrate core Android development concepts, system design, and best practices covered in the course.

The system assists restaurant staff and users in managing daily operations such as viewing menus, placing orders, managing reservations, and receiving notifications in a structured and efficient manner.

---

## Features
- User registration and authentication
- Menu browsing and item display
- Order and reservation management
- Notification support based on user-defined preferences
- Offline data availability using local storage
- Remote data synchronization via RESTful API

---

## Technologies Used
- **Language:** Java  
- **UI Design:** XML (Material Design components)  
- **IDE:** Android Studio  
- **Local Database:** SQLite  
- **Remote Communication:** RESTful API  
- **Networking Library:** Retrofit  
- **Image Handling:** Glide  

---

## Application Architecture
The application follows a structured architecture that separates concerns between:
- **UI Layer:** Activities and XML layouts
- **Data Layer:** SQLite database and API services
- **Logic Layer:** Controllers and helper classes

Network operations are executed in **background worker threads** to prevent blocking the main UI thread and to ensure a responsive user experience.

---

## Data Management
A **hybrid data management approach** is implemented, combining local SQLite storage with remote server communication. This allows essential data such as menu items, reservations, and notifications to remain accessible even when an internet connection is unavailable. When connectivity is restored, the application synchronizes with the server to retrieve updated information.

---

## Design Practices
The development applies **SOLID principles** and common design patterns to improve code readability, maintainability, and scalability. Separation of concerns is enforced to reduce coupling between UI components and data logic.

---

## Testing
Basic testing was conducted through:
- Input validation testing
- UI interaction testing
- Network connectivity testing
- Error handling scenarios (e.g. empty fields, failed API calls)

These tests help ensure the application behaves correctly under expected and edge-case conditions.

---

## Limitations
- The application is a coursework prototype and not production-ready
- Advanced security mechanisms (e.g. HTTPS enforcement, password hashing) are limited
- UI design prioritizes functionality over visual polish

---

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
