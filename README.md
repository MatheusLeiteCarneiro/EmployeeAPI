# Employee API

**Description:** `A robust RESTful API built with pure Java (Servlets & JDBC) to acquire a profound understanding of backend architecture without the 'magic' of frameworks like Spring.`

This Java project was developed to master the fundamentals of Web Development and Backend Architecture.
#### The primary focus was on:
* High-Performance Database Connection Pooling (HikariCP).
* Centralized Error Handling via Filters.
* Implementing DAO and DTO patterns manually.

The application exposes endpoints to manage Employees, handling JSON serialization/deserialization manually and enforcing strict architectural layers.

---
## ✨ Features

* **CRUD Operations:** Complete lifecycle management for Employees (Create, Read, Update, Delete).

* **Pagination System:**
    * The `GET` endpoint supports `page` and `size` query parameters.
    * Optimized SQL queries using `LIMIT` and `OFFSET` to handle large datasets efficiently.

* **High-Performance Database Access:**
    * **Connection Pooling (HikariCP):** Instead of opening a new expensive connection for every request, the API uses a pre-warmed pool of connections, drastically reducing latency.
    * **Prepared Statements:** All database operations are protected against SQL Injection.

* **Global Exception Handling:**
    * Implemented a custom `Filter` (`ExceptionHandlerFilter`) that wraps the entire request lifecycle.
    * Catches custom exceptions like `BusinessRuleException` or `DatabaseException` and converts them into standardized, clean JSON responses (`ErrorDTO`) without exposing stack traces to the client.

* **Clean Architecture:**
    * **DAO Pattern:** The Service layer never touches SQL code; it relies on the Data Access Object.
    * **DTO Pattern:** The API never exposes the raw Entity to the outside world, using Data Transfer Objects for safety and decoupling.

---

## 🛠️ Technologies Used

* **Java 25**
* **Jakarta EE (Servlets)**
* **MySQL** (Database)
* **MySQL Connector/J** (JDBC Driver)
* **HikariCP** (Connection Pooling)
* **Jackson** (JSON Processing)
* **Maven** (Dependency Management)
* **Apache Tomcat** (Web Server)
* **Git & GitHub** for version control.

---

## 🚀 What I Learned (Concepts Practiced)

This project bridged the gap between basic Java syntax and Web Backend development:

* **Manual Architectural Implementation:**
    * I built the layers that Spring Boot usually creates automatically. This gave me a deep understanding of *how* and *why* we use **Controllers**, **Services**, and **Repositories**.

* **Connection Lifecycle Management:**
    * Learned the cost of opening database connections and how to solve it using the **Connection Pool** with **HikariCP**.

* **Servlet Filters & Request Chain:**
    * Understood how to intercept HTTP requests using Filters to handle cross-cutting concerns like Exception Handling and Content-Type encoding globally.

* **RESTful Principles:**
    * Practiced the correct usage of HTTP Verbs (`GET`, `POST`, `PUT`, `DELETE`) and Status Codes (`200 OK`, `201 Created`, `204 No Content`, `404 Not Found`).

---

## 🛠️ Configuration & Setup

### 1. Database Configuration
The project is configured with default development credentials in `src/main/resources/application.properties`.

> **Note:** The default user is set to `root` and the password is set to `1234`. Please update the properties to match your local MySQL installation before running.

```properties
db.url=jdbc:mysql://localhost:3306/EmployeeAPI?useTimeZone=true&serverTimeZone=UTC
db.user=root  ← Change this if your user is different
db.password=1234  ← Change this if your password is different
```

---

### 2. Database Initialization
To create the project database use the following SQL script

