Budget Approval System (Grails 6.x)

A web-based budget request and approval system built with **Grails 6**, **MySQL**, and **Grails Mail** plugin.



Features

- Department-based budget tracking
- Submit budget requests (employees)
- Approve/Reject requests (managers)
- Budget validation (10% of yearly allocation limit)
- Audit logging for transparency
- Email notifications on approval/rejection

---

Tech Stack
Backend:Grails 6.x (Groovy on JVM)
Database:MySQL 8.x
Email Service:Gmail SMTP (with app password)
API Format:REST (JSON)





Install Dependencies

Ensure you have:

* **Grails 6.x**
* **Java 17**
* **MySQL 8.x**

Check Grails:

grails -version


Check Java:

java -version


Create and Import MySQL Schema

Open MySQL Workbench:

SOURCE db/schema.sql;

This will:

Create `budget_db`
Create required tables
Insert sample department (`IT`)

Configure Email (Gmail SMTP):

In `application.yml`:


grails:
  mail:
    host: smtp.gmail.com
    port: 587
    username: yourgmail@gmail.com
    password: your-app-password
    props:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true




Run the Application :


grails run-app


API is available at: `http://localhost:8080`

---

API Endpoints:

1. POST `/api/budget-request`

Submit a new budget request


{
  "deptId": 1,
  "requestedAmount": 5000,
  "purpose": "Office chairs",
  "requestedBy": "sam.kiran@example.com"
}




2. PUT `/api/budget-request/{id}/approve`

Approve the request with given ID.



3. PUT `/api/budget-request/{id}/reject`

Reject a request with a reason:

{
  "reason": "Exceeded monthly limit"
}


4. GET `/api/budget-request/pending`

Returns all pending requests.


5. GET `/api/audit-logs?entityType=BudgetRequest`

View Audit logs.



