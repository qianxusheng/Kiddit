# Kiddit - Social Networking Website
## Notice
Please use your own database and ChatGPT API in the **application.properties** file (backend).

## Project Overview
Kiddit is a social networking website designed for children to help them learn about different cultures, develop good internet etiquette, and reduce online hate speech. This project is developed using **Angular** (frontend) and **Spring Boot** (backend).

### Backend Generation
The backend of this project was generated using **Spring Initializr**. You can learn more or generate your own Spring Boot project at [Spring Initializr](https://start.spring.io/).

### Technologies & Package Management Tools & Main Dependencies Used
- **Java 17** (Backend Language)
- **Angular CLI 19.1.6** (Frontend Framework)
- **Spring** (Backend Framework)
- **Node.js** (Latest Version Recommended, need npm for package management)
- **Maven** (Latest Version Recommended, for package management)
- **Spring Web** (RESTful APIs)
- **Spring Data JPA** (Database Access)
- **Spring Security** (JWT Authentication)
- **Lombok** (Reducing Boilerplate Code)
- **MySQL Server** (Database Management)

## Installation and Setup

### 1. Clone the Project
```sh
git clone https://github.com/qianxusheng/Kiddit.git
cd kiddit
```

### 2. Project Structure
The `Kiddit` folder contains all backend-related files.

Inside this folder, the `Web` folder is the Angular frontend project.

All other files outside the `Web` folder are related to the backend.

The `src` folder inside the backend contains backend source code.

The `pom.xml` file in the backend folder manages backend dependencies.

The `package.json` file in the `Web` folder manages front dependencies.


### 3. Frontend Setup
#### Install Node.js and npm
Download and install **Node.js** (latest recommended version) from [Node.js Official Website](https://nodejs.org/en) **npm** comes bundled with Node.js.

#### Install Frontend Dependencies
```sh
cd kiddit/web
npm install
```
Refer to the [Angular Official Documentation](https://angular.dev/tutorials/learn-angular) for more details.

### 4. Backend Setup
#### Install Maven
Download and install **Apache Maven** (latest recommended version) from [Maven Official Website](https://maven.apache.org/download.cgi).

#### Install Backend Dependencies
```sh
cd kiddit
mvn install
```

### 5. Run the Project
#### Start the Frontend
```sh
cd kiddit/web
ng serve
```

#### Start the Backend
```sh
cd kiddit
mvn spring-boot:run
```

### 6. Database Management & Backend Configuration
This project uses **MySQL** as the database. Please use [MySQL Workbench 8.0](https://dev.mysql.com/downloads/workbench/) to connect to the database and view data.

The backend uses a MySQL database, and the connection settings are specified in the `application.properties` file:

```properties
spring.application.name=Kiddit

spring.datasource.url=**********************
spring.datasource.username=**********************
spring.datasource.password=**********************

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```
Ensure that your MySQL server is running and properly configured to accept connections.

### 7. API Testing
Use **Postman** for API Testing. [Postman](https://www.postman.com/) is a popular API testing tool. You can download and install it from Postman Official Website.

#### Example API Requests (Test using Postman or curl):

**User Registration**
```sh
POST http://localhost:8080/api/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "testuser@example.com"
}
```

**User Login**
```sh
POST http://localhost:8080/api/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

**Get User List**
```sh
GET http://localhost:8080/api/users
```

### 8. Development Standards
#### API Response Standard
To ensure consistency and maintainability in data transmission between the frontend and backend, all backend APIs must adhere to the following standard response format. This format includes three main fields: status, message, and data.

- **status**: The status code of the request processing, using HTTP status codes or custom status codes. Common status codes include:
  - 200: Request was successful.
  - 400: Bad request or invalid input.
  - 500: Internal server error.
- **message**: The response message, providing a brief description of the status or error, to be processed or displayed to the user on the frontend.
- **data**: The data portion of the response, containing the actual data returned by the API. If the request is successful, data will contain the relevant information; if the request fails, data can be null.

#### Example of Successful Response
When a request is successful, the response format should be as follows:

```json
{
  "status": 200,
  "message": "Request was successful",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "testuser@example.com"
  }
}
```

### 9. Contribution Guidelines
If you want to contribute to Kiddit, please fork this project and submit a Pull Request.

### 10. License
This project follows the **MIT license**. Please refer to the LICENSE file for details.