```sql
-- 1. Create Database
CREATE DATABASE IF NOT EXISTS EmployeeAPI;
USE EmployeeAPI;

-- 2. Create Table
CREATE TABLE IF NOT EXISTS employee (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    salary DECIMAL(19, 2) NOT NULL,
    role VARCHAR(50) NOT NULL,
    hiring_date DATE NOT NULL
    );

--3. (Optional) Insert initial employees
INSERT INTO employee (name, salary, role, hiring_date) VALUES
                                                           ('Carlos Eduardo Silva', 12500.00, 'SENIOR', '2021-03-15'),
                                                           ('Fernanda Oliveira', 7800.00, 'MID_LEVEL', '2022-08-20'),
                                                           ('Lucas Pereira', 4200.50, 'JUNIOR', '2024-01-10'),
                                                           ('Amanda Costa', 1800.00, 'INTERN', '2024-11-01'),
                                                           ('Roberto Mendes', 8500.00, 'MID_LEVEL', '2023-05-12');
```
> **Note:** The roles available in the project are: `INTERN`, `JUNIOR`, `MID_LEVEL`, `SENIOR`.
---
## 🏁 How to Run
### - Prerequisites
- **Java 17+**.
- **Maven**.
- **MySQL** Database installed and running.
- **Apache Tomcat 10+** (Server).
- **Endpoints Testing Tool:** Postman or Insomnia.

## - Steps

### 1. Clone the repository:
Open your terminal and run:
```bash
git clone https://github.com/MatheusLeiteCarneiro/EmployeeAPI.git
```

### 2. Database Setup:
- Open your MySQL client (Workbench, DBeaver, or Terminal).
- Create the database and table using the script provided in the **Configuration** section above.

### 3. Configure Credentials:
- Open the file `src/main/resources/application.properties`.
- Update the `db.user` and `db.password` fields with your local MySQL information.

### 4. Build & Run:
Choose your preferred IDE below to run the server.

<details>
<summary><strong>👉 Option 1: IntelliJ IDEA</strong></summary>
<br>

1.  **Open** the project in IntelliJ and wait for Maven to download dependencies.
2.  Click on **"Add Configuration"** (top right) → **+** → **Tomcat Server** → **Local**.
3.  In the **"Deployment"** tab, click **+** and select **"Artifact"** (`EmployeeAPI:war exploded`).
4.  Click **Apply** and **Run**.
</details>

<details>
<summary><strong>👉 Option 2: Eclipse IDE</strong></summary>
<br>

1.  **Import:** Go to `File` > `Import` > `Maven` > `Existing Maven Projects` and select the cloned folder.
2.  **Update Maven:** Right-click the project > `Maven` > `Update Project` (wait for downloads).
3.  **Server Setup:**
    * Go to the **Servers** view (if not visible: `Window` > `Show View` > `Servers`).
    * Click to create a new server > Select **Apache Tomcat v10.1** > Next.
    * Browse to your local Tomcat installation directory > Finish.
4.  **Run:**
    * Right-click the project root folder.
    * Select `Run As` > `Run on Server`.
    * Choose the Tomcat server you just created and click **Finish**.
</details>

### 5. Access the API:
The API Base URL is: `http://localhost:8080/app`

> **Note:** Accessing the base URL (`/app`) may return a 404 as there is no index page.



### 6. Available Endpoints

Here is the full list of API routes you can test.

| Method     | Endpoint             | Description                                     |
|:-----------|:---------------------|:------------------------------------------------|
| **GET**    | `/app/employee`      | List all employees (supports `?page=1&size=10`) |
| **GET**    | `/app/employee/{id}` | Get details of a specific employee              |
| **POST**   | `/app/employee`      | Create a new employee                           |
| **PUT**    | `/app/employee/{id}` | Update an existing employee                     |
| **DELETE** | `/app/employee/{id}` | Remove an employee                              |
> **Note:** On the GET method if you don't specify a page and a size it will automatically assume that page=1 and size=10.
#### 📝 Sample JSON Payload (for POST & PUT)
Use this JSON body when creating or updating an employee. Note that `hiringDate` follows the `YYYY-MM-DD` format.

```json
{
  "name": "Developer Name",
  "salary": 10000.00,
  "role": "JUNIOR",
  "hiringDate": "2024-02-01"
}
```

### - Testing with Postman
To make testing easier, I have included a Postman Collection file in this repository with all endpoints pre-configured.
1.  **Download the Collection:**
    * Locate the file [`EmployeeAPI.postman_collection.json`](./EmployeeAPI.postman_collection.json) on the project files.

2.  **Import into Postman:**
    * Open Postman.
    * Click the **Import** button.
    * Select the `.json` file.
3.  **Run:**
    * Select the **"Employee API"** collection from the menu.
    * Start testing the requests.


---
### Author: **Matheus Leite Carneiro**