# Safe Bank Internet Banking

Safe Bank Internet Banking is a secure Internet banking application that aims to provide a convenient and secure banking experience for both banks and customers. This JavaFX project offers multiple banking services on a single platform and ensures the security of customer data by preventing fraudulent or unauthorized access.

# Steps to run

1. clone the repository using **git clone https://github.com/sriharshaperi/Safe_Bank_Banking_App.git**
2. Run the java project
3. Provide a valid email for registration as the further step requires email verification

# Features

The implementation of Safe Bank Internet Banking includes various functionalities and features. It covers user authentication, profile information management, credit score calculation, credit card bill payment, account transactions and credit card payment transaction, beneficiary and self transfer transaction, reports and analytics.

# Implementation Details

The implementation of Safe Bank Internet Banking follows a layered application architecture. It involves several classes and interfaces from different packages. Here's a brief overview of the implementation details:

1. **Backend Services:** The backend services are integrated into the event handlers in the JavaFX UI. These services make use of classes and methods defined in the services and DAO (Data Access Object) packages. The DAO classes implement CRUD (Create, Read, Update, Delete) operations and interact with the database using a DatabaseConnectionFactory class.

2. **Validation Middleware:** Before processing any service, a validation middleware from the validations package is used to ensure that valid input is provided for transactions.
   Models and Database Schemas: Several models are implemented as blueprints for the database schemas, making it easier to add, update, and retrieve data from the database.

3. **Java Collection Framework:** The Java Collection Framework, including classes like List, Stack, Map, and Set, is primarily used for data structures in the validation logic and business logic of the application.

4. **Java Streams API:** The Java Streams API is used for streaming data collections and performing mapping and filtering operations to simplify processes.

5. **JavaFX UI:** The UI consists of .fxml files and their respective Controller files. The main features are represented by files ending with Scene.fxml, while sub-features rendered inside nested components are represented by files ending with AnchorPane.fxml.
   Problem Description and Analysis

6. The project aims to provide a seamless and secure banking experience for users. It includes features such as email verification, OTP (One-Time Password) verification, credit score calculation, account transactions, payments, and transfers. The security measures implemented in this application were inspired by real-time banking websites.

# System Design

The system design of Safe Bank Internet Banking includes both the UI design and the backend design. The UI design consists of .fxml files and their respective Controller files, while the backend design involves multiple layers for processing user inputs and completing transactions in the database.

## UI Design:

1. The UI design of Safe Bank Internet Banking is implemented using JavaFX, a popular framework for building user interfaces in Java.
2. It involves creating a set of .fxml files and their respective Controller files. The UI design focuses on providing a user-friendly and intuitive interface for customers to interact with the banking application.
3. The UI components are organized hierarchically, with the main features represented by files ending with "Scene.fxml". These files define the layout, structure, and initial appearance of the screens.
4. Additionally, sub-features rendered inside nested components are represented by files ending with "AnchorPane.fxml". These components are embedded within the main screens to provide a cohesive and modular design.
5. The UI design takes into account the principles of usability, accessibility, and responsiveness. It incorporates appropriate colors, fonts, icons, and visual elements to enhance the user experience. The layout is designed to be responsive, adapting to different screen sizes and resolutions.

## Backend Design:

1. The backend design of Safe Bank Internet Banking follows a layered architecture, which separates concerns and improves maintainability.
2. It involves the following layers:
   **Presentation Layer:** This layer corresponds to the UI components and handles user interactions. It consists of the Controller classes associated with the .fxml files. These classes receive user inputs, invoke the necessary services, and update the UI accordingly.

   **Service Layer:** The service layer encapsulates the business logic of the application. It contains classes that implement various banking services, such as user authentication, profile management, credit score calculation, transactions, payments, and transfers. These services are invoked by the Controller classes to perform the required operations.

   **Data Access Layer:** The data access layer is responsible for interacting with the database. It includes Data Access Object (DAO) classes that implement CRUD operations for retrieving, creating, updating, and deleting data from the database. These classes utilize a DatabaseConnectionFactory class to establish a connection with the database and execute SQL queries.

   **Model Layer:** The model layer defines the data structures used in the application. It includes classes that represent entities such as users, accounts, transactions, and credit scores. These classes serve as blueprints for the corresponding database schemas and are used for data manipulation and validation.

The backend design ensures separation of concerns, modularity, and reusability. Each layer has clear responsibilities, and dependencies are managed appropriately to maintain a loosely coupled architecture.

Overall, the system design of Safe Bank Internet Banking combines an intuitive UI design with a well-structured backend architecture. This ensures a seamless and secure banking experience for users while facilitating future enhancements and maintenance.

# Evaluation

The application has been developed with a focus on security, convenience, and consistency of transaction data. It includes features such as email and OTP verification, password encryption, and various validation checks. The implementation has been thoroughly tested, and screenshots of sample runs are provided in the report.

# Advantages and Benefits

Safe Bank Internet Banking offers several advantages and benefits to users. It securely stores user credentials using password encryption techniques, implements email and OTP verification for transactions, and ensures consistency in transaction data. The application also pays attention to edge cases and provides detailed input validations.
