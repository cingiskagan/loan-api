# Bank Loan Management API

This is a Spring Boot-based backend service for managing bank loans, customers, and loan payments.  
It supports creating loans, listing loans and installments, and processing loan payments with business rules.

---

## **Technologies Used**
- **Java 19**
- **Spring Boot**
- **Maven**
- **H2 Database (in-memory)**
- **Springdoc OpenAPI (for API documentation)**


## **How to use**
- Clone project to your dev env
- Import project as Maven Project or Spring Boot Project (Depends on your IDE)
- After the project is run, visit "http://localhost:8080/swagger-ui/index.html" to explore endpoints.
- or "LoanAPI.postman_collection.json" file can be imported to Postman to test endpoints.
- Unit tests are in "LoanApi2ApplicationTests.java" file.
- API uses Basic authentication. Default credentials are in "application.propeties" file.
