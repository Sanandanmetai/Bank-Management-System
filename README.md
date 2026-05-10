# 🏦 NexaBank — Bank Management System

> A fully functional, GUI-based Bank Management System built with **Java Swing** (frontend) and **MySQL** (backend), connected via **JDBC**. Developed as a semester OOP + DBMS project.

---

## 🎥 Demo Video
📺 **[Click Here to Watch Demo](https://drive.google.com/file/d/1n9Rl4O9b1adqo9BsxUUObCrfuNo69Br6/view?usp=sharing)**

---

## 👥 Group Members

| Name | Student ID | Section |
|------|-----------|---------|
| [Sanandan Metai] | [023-25-0096] | [BS-CS-II (sec: E)] |

---

## 📌 Purpose

Traditional banking systems rely on manual record-keeping which causes data redundancy and poor scalability. **NexaBank** replaces this with a structured relational database-driven solution.

The system manages:
- Customer accounts (register, login, update profile)
- Financial transactions (deposits and withdrawals)
- Loan applications (apply, approve, reject)
- Admin operations (manage customers, monitor all activity)

---

## 🗂️ Main Modules

| Module | Files | Description |
|--------|-------|-------------|
| **Models** | Account.java, Transaction.java, Loan.java, Admin.java | Entity classes with encapsulation |
| **Database** | DBConnection.java | JDBC MySQL connection using Singleton pattern |
| **DAO Layer** | AccountDAO.java, TransactionDAO.java, LoanDAO.java, AdminDAO.java | All database CRUD operations using PreparedStatement |
| **GUI** | 13 Java Swing window files | Complete user interface for all features |
| **Theme** | UITheme.java | Centralized modern dark UI design system |

---

## ✅ OOP Concepts Demonstrated

| Concept | Where Used |
|---------|-----------|
| Encapsulation | Private fields + getters/setters in all 4 model classes |
| Inheritance | All 13 GUI classes extend JFrame |
| Abstraction | DAO layer hides SQL logic from GUI completely |
| Method Overriding | toString() in Account; isCellEditable() in JTable |
| Constructors | Default + parameterized in all model classes |
| Collections | ArrayList used in all DAOs to return lists of records |
| Exception Handling | Try-catch in every DAO and GUI method |
| Singleton Pattern | DBConnection — one connection reused everywhere |
| DAO Pattern | 4 DAO classes separate database from interface |

---

## 🗄️ Database Schema

**Database name:** `bank_management_system`

```
account      (account_id PK, name, email UNIQUE, phone, balance, password)
transaction  (transaction_id PK, account_id FK, type, amount, date)
loan         (loan_id PK, account_id FK, amount, status)
admin        (admin_id PK, username UNIQUE, password)
```

**Relationships:**
- account (1) → transaction (many)
- account (1) → loan (many)
- Both foreign keys use ON DELETE CASCADE

---

## ⚙️ How to Run

### Requirements
- JDK 17 or higher
- MySQL 8.0
- MySQL Connector/J (included in lib/ folder)

### Step 1 — Setup database
```
mysql -u root -p
source /path/to/database_setup.sql
```

### Step 2 — Set your MySQL password
Open `src/DBConnection.java` and update:
```java
private static final String PASSWORD = "your_mysql_password";
```

### Step 3 — Compile
```
javac -cp "lib/mysql-connector-j-9.3.0.jar" -d out src/*.java
```

### Step 4 — Run
```
# Windows
java -cp "out;lib/mysql-connector-j-9.3.0.jar" Main

# Linux / Mac
java -cp "out:lib/mysql-connector-j-9.3.0.jar" Main
```

### Default Login Credentials

| Role | Email / Username | Password |
|------|-----------------|----------|
| Admin | admin | admin123 |
| Customer | ali@example.com | pass123 |

---

## 🚀 Features

### Customer
- Register new account
- Secure login
- Deposit and withdraw money
- View transaction history
- Apply for loans and track status
- Update personal information

### Admin
- Separate admin login
- Dashboard with live bank statistics
- View, search, delete customer accounts
- Monitor all transactions
- Approve or reject loan applications

---

## 🛠️ Technologies

| Technology | Purpose |
|-----------|---------|
| Java Swing | GUI frontend |
| MySQL 8.0 | Relational database backend |
| JDBC Connector/J 9.3.0 | Database connectivity |
| JDK 21 | Core language |

---

## 📁 GitHub Repository
🔗 **[YOUR_GITHUB_REPO_LINK_HERE](https://github.com/Sanandanmetai/Bank-Management-System)**

---

*NexaBank — OOP Semester Project | 2026*
