# 🌱 CrowdFund — Crowdfunding Platform for Social Causes

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen?style=for-the-badge&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?style=for-the-badge&logo=mysql)
![Maven](https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apachemaven)
![Razorpay](https://img.shields.io/badge/Razorpay-Sandbox-02042B?style=for-the-badge&logo=razorpay)

A full-stack crowdfunding web application built with **Java Spring Boot** + **HTML/CSS/JS** for empowering social causes through community funding.

</div>

---

## ✨ Features

- 🔐 **JWT-based Authentication** — Secure login, registration, and role-based access
- 📋 **Campaign Management** — Create, browse, search and manage fundraising campaigns
- 💳 **Razorpay Payment Integration** — Real sandbox payment processing with signature verification
- 🧾 **PDF Donation Receipts** — Auto-generated receipts via iText7
- 📧 **Email Notifications** — Donation confirmations sent via JavaMail/SMTP
- 📊 **Admin Dashboard** — Full analytics with Chart.js (monthly trends, category breakdown)
- 👥 **User Management** — Admin can manage users, approve/reject campaigns
- 📁 **CSV Reports** — Export users and donations as CSV

---

## 🧩 Module Division (Team of 4)

| Module | Description | Package |
|---|---|---|
| **Module 1** | User Authentication & Profile Management | `com.crowdfund.auth` |
| **Module 2** | Campaign Management | `com.crowdfund.campaign` |
| **Module 3** | Donations & Payment Processing | `com.crowdfund.donation` |
| **Module 4** | Admin Dashboard & Analytics | `com.crowdfund.admin` |

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.1.5 |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| Database | MySQL 8.x + Spring Data JPA (Hibernate) |
| Payment | Razorpay Sandbox API |
| PDF | iText7 (7.2.5) |
| Email | JavaMail (SMTP) |
| Frontend | HTML5, CSS3, Vanilla JavaScript |
| Charts | Chart.js (CDN) |
| Build | Maven 3.8+ |

---

## 🚀 Getting Started

### Prerequisites
- ☕ Java 17+
- 📦 Maven 3.8+
- 🐬 MySQL 8.x (running locally)
- 💳 Razorpay account (free sandbox at [razorpay.com](https://razorpay.com))

### 1. Clone the Repository
```bash
git clone https://github.com/YOUR_USERNAME/crowdfund-platform.git
cd crowdfund-platform
```

### 2. Create the MySQL Database
```sql
CREATE DATABASE crowdfund_db;
```

### 3. Configure `application.properties`
Edit `src/main/resources/application.properties` and update:
```properties
# Database
spring.datasource.password=YOUR_MYSQL_PASSWORD

# Razorpay Sandbox Keys (from razorpay.com/dashboard → Settings → API Keys)
razorpay.key_id=rzp_test_XXXXXXXXXXXXXXXX
razorpay.key_secret=XXXXXXXXXXXXXXXXXXXXXXXX

# Gmail SMTP (use App Password, not your real password)
spring.mail.username=your_gmail@gmail.com
spring.mail.password=YOUR_GMAIL_APP_PASSWORD
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

### 5. Open in Browser
| Page | URL |
|---|---|
| 🏠 Home | http://localhost:8080 |
| 🔐 Login | http://localhost:8080/login.html |
| 📝 Register | http://localhost:8080/register.html |
| 📋 Campaigns | http://localhost:8080/campaigns.html |
| 📊 Admin Panel | http://localhost:8080/admin-dashboard.html |

---

## 🔑 Creating an Admin User

1. Register a new user via `/register.html`
2. Run this SQL to grant admin access:
```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'your@email.com';
```

---

## 📡 REST API Endpoints

### Auth (`/api/auth`)
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/register` | Register new user | ❌ |
| POST | `/login` | Login, returns JWT | ❌ |
| GET | `/profile` | Get current user profile | ✅ |
| PUT | `/profile` | Update profile | ✅ |
| POST | `/forgot-password` | Send OTP to email | ❌ |
| POST | `/reset-password` | Reset password with OTP | ❌ |

### Campaigns (`/api/campaigns`)
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| GET | `/` | All active campaigns (paginated) | ❌ |
| GET | `/featured` | Top 6 campaigns | ❌ |
| GET | `/{id}` | Single campaign details | ❌ |
| GET | `/category/{cat}` | Filter by category | ❌ |
| GET | `/search?keyword=` | Search campaigns | ❌ |
| GET | `/my` | My campaigns | ✅ |
| POST | `/` | Create campaign | ✅ |
| PUT | `/{id}` | Update campaign | ✅ |
| DELETE | `/{id}` | Delete campaign | ✅ |

### Donations (`/api/donations`)
| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/initiate` | Create Razorpay order | ✅ |
| POST | `/verify` | Verify payment & complete | ✅ |
| GET | `/my` | My donation history | ✅ |
| GET | `/campaign/{id}` | Campaign's recent donors | ❌ |
| GET | `/{id}/receipt` | Download PDF receipt | ✅ |

### Admin (`/api/admin`) — ADMIN role only
| Method | Endpoint | Description |
|---|---|---|
| GET | `/stats` | Platform overview stats |
| GET | `/users` | All users |
| PUT | `/users/{id}/suspend` | Suspend user |
| GET | `/campaigns` | All campaigns |
| PUT | `/campaigns/{id}/approve` | Approve campaign |
| PUT | `/campaigns/{id}/reject` | Reject campaign |
| GET | `/analytics/monthly` | Monthly donation trend |
| GET | `/analytics/categories` | Category-wise data |
| GET | `/analytics/top-donors` | Top 10 donors |
| GET | `/reports/users/csv` | Export users CSV |
| GET | `/reports/donations/csv` | Export donations CSV |

---

## 📁 Project Structure

```
src/main/
├── java/com/crowdfund/
│   ├── CrowdfundApplication.java
│   ├── auth/          ← Module 1: JWT, Security, User entity
│   ├── campaign/      ← Module 2: Campaign CRUD, File upload
│   ├── donation/      ← Module 3: Razorpay, PDF, Email
│   └── admin/         ← Module 4: Analytics, Admin APIs
└── resources/
    ├── application.properties
    └── static/
        ├── index.html, login.html, register.html, profile.html
        ├── campaigns.html, campaign-detail.html, create-campaign.html
        ├── donate.html, my-donations.html
        ├── admin-dashboard.html, admin-users.html, admin-campaigns.html
        ├── css/styles.css
        └── js/app.js
```

---

## 👨‍💻 Team

| Member | Module | Responsibility |
|---|---|---|
| Member 1 | Auth & Profiles | Spring Security, JWT, User management |
| Member 2 | Campaigns | Campaign CRUD, file uploads, search |
| Member 3 | Donations | Razorpay integration, PDF receipts, email |
| Member 4 | Admin Panel | Analytics, Chart.js, moderation, CSV reports |

---

## 📄 License

This project was built as a College CEP (Curriculum Enrichment Program) project.

---

<div align="center">
Made with ❤️ for Social Good
</div>
