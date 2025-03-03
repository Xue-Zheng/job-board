# E tū Union Job Board System

This is a simple job board system prototype developed for the E tū union, allowing union members to browse job listings and submit applications, while enabling employers to post jobs and manage applications.

## System Architecture

### Technology Stack
- **Backend**: Java 17 + Spring Boot 3.1.5
- **Frontend**: Thymeleaf + Bootstrap 5 + JavaScript
- **Database**: H2 in-memory database
- **Build Tool**: Maven

### Core Components
- **User Management**: Supports registration and authentication for multiple roles (job seekers, employers, administrators)
- **Job Management**: Creation, editing, viewing, and status management of jobs
- **Application Management**: Submission, viewing, and status updates of job applications

### System Structure
- **Controller Layer**: Handles HTTP requests, renders views
- **Service Layer**: Contains business logic
- **Repository Layer**: Data access and persistence
- **Model Layer**: Domain models of the application
- **View Layer**: Thymeleaf-based templates

## Installation and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Building and Running the Application
1. Clone the project repository
   ```bash
   git clone [repository address]
   cd job-board
   ```

2. Build the project with Maven
   ```bash
   mvn clean package
   ```

3. Run the application
   ```bash
   java -jar target/job-board-0.0.1-SNAPSHOT.jar
   ```

4. Access the application
   Open a browser and visit http://localhost:8080

## System Features

### User Functionality
- **Job Seekers**:
    - Browse and search jobs
    - Apply for jobs
    - View application status
    - Withdraw applications

- **Employers**:
    - Post new jobs
    - Manage posted jobs
    - View and process applications

- **Administrators**:
    - Manage all jobs and applications
    - System monitoring and user management

## Default Accounts

The system has the following pre-configured test accounts, all with the password: `password`

- **Administrator**: admin@etu.nz
- **Employer**: employer@etu.nz
- **Job Seeker 1**: jobseeker1@etu.nz
- **Job Seeker 2**: jobseeker2@etu.nz

## H2 Database Console

H2 console (for viewing/operating the database):

H2 console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:jobboarddb  
Username: sa  
Password: password

## System Security Measures

- Password storage with BCrypt encryption
- Role-based access control
- CSRF protection
- Input validation and sanitization

## Future Improvements

- Implement job search and filtering functionality
- Add notification system
- Enhance user profile management
- Integrate resume upload functionality
- Migrate to PostgreSQL database
- Implement front-end/back-end separation architecture
