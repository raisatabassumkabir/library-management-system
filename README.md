# Library Management System

A comprehensive web-based application designed to streamline library operations for universities and educational institutions. This system manages the entire lifecycle of library resources, from book acquisition to member management and circulation, featuring a modern, glassmorphism-inspired UI.

## 🎥 Project Demo

<!--
    Instructions for User:
    1. Go to this file on GitHub.
    2. Click the 'Edit' (pencil) icon.
    3. Drag and drop your video file right here.
    4. GitHub will upload it and generate a link.
    5. Commit the changes.
-->

## 🚀 Key Features

### 🔐 Unified Login System
- **Single Entry Point**: Unified login interface for Admins, Students, and Teachers.
- **Role-Based Redirection**: Automatically directs users to their specific dashboard based on their role.

### 👨‍💼 Admin Panel
- **Interactive Dashboard**: Real-time overview of total books, issued books, and active members with dynamic charts.
- **Inventory Management**: Add, update, and delete books with tracking for total and remaining quantities.
- **Live Search**: "YouTube-like" instant search for books and defaulters without page reloads.
- **Member Directory**: Manage student and teacher accounts with visual distinction (Blue for Students, Purple for Teachers).
- **Circulation**: Streamlined process for issuing and returning books.
- **Defaulter Management**: Auto-detection of overdue returns and fine management.

### 👨‍🎓 User Portals (Student & Teacher)
- **Personal Dashboard**: View borrowed books, due dates, and fine status.
- **Book Search**: Browse the library catalog instantly.
- **Book Reviews & Ratings**: Rate and review books you've borrowed.
- **Profile Management**: Update personal details and contact information.

### 🌐 Public Services
- **Digital Archive**: Access to electronic resources.
- **Study Room Booking**: Information on facility availability.
- **Community Events**: Updates on library workshops and events.

## 🛠️ Technology Stack

- **Backend**: Java 22, Spring Boot 3.4.4
- **Security**: Spring Security (Role-based access control)
- **Database**: MySQL, Spring Data JPA
- **Frontend**: Thymeleaf, HTML5, Tailwind CSS
- **Build Tool**: Maven

## ⚙️ Setup & Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/yourusername/library-management-system.git
    ```

2.  **Configure Database**
    Update `src/main/resources/application.properties` with your MySQL credentials:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/library_management_system
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    ```

3.  **Run the Application**
    ```bash
    mvn spring-boot:run
    ```

4.  **Access the App**
    Open your browser and navigate to: `http://localhost:8080`

## 🔐 Default Access
- **Login URL**: `/` or `/login`
- **Credentials**: Use the registration page to create a new account, or seed the database with an admin user.

## 📝 License
This project is open-source and available for educational purposes.
