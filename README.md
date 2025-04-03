# Kiddit - Social Networking Website

## Project Overview
Kiddit is a social networking website designed for children to help them learn about different cultures, develop good internet etiquette, and reduce online hate speech. This project is developed using **Angular** (frontend) and **Spring Boot** (backend).

## Installation and Setup

### 1. Clone the Project
```sh
git clone https://github.com/your-repository/kiddit.git
cd kiddit
```

### 2. Frontend Setup

#### Install Node.js and npm
Download and install Node.js from [Node.js Official Website](https://nodejs.org/). npm comes bundled with Node.js.

#### Install Frontend Dependencies
```sh
cd frontend
npm install
```

**Angular Official Documentation**: [Angular Framework](https://angular.io/)

### 3. Backend Setup

#### Install Maven
Download and install Apache Maven from [Maven Official Website](https://maven.apache.org/).

#### Install Backend Dependencies
```sh
cd backend
mvn install
```

### 4. Run the Project

#### Start the Frontend
```sh
cd frontend
ng serve
```

#### Start the Backend
```sh
cd backend
mvn spring-boot:run
```

### 5. Database Management
This project uses **MySQL** as the database. Please use **MySQL Workbench 8.0** to connect to the database and view data.

### 6. API Testing

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

