# Library Management System

A comprehensive web-based application designed to streamline library operations for universities and educational institutions. This system manages the entire lifecycle of library resources, from book acquisition to member management and circulation.

## 🚀 Key Features

### 👨‍💼 Admin Panel
- **Dashboard**: Real-time overview of total books, issued books, and active members.
- **Inventory Management**: Add, update, and delete books with tracking for total and remaining quantities.
- **Live Search**: "YouTube-like" instant search for books and defaulters without page reloads.
- **Member Directory**: Manage student and teacher accounts with visual distinction (Blue for Students, Purple for Teachers).
- **Circulation**: Streamlined process for issuing and returning books.
- **Defaulter Management**: Auto-detection of overdue returns and fine management.

### 👨‍🎓 User Portals (Student & Teacher)
- **Personal Dashboard**: View borrowed books, due dates, and fine status.
- **Book Search**: Browse the library catalog instanly.
- **Profile Management**: Update personal details and contact information.

### 🌐 Public Services
- **Digital Archive**: Access to electronic resources.
- **Study Room Booking**: Information on facility availability.
- **Community Events**: Updates on library workshops and events.

## 🛠️ Technology Stack

- **Backend**: Java, Spring Boot 3.4
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

## 🔐 Default Admin Access
*(Replace with actual default credentials if applicable, or instructions to create an admin)*
- **Login URL**: `/admin`

## 📝 License
This project is open-source and available for educational purposes.
