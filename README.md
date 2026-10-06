# Library Management System

**Java + JDBC + MySQL**

A simple console-based Library Management System demonstrating Java OOP, JDBC, MySQL database interaction, CRUD operations, and book issue/return management.

---

## Project Overview

A console application for managing books and library members. It supports adding/searching books and members, issuing and returning books, checking availability, and automatic database/table setup.

---

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Application development |
| JDBC | Java–MySQL connectivity |
| MySQL 8.4 | Database |
| MySQL Connector/J | JDBC driver |
| Command Prompt | Compilation and execution |
| Git/GitHub | Version control and hosting |

---

## Features

### 1. Add Book

Stores:

- Book title
- Author
- Quantity

Example:

```text
Title: Java Programming
Author: James Gosling
Quantity: 3
```

### 2. View/Search Books

Displays books stored in the database.

Example:

```text
1 | Java Programming | James Gosling | Quantity: 3
```

### 3. Add Member

Registers a library member with:

- Member name
- Phone number

Example:

```text
Name: GK
Phone: 90
```

### 4. View/Search Members

Displays registered members.

Example:

```text
1 | GK | 90
```

### 5. Issue Book

Issues an available copy and records the issue.

When a book is issued:

```text
Available Copies = Available Copies - 1
```

### 6. Return Book

Records the return and restores availability.

When a book is returned:

```text
Available Copies = Available Copies + 1
```

### 7. Check Book Availability

Shows the number of available copies.

Example:

```text
Java Programming - Available copies: 2
```

If all copies are issued:

```text
Java Programming - Available copies: 0
```

The system prevents further issues when no copies are available.

### 8. Exit

Closes the application safely.

---

# System Architecture

```text
              USER
                |
                v
          +-----------+
          | Main.java |
          | Console UI|
          +-----+-----+
                |
                v
        +----------------+
        |  Library.java  |
        | Business Logic |
        +-------+--------+
                |
                v
        +----------------+
        | Database.java  |
        | JDBC Connection|
        +-------+--------+
                |
                v
        +----------------+
        | MySQL Server   |
        |   library_db   |
        +----------------+
```

---

# Project Structure

```text
LibraryManagementSystem/
|
+-- src/
|   +-- Main.java
|   +-- Library.java
|   +-- Book.java
|   +-- Member.java
|   +-- Database.java
|
+-- out/
|
+-- mysql-connector-j-26.7.0.jar
|
+-- README.md
```

---

# Source Code Structure

## Main.java

`Main.java` is the entry point of the application.

Responsibilities:

- Displays the main menu
- Takes user input
- Calls the appropriate library operations
- Handles console interaction

Main menu:

```text
=================================
     LIBRARY MANAGEMENT SYSTEM
=================================
1. Add Book
2. View/Search Books
3. Add Member
4. View/Search Members
5. Issue Book
6. Return Book
7. Check Book Availability
8. Exit
Enter choice:
```

---

## Book.java

`Book.java` represents a book in the library.

It contains information such as:

- Book ID
- Title
- Author
- Quantity

The class acts as the model for book-related data.

---

## Member.java

`Member.java` represents a library member.

It contains information such as:

- Member ID
- Member name
- Phone number

The class acts as the model for member-related data.

---

## Library.java

`Library.java` contains the main business logic of the application.

Responsibilities include:

- Adding books
- Searching books
- Adding members
- Searching members
- Issuing books
- Returning books
- Checking availability
- Validating book/member information

This keeps the application logic separate from the console interface.

---

## Database.java

`Database.java` handles database connectivity and database initialization.

Responsibilities include:

- Connecting to MySQL
- Loading the MySQL JDBC driver
- Creating the database if required
- Creating required tables
- Providing database connections

The application uses JDBC to communicate with MySQL.

---

# Database Architecture

The application uses a MySQL database named:

```text
library_db
```

The database contains three main tables:

```text
library_db
|
+-- books
|
+-- members
|
+-- issues
```

---

# Books Table

The `books` table stores information about books.

Conceptually:

```text
books
--------------------------------
id
title
author
quantity
```

Example:

```text
1 | Java Programming | James Gosling | 3
```

---

# Members Table

The `members` table stores registered members.

Conceptually:

```text
members
--------------------------------
id
name
phone
```

