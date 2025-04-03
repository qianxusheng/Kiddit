# Kiddit - Social Networking Website

## Project Overview
Kiddit is a social networking website designed for children to help them learn about different cultures, develop good internet etiquette, and reduce online hate speech. This project is developed using **Angular** (frontend) and **Spring Boot** (backend).

### Backend Generation
The backend of this project was generated using **Spring Initializr**. You can learn more or generate your own Spring Boot project at [Spring Initializr](https://start.spring.io/).

### Technologies & Dependencies Used
- **Java 17** (Backend Language)
- **Angular CLI 19.1.6** (Frontend Framework)
- **Spring Boot Web** (RESTful APIs)
- **Spring Data JPA** (Database Access)
- **Lombok** (Reducing Boilerplate Code)
- **MySQL Server** (Database Management)
- **Node.js** (Latest Version Recommended)
- **Maven** (Latest Version Recommended)

## Installation and Setup

### 1. Clone the Project
```sh
git clone https://github.com/qianxusheng/Kiddit.git
cd kiddit
```

### 2. Project Structure
- The **Kiddit** folder(original folder) contains all backend-related files.
- Inside this folder, the **Web** folder is the Angular frontend project.
- All other files outside **Web** folder are related to the backend.
- The **src** folder inside the backend contains backend source code.
- The **pom.xml** file in the backend folder manages backend dependencies.

### 3. Frontend Setup

#### Install Node.js and npm
Download and install Node.js (latest recommended version) from [Node.js Official Website](https://nodejs.org/). npm comes bundled with Node.js.

#### Install Frontend Dependencies
```sh
cd /kiddit/web
npm install
```

**Angular Official Documentation**: [Angular Framework](https://angular.io/)

### 4. Backend Setup

#### Install Maven
Download and install Apache Maven (latest recommended version) from [Maven Official Website](https://maven.apache.org/).

#### Install Backend Dependencies
```sh
cd kiddit
mvn install
```

### 5. Run the Project

#### Start the Frontend
```sh
cd /kiddit/web
ng serve
```

#### Start the Backend
```sh
cd kiddit
mvn spring-boot:run
```

### 6. Database Management
This project uses **MySQL** as the database. Please use **MySQL Workbench 8.0** to connect to the database and view data.

### 7. API Testing

#### Use Postman for API Testing
Postman is a popular API testing tool. You can download and install it from [Postman Official Website](https://www.postman.com/).

**Example API Requests** (Test using Postman or curl):

- **User Registration**
```sh
POST http://localhost:8080/api/register
Content-Type: application/json
{
  "username": "testuser",
  "password": "password123",
  "email": "testuser@example.com"
}
```

- **User Login**
```sh
POST http://localhost:8080/api/login
Content-Type: application/json
{
  "username": "testuser",
  "password": "password123"
}
```

- **Get User List**
```sh
GET http://localhost:8080/api/users
```

## Contribution Guidelines
If you want to contribute to Kiddit, please fork this project and submit a Pull Request.

## License
This project follows the MIT license. Please refer to the LICENSE file for details.

---
Thank you for your support! Enjoy using Kiddit! 🚀

