# 🐾 Happy Pet Backend

## 📌 Project Overview
**Happy Pet** is a full-stack web platform built to facilitate **ethical pet adoption, purchasing, and matchmaking** while promoting **animal welfare**.  
This backend, developed using **Spring Boot**, provides secure **RESTful APIs** to connect adopters, breeders, and shelters through a **role-based access system**.

The system manages:
- 🐶 Pets (adoption, matchmaking & purchasing)
- 🧑‍🤝‍🧑 Users (adopters, breeders, shelters, admins)
- 📩 Inquiries (pet adoption, pet buy, pet matchmaking, contact)
- 📊 Admin dashboard insights

---

## 🚀 Features
- **User Authentication & Authorization** with JWT
- **CRUD Operations** for pets, adoptions, and users
- **Pet Profile Image Uploads**
- **Admin Dashboard Data**
- **CORS Configuration** for cross-origin requests
- Hosted on **AWS EC2**
- Integrated with **MySQL** for data persistence
- CI/CD with automated testing & deployment

---

## 🛠 Technologies Used
**Backend Framework:** Spring Boot (Java 17)  
**Security:** Spring Security + JWT  
**Database:** MySQL (configurable) with JPA/Hibernate ORM  
**Build Tool:** Maven  
**Libraries:**
- Lombok (boilerplate reduction)
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL Connector

---

## ⚙️ Getting Started

### 1️⃣ Prerequisites
Ensure you have installed:
- Java 17+
- Maven
- MySQL

---

### 2️⃣ Installation
```bash
# Clone the repository
git clone https://github.com/yourusername/happypet-backend.git
cd happypet-backend

# Configure database in application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/happypet
spring.datasource.username=yourusername
spring.datasource.password=yourpassword

# JPA & Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# JWT Secret Key
jwt.secret=your_secret_key

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```
---
## The backend will start on:
- http://localhost:8080
---
## 📽️ Demo Video
https://www.youtube.com/watch?v=3azFI7EclAU

---

## 🔑 Authentication Endpoints
| Method | Endpoint                   | Description                                |
| ------ | -------------------------- | ------------------------------------------ |
| POST   | `/api/auth/register`       | Register a new user                        |
| POST   | `/api/auth/authenticate`   | Authenticate a user & return JWT           |
| POST   | `/api/auth/register-admin` | Register a new admin (requires secret key) |

## 👥 Matchmaking Management
| Method | Endpoint                      | Access | Description          |
| ------ | ----------------------------- | ------ | -------------------- |
| POST   | `/api/v1/adduser`             | ADMIN  | Add new user         |
| GET    | `/api/v1/getusers`            | ADMIN  | Retrieve all users   |
| PUT    | `/api/v1/updateuser/{id}`     | ADMIN  | Update existing user |
| DELETE | `/api/v1/deleteuser/{userId}` | ADMIN  | Delete user          |

## 🐾 Pet Buy Management
| Method | Endpoint                   | Access | Description        |
| ------ | -------------------------- | ------ | ------------------ |
| GET    | `/api/v1/pets`             | Public | Retrieve all pets  |
| GET    | `/api/v1/pets/{id}`        | Public | Retrieve pet by ID |
| POST   | `/api/v1/pets/add`         | ADMIN  | Add new pet        |
| PUT    | `/api/v1/pets/update/{id}` | ADMIN  | Update pet         |
| DELETE | `/api/v1/pets/delete/{id}` | ADMIN  | Delete pet         |

## 🏡 Adoption Management
| Method | Endpoint                        | Access | Description             |
| ------ | ------------------------------- | ------ | ----------------------- |
| GET    | `/api/v1/adoptions`             | Public | Retrieve all adoptions  |
| GET    | `/api/v1/adoptions/{id}`        | Public | Retrieve adoption by ID |
| POST   | `/api/v1/adoptions/submit`      | Public | Submit adoption request |
| PUT    | `/api/v1/adoptions/update/{id}` | ADMIN  | Update adoption status  |
| DELETE | `/api/v1/adoptions/delete/{id}` | ADMIN  | Delete adoption         |

## 📩 Contact Inquiry Management
| Method | Endpoint               | Access | Description                |
| ------ | ---------------------- | ------ | -------------------------- |
| POST   | `/api/v1/contact`      | Public | Submit contact form        |
| GET    | `/api/v1/contact`      | ADMIN  | Retrieve all contact forms |
| DELETE | `/api/v1/contact/{id}` | ADMIN  | Delete contact form        |
| PUT    | `/api/v1/contact/{id}` | ADMIN  | Update contact form status |

## 🐕 Pet Inquiry Management
| Method | Endpoint                     | Access | Description                |
| ------ | ---------------------------- | ------ | -------------------------- |
| GET    | `/api/v1/pet-inquiries`      | ADMIN  | Retrieve all pet inquiries |
| GET    | `/api/v1/pet-inquiries/{id}` | ADMIN  | Retrieve inquiry by ID     |
| POST   | `/api/v1/pet-inquiries`      | Public | Create new pet inquiry     |
| PUT    | `/api/v1/pet-inquiries/{id}` | ADMIN  | Update inquiry status      |

## 🖼 Image Management
| Method | Endpoint                    | Access | Description        |
| ------ | --------------------------- | ------ | ------------------ |
| GET    | `/api/v1/images/{filename}` | Public | Retrieve pet image |

## 📊 Dashboard
| Method | Endpoint                      | Access | Description                        |
| ------ | ----------------------------- | ------ | ---------------------------------- |
| GET    | `/api/v1/dashboard/formatted` | ADMIN  | Get formatted admin dashboard data |
| GET    | `/api/v1/dashboard/raw`       | ADMIN  | Get raw admin dashboard data       |

---
## 🌍 CORS Configuration
- CORS is configured to allow cross-origin requests from the Happy Pet Frontend.

---
## 🛡 Security
- Authentication: JWT tokens are required for protected endpoints.

- Authorization: Role-based (ADMIN, USER) access control.

---
## 👨‍💻 Team Project Details
- Type: Full-Stack Web Application

- Frontend: React (Hosted on Vercel)

- Backend: Spring Boot + MySQL (Hosted on AWS EC2)

- CI/CD: Automated testing & deployment pipelines
---
