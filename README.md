# Library Management System

A Library Management System designed to facilitate the management of books and users in a library setting for CS UH-3260 assignments on Advanced Testing. 

## Goal 
The goal of the Library Management System is to provide an efficient and user-friendly platform for managing books and users within a library environment. It aims to streamline processes such as book search, reservation, and return, while also facilitating administrative tasks such as adding new books, managing book inventory, and user authentication.

## Features
- User Authentication: Secure login and signup for admin and regular users.
- Book Management: Add, remove, and update book inventory.
- Search Functionality: Quick keyword-based book search.
- Reservation System: Reserve books for borrowing.
- Book Borrowing and Return: Borrow and return books seamlessly.
- Database Management: Utilize MongoDB for secure data storage.
- User-Friendly Interface: Intuitive interface for easy interaction.

1. User: Manages user information and authentication.
2. Book: Handles book details and information.
3. Database Manager: Manages interactions with the database for users and books.
4. SystemManager: Controls system functionality, including user operations and book management.

## Tools & Technologies
- Language of implementation: Java
- Framework for Unit Testing: JUnit
- Framework for Mocking: Mockito
- Framework for Mutation Testing: Pitest
- Code Coverage Tool: Jacoco
- External database: MongoDB

## Instructions
### Prerequisites
1. Java Development Kit (JDK) installed on your machine.
2. Maven

### Compilation & Running
1. Navigate to the root directory of this project where the pom.xml file is located:
```
cd path/to/your/project/root
```
2. Compile your project using Maven:
```
mvn clean compile
```
3. Run the program using the following command:
```
mvn exec:java -Dexec.mainClass="cs.nyuad.csuh3260.library.SystemManager"
```

## Achieved statement and branch coverage 

## Achieved mutation score

