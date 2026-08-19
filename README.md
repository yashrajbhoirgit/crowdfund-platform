#  CrowdHope — Crowdfunding Platform for Social Causes

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen?style=for-the-badge&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?style=for-the-badge&logo=mysql)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apachemaven)
![Razorpay](https://img.shields.io/badge/Razorpay-Sandbox-02042B?style=for-the-badge&logo=razorpay)

A modern, full-stack crowdfunding web platform built with **Java Spring Boot 3** and **Vanilla Web Technologies (HTML5 / CSS3 / JavaScript)** designed to empower social, medical, educational, and community initiatives through transparent collective funding.

</div>

---

## ✨ Features

- 🔐 **JWT-Based Authentication** — Stateless JWT authentication, role-based access control (User/Admin), secure BCrypt password hashing.
- 📋 **Campaign Management (CRUD)** — Create, edit, browse, search, and delete fundraising campaigns with live progress tracking in INR (₹).
- 🖼️ **Image Upload Pipeline** — Seamless cover photo uploads for campaigns with local storage and instant previews.
- 💳 **Donations & Payment Flow** — Supports Razorpay Sandbox Checkout as well as 1-click Instant Test Donation for easy local testing.
- 🧾 **PDF Tax Receipts** — Auto-generated downloadable donation receipts via **iText7**.
- 📊 **Admin Dashboard & Analytics** — Live MySQL platform metrics, **Chart.js** monthly donation trends, and category distribution charts.
- 👥 **User Management & Moderation** — Admin capability to inspect users, moderate/approve/reject campaigns, and delete entries.
- 📁 **CSV Reports Export** — 1-click export of platform users and donation records into CSV.
- 🌐 **Unified Navigation & Profile** — Dynamic top navbar tracking authentication state, avatar pills, and dedicated **My Campaigns** & **My Donations** tabs.

---

## 🧩 Module Architecture

| Module | Description | Key Components |
|---|---|---|
| **Module 1: Authentication** | User sign-up, login, profile management, JWT filter | `com.crowdfund.auth` |
| **Module 2: Campaigns** | Campaign creation, image uploads, search, delete | `com.crowdfund.campaign` |
| **Module 3: Donations** | Order initiation, verification, PDF receipt, impact points | `com.crowdfund.donation` |
| **Module 4: Admin Panel** | Platform statistics, Chart.js analytics, CSV reporting | `com.crowdfund.admin` |

---

## 🛠️ Technology Stack

| Layer | Technologies Used |
|---|---|
| **Backend Framework** | Spring Boot 3.1.5, Spring Security 6, Spring Data JPA |
| **Language** | Java 17 |
| **Database** | MySQL 8.x (Hibernate ORM with auto-schema updates) |
| **Security** | JSON Web Tokens (JJWT 0.11.5), BCrypt Hashing |
| **Payment Gateway** | Razorpay API & Sandbox Signature Verification |
| **PDF Engine** | iText7 (7.2.5) |
| **Email Service** | JavaMailSender (SMTP) |
| **Frontend** | HTML5, CSS3 Glassmorphism UI, Vanilla JavaScript |
| **Data Visualization**| Chart.js (CDN) |
| **Build & Dependency**| Apache Maven 3.8+ |

---

## 🚀 Quick Start Guide

### Prerequisites
- ☕ **Java 17+** installed (`java -version`)
- 📦 **Maven 3.8+** installed (`mvn -v`)
- 🐬 **MySQL Server 8.x** running locally

---

### 1. Clone the Repository
```bash
git clone https://github.com/yashrajbhoirgit/crowdfund-platform.git
cd crowdfund-platform
```

### 2. Create the Database
Open MySQL Workbench or MySQL CLI and run:
```sql
CREATE DATABASE IF NOT EXISTS crowdfund_db;
```

### 3. Configure Database Credentials
Edit `src/main/resources/application.properties` to match your local MySQL password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crowdfund_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 4. Build and Run the Application
```bash
mvn spring-boot:run
```
The server will start at: **`http://localhost:8080`**

---

## 🔑 Default Credentials

The application automatically provisions a default **Admin** user upon startup:

| Role | Email | Password |
|---|---|---|
| ⚡ **Admin** | `admin@test.com` | `password123` |
| 👤 **User** | Register a new user at `/register.html` | Your chosen password |

---

## 🌐 Application Pages

| Page | URL | Description |
|---|---|---|
| 🏠 **Home** | `http://localhost:8080/index.html` | Landing page with featured causes & statistics |
| 🔍 **Explore Campaigns** | `http://localhost:8080/campaigns.html` | Browse, search, filter causes with inline delete option |
| 📄 **Campaign Details** | `http://localhost:8080/campaign-detail.html?id=1` | View story, progress, donors, donate, or manage campaign |
| ➕ **Start Campaign** | `http://localhost:8080/create-campaign.html` | Clean single-form to launch a campaign with photo upload |
| 💳 **Donate** | `http://localhost:8080/donate.html?id=1` | Make sandbox/test contribution with custom amounts |
| 💝 **My Donations** | `http://localhost:8080/my-donations.html` | Track contribution history and download PDF receipts |
| 👤 **User Profile** | `http://localhost:8080/profile.html` | Manage profile details, view your campaigns & donations |
| 📊 **Admin Dashboard** | `http://localhost:8080/admin-dashboard.html` | Live metrics, Chart.js charts, and CSV report downloads |
| 👥 **Admin Users** | `http://localhost:8080/admin-users.html` | User list, role inspection, and account deletion |
| 🎯 **Admin Campaigns** | `http://localhost:8080/admin-campaigns.html` | Moderate, approve, reject, or remove campaigns |

---

## 📡 REST API Reference

### 🔐 Authentication (`/api/auth`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register a new account | No |
| `POST` | `/api/auth/login` | Login and receive JWT token | No |
| `GET` | `/api/auth/profile` | Fetch authenticated user profile | Yes (Bearer) |
| `PUT` | `/api/auth/profile` | Update profile information | Yes (Bearer) |

### 📋 Campaigns (`/api/campaigns`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `GET` | `/api/campaigns` | Get all active campaigns (paginated) | No |
| `GET` | `/api/campaigns/{id}` | Get single campaign by ID | No |
| `GET` | `/api/campaigns/category/{cat}` | Filter campaigns by category | No |
| `GET` | `/api/campaigns/search?keyword=` | Search campaigns by title | No |
| `GET` | `/api/campaigns/my` | Get campaigns created by logged-in user | Yes (Bearer) |
| `POST` | `/api/campaigns` | Create a new campaign | Yes (Bearer) |
| `POST` | `/api/campaigns/{id}/upload-image`| Upload cover photo for campaign | Yes (Bearer) |
| `PUT` | `/api/campaigns/{id}` | Update existing campaign | Yes (Owner/Admin) |
| `DELETE`| `/api/campaigns/{id}` | Delete campaign | Yes (Owner/Admin) |

### 💳 Donations (`/api/donations`)
| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/api/donations/initiate` | Initiate donation order | Yes (Bearer) |
| `POST` | `/api/donations/verify` | Verify payment signature and complete donation | Yes (Bearer) |
| `GET` | `/api/donations/my` | Get authenticated user donation history | Yes (Bearer) |
| `GET` | `/api/donations/campaign/{id}` | Get campaign donor list | No |
| `GET` | `/api/donations/{id}/receipt` | Download PDF receipt | Yes (Donor/Admin) |

### 🛡️ Admin (`/api/admin`) — *Requires ADMIN Role*
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/admin/stats` | Platform summary counts & total raised |
| `GET` | `/api/admin/users` | List all registered users |
| `DELETE`| `/api/admin/users/{id}` | Delete user account |
| `GET` | `/api/admin/campaigns` | List all campaigns for moderation |
| `PUT` | `/api/admin/campaigns/{id}/approve`| Approve campaign |
| `PUT` | `/api/admin/campaigns/{id}/reject` | Reject/expire campaign |
| `GET` | `/api/admin/analytics/monthly` | Monthly donation trends for Chart.js |
| `GET` | `/api/admin/analytics/categories` | Category distribution data for Chart.js |
| `GET` | `/api/admin/reports/users/csv` | Download users CSV report |
| `GET` | `/api/admin/reports/donations/csv` | Download donations CSV report |

---

## 📁 Project Directory Structure

```
crowdfund-platform/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/crowdfund/
    │   ├── CrowdfundApplication.java       ← Entry point & default admin seeder
    │   ├── auth/                           ← JWT, SecurityConfig, User entities
    │   ├── campaign/                       ← Campaign CRUD & FileUploadService
    │   ├── donation/                       ← Razorpay, PdfReceiptService, Email
    │   └── admin/                          ← AdminController, AnalyticsService
    └── resources/
        ├── application.properties          ← DB, JWT, and Mail configuration
        └── static/                         ← Frontend HTML/CSS/JS assets
            ├── index.html, login.html, register.html, profile.html
            ├── campaigns.html, campaign-detail.html, create-campaign.html
            ├── donate.html, my-donations.html
            ├── admin-dashboard.html, admin-users.html, admin-campaigns.html
            ├── js/navbar.js                ← Universal navigation component
            └── uploads/                    ← Campaign cover images
```

---

## 📄 License

This project is open-source and developed for the College CEP (Curriculum Enrichment Program).
