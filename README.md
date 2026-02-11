# Employee API

A RESTful API built with pure Java (Servlets & JDBC) to deeply understand backend architecture without relying on frameworks like Spring.

This project simulates a production-ready backend application, focusing on clean architecture, performance, testing, and proper separation of responsibilities.

> 🚀 **Note:** This is the **Core Java** version of the project.  
> Check out the **Spring Boot** evolution of this API here: [EmployeeAPI-Spring](https://github.com/MatheusLeiteCarneiro/EmployeeAPI-Spring)

---
## 🎯 Purpose 
The goal of this project was to master backend fundamentals before depending on high-level frameworks. Instead of relying on Spring Boot to abstract complexity, this application manually implements:
* HTTP request lifecycle handling
* Dependency boundaries between layers
* Global exception management
* Database connection pooling
* Clean separation of concerns This approach builds strong architectural foundations and a deeper understanding of enterprise backend systems.
---

## 📌 Project Overview

- **Architecture:** MVC + Service Layer + DAO + DTO  
- **Exception Handling:** Global Filter  
- **Connection Pooling:** HikariCP  
- **Database:** MySQL  
- **Testing:** JUnit 5 + Mockito + H2  
- **Server:** Apache Tomcat  

This project was intentionally built without Spring to fully understand what frameworks abstract under the hood.

---

## ✨ Features

### ✅ CRUD Operations
Complete lifecycle management for Employees:
- Create
- Read
- Update
- Delete

### ✅ Pagination
The `GET /employee` endpoint supports:

```
?page=1&size=10
```

Implemented using optimized SQL with `LIMIT` and `OFFSET`.

---

### ✅ High-Performance Database Access

- **HikariCP Connection Pool**
  - Avoids expensive connection creation per request
  - Reuses pre-initialized connections
  - Reduces latency and improves scalability

- **Prepared Statements**
  - Prevents SQL Injection
  - Ensures safe parameter handling

---

### ✅ Global Exception Handling

Implemented a custom `ExceptionHandlerFilter` that:

- Wraps the entire request lifecycle
- Catches custom exceptions (`BusinessRuleException`, `DatabaseException`)
- Returns standardized JSON error responses
- Prevents stack traces from leaking to clients

Example error response:

```json
{
  "status": 400,
  "message": "Salary must be greater than zero"
}
```

---

### ✅ Clean Architecture

- **DAO Pattern**
  - Service layer does not contain SQL logic.
  - All database operations are isolated in the DAO.

- **DTO Pattern**
  - Entities are never exposed directly.
  - API contracts are controlled and decoupled.

This ensures maintainability and scalability.

---

## 🧪 Testing & Code Quality

### Unit Testing

- **JUnit 5**
- **Mockito**
- **H2 In-Memory Database** for DAO testing

Testing strategy:

- DAO tested with real H2 database  
- Service layer tested with mocked DAO  
- Controller tested with mocked Service and HTTP objects  

---

### Logging

- **SLF4J + Logback**
- Different log levels (INFO, WARN, ERROR)
- Structured log configuration

Testing and logging were treated as production-level concerns.

---

## 🛠️ Technologies Used

- Java 25  
- Jakarta EE (Servlets)  
- MySQL  
- MySQL Connector/J  
- HikariCP  
- Jackson  
- Maven  
- Apache Tomcat 10+  
- Git & GitHub  

---

## 🧠 What This Project Demonstrates

This project shows understanding of:

- HTTP request lifecycle  
- Servlet Filters  
- Layered architecture  
- Dependency boundaries  
- Database connection lifecycle  
- RESTful principles  
- Manual implementation of patterns commonly abstracted by Spring Boot  

By building these layers manually, I developed a strong foundation for working with frameworks like Spring.

---

## 🛠️ Configuration & Setup

### 1️⃣ Database Configuration

Edit:

```
src/main/resources/application.properties
```

Default configuration:

```properties
db.url=jdbc:mysql://localhost:3306/EmployeeAPI?useTimeZone=true&serverTimeZone=UTC
db.user=root
db.password=1234
```

Update credentials according to your local setup.

---

### 2️⃣ Database Initialization

```sql
CREATE DATABASE IF NOT EXISTS EmployeeAPI;
USE EmployeeAPI;

CREATE TABLE IF NOT EXISTS employee (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  salary DECIMAL(19,2) NOT NULL,
  role VARCHAR(50) NOT NULL,
  hiring_date DATE NOT NULL
);
```

Optional sample data:

```sql
INSERT INTO employee (name, salary, role, hiring_date) VALUES
('Carlos Eduardo Silva', 12500.00, 'SENIOR', '2021-03-15'),
('Fernanda Oliveira', 7800.00, 'MID_LEVEL', '2022-08-20'),
('Lucas Pereira', 4200.50, 'JUNIOR', '2024-01-10');
```

Available roles:

```
INTERN | JUNIOR | MID_LEVEL | SENIOR
```

---

## 🚀 Running the Application

### Prerequisites

- Java 17+  
- Maven  
- MySQL running  
- Apache Tomcat 10+  
- Postman or Insomnia  

---

### 1️⃣ Clone

```bash
git clone https://github.com/MatheusLeiteCarneiro/EmployeeAPI.git
```

---

### 2️⃣ Build

```bash
mvn clean package
```

---

### 3️⃣ Deploy

Deploy the generated `.war` file to Tomcat.

Base URL:

```
http://localhost:8080/app
```

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|--------|----------|------------|
| GET | `/app/employee` | List employees (pagination supported) |
| GET | `/app/employee/{id}` | Get employee by ID |
| POST | `/app/employee` | Create employee |
| PUT | `/app/employee/{id}` | Update employee |
| DELETE | `/app/employee/{id}` | Delete employee |

---

### 📝 Sample JSON (POST / PUT)

```json
{
  "name": "Developer Name",
  "salary": 10000.00,
  "role": "JUNIOR",
  "hiringDate": "2024-02-01"
}
```

---

## 📬 Postman Collection

The repository includes:

```
EmployeeAPI.postman_collection.json
```

Import it into Postman to test all endpoints quickly.

---

## 👨‍💻 Author

**Matheus Leite Carneiro**  
Backend Developer | Java