Example:

```text
1 | GK | 90
```

---

# Issues Table

The `issues` table stores information about book issues and returns.

It connects books and members and tracks the borrowing process.

Conceptually:

```text
issues
--------------------------------
id
book_id
member_id
issue_date
return_date
```

---

# Database Relationships

```text
             +--------------+
             |    BOOKS     |
             |--------------|
             | id           |
             | title        |
             | author       |
             | quantity     |
             +------+-------+
                    |
                    | book_id
                    |
             +------v-------+
             |    ISSUES    |
             |--------------|
             | id           |
             | book_id      |
             | member_id    |
             | issue_date   |
             | return_date  |
             +------+-------+
                    |
                    | member_id
                    |
             +------v-------+
             |   MEMBERS    |
             |--------------|
             | id           |
             | name         |
             | phone        |
             +--------------+
```

---

# Book Issue Workflow

```text
User selects Issue Book
        |
        v
Enter Book ID
        |
        v
Enter Member ID
        |
        v
Check Book
        |
        v
Check Member
        |
        v
Check Availability
        |
        +-- No copies --> Reject
        |
        v
Create Issue Record
        |
        v
Decrease Book Quantity
        |
        v
Issue Successful
```

---

# Book Return Workflow

```text
User selects Return Book
        |
        v
Find Issue Record
        |
        v
Update Return Information
        |
        v
Increase Available Quantity
        |
        v
Return Successful
```

---

# Availability Logic

The application keeps track of the number of available copies.

For issuing:

```text
quantity = quantity - 1
```

For returning:

```text
quantity = quantity + 1
```

If:

```text
quantity = 0
```

the system prevents another user from issuing the book.

Example:

```text
Initial quantity: 3

Issue #1 -> 2 available
Issue #2 -> 1 available
Issue #3 -> 0 available

Issue #4 -> Book is currently unavailable.
```

---

# JDBC Architecture

The application uses JDBC to connect Java with MySQL.

The basic flow is:

```text
Java Application
       |
       v
JDBC API
       |
       v
MySQL Connector/J
       |
       v
MySQL Server
       |
       v
library_db
```

The MySQL Connector/J JAR is included in the project:

```text
mysql-connector-j-26.7.0.jar
```

---

# Database Configuration

The application accepts database credentials at runtime.

Example:

```cmd
java -Ddb.user=root -Ddb.password=YOUR_MYSQL_PASSWORD -cp "out;mysql-connector-j-26.7.0.jar" Main
```

This avoids storing the MySQL password directly in the source code.

> **Important:** Never commit your real database password to GitHub.

---

# Requirements

Before running the project, install:

## 1. Java JDK

Java 21 or a compatible JDK.

Check:

```cmd
java -version
```

and:

```cmd
javac -version
```

---

## 2. MySQL Server

Install MySQL Server and make sure the MySQL server is running.

The application expects MySQL to be available on:

```text
localhost:3306
```

---

## 3. MySQL Connector/J

The project includes:

```text
mysql-connector-j-26.7.0.jar
```

---

# Project Location

The project is located at:

```text
C:\Mini Projects\LibraryManagementSystem
```

Open Command Prompt and navigate to it:

```cmd
cd /d "C:\Mini Projects\LibraryManagementSystem"
```

---

# How to Compile

From the project directory, run:

```cmd
javac -cp ".;mysql-connector-j-26.7.0.jar" -d out src\*.java
```

If compilation succeeds, the compiled `.class` files will be generated inside:

```text
out/
```

---

# How to Run

Run:

```cmd
java -Ddb.user=root -Ddb.password=YOUR_MYSQL_PASSWORD -cp "out;mysql-connector-j-26.7.0.jar" Main
```

For example, if your local MySQL password is `1234`:

```cmd
java -Ddb.user=root -Ddb.password=1234 -cp "out;mysql-connector-j-26.7.0.jar" Main
```

---

# Database Initialization

The application handles the required database setup through `Database.java`.

The required database is:

```text
library_db
```

and the required tables are:

```text
books
members
issues
```

This reduces the amount of manual database setup required before running the application.

---

# Testing

The following functionality was tested successfully:

| Test | Status |
|---|---|
| Java compilation | Passed |
| MySQL connection | Passed |
| JDBC connection | Passed |
| Database creation/setup | Passed |
| Add Book | Passed |
| View/Search Books | Passed |
| Add Member | Passed |
| View/Search Members | Passed |
| Invalid Member ID handling | Passed |
| Issue Book | Passed |
| Return Book | Passed |
| Availability checking | Passed |
| Prevent issuing unavailable book | Passed |
| Exit application | Passed |

---

# Java Concepts Demonstrated

This project demonstrates several important Java concepts.

## Object-Oriented Programming

The project uses multiple classes:

```text
Book
Member
Library
Database
Main
```

---

## Classes and Objects

`Book` and `Member` are used as models for real-world entities.

Example:

```java
Book book = new Book(...);
```

---

## Constructors

Constructors are used to initialize objects.

---

## Encapsulation

Data and related operations are organized inside classes.

---

## Methods

Different methods handle individual operations such as:

```text
addBook()
addMember()
issueBook()
returnBook()
searchBooks()
searchMembers()
```

---

## Exception Handling

Database and input-related errors are handled to prevent the application from crashing unexpectedly.

---

## JDBC

The project demonstrates how Java applications communicate with relational databases using JDBC.

---

# Separation of Responsibilities

The project separates different responsibilities between classes.

```text
Main.java
    |
    +-- User Interface

Library.java
    |
    +-- Business Logic

Book.java
    |
    +-- Book Model

Member.java
    |
    +-- Member Model

Database.java
    |
    +-- Database Connectivity
```

This makes the project easier to understand, maintain, and extend.

---

# Security Note

This project is intended as an educational college project.

For a production application, additional security features should be implemented, such as:

- Secure credential management
- Password hashing
- User authentication
- Authorization
- Input validation
- Environment variables/secrets
- Better database access controls
- Transaction management
- Audit logging

Do not commit real passwords, API keys, or other secrets to GitHub.

---

# Current Limitations

This is a simple console-based academic project.

Some production-level features are not included, such as:

- GUI
- Web interface
- Authentication system
- Admin dashboard
- Fine calculation
- Book reservation system
- Email notifications
- Multiple library branches
- Advanced reporting
- Cloud database
- Role-based access control

---

# Future Improvements

Possible future versions could include:

## GUI

Build a graphical interface using:

- JavaFX
- Swing

## Web Application

Convert the system into a web application using:

- Spring Boot
- HTML/CSS/JavaScript
- REST APIs

## Authentication

Add:

```text
Admin Login
Member Login
```

## Fine Management

Automatically calculate fines for overdue books.

## Search Improvements

Support searching by:

- Book title
- Author
- Book ID
- Member name
- Member ID

## Reporting

Generate reports such as:

- Most borrowed books
- Active issues
- Available books
- Registered members
- Overdue books

---

# Example Application

When the application starts:

```text
=================================
     LIBRARY MANAGEMENT SYSTEM
=================================
1. Add Book
2. View/Search Books
3. Add Member
4. View/Search Members
5. Issue Book
6. Return Book
7. Check Book Availability
8. Exit
Enter choice:
```

Example book:

```text
1 | Java Programming | James Gosling | Quantity: 3
```

After issuing books:

```text
Java Programming - Available copies: 0
```

Attempting to issue another copy:

```text
Book is currently unavailable.
```

---

# Complete System Flow

```text
                 START
                   |
                   v
             Start Java App
                   |
                   v
          Connect to MySQL
                   |
                   v
       Initialize Database/Tables
                   |
                   v
              Show Menu
                   |
       +-----------+------------+
       |           |            |
       v           v            v
    Books       Members       Issues
       |           |            |
       +-----------+------------+
                   |
                   v
             Perform Action
                   |
                   v
              Show Result
                   |
                   v
              Show Menu
                   |
                   v
                  Exit
```

---

# Academic Purpose

This project was created as a practical demonstration of:

- Java programming
- Object-Oriented Programming
- JDBC
- MySQL
- Database design
- CRUD operations
- Console-based application development

It combines Java programming concepts with real database interaction in a small but functional application.

---

# Author

**Granth Koushik (GK)**

Java + JDBC + MySQL Library Management System

---

# License

This project is intended primarily for educational and academic purposes.

You may modify and use the project for learning and personal educational work.
