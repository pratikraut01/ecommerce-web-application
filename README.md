E-Commerce Web Application
Java · Spring MVC · Hibernate ORM · MySQL · HTML / CSS / JavaScript

A dynamic e-commerce app with product listings, shopping cart management and order processing. Built with Spring MVC (controllers + Thymeleaf views) and Hibernate ORM on a normalized MySQL schema.

Features
Product catalog with pagination
Search & filtering: keyword search, category filter, price range, sorting (newest, price, name)
Shopping cart stored in the database per user: add, update quantity, remove
Order processing: transactional checkout that validates stock, reduces inventory, snapshots prices, creates the order and a payment/transaction record, then clears the cart
User accounts: registration, login (BCrypt-hashed passwords), session-based access control
Admin dashboard: add and remove products, view all orders, update order status
Responsive UI (HTML, CSS, JavaScript), light and dark mode
Database Schema (normalized)
users ──< cart_items >── products >── categories
  └──< orders ──< order_items >── products
         └── payments (1:1)
Tables: users, categories, products, cart_items, orders, order_items, payments. See database/schema.sql.

Prerequisites
JDK 17+, Maven 3.8+
MySQL 8+ running locally
Run
Set your MySQL username and password in src/main/resources/application.properties (the shop_db database is created automatically).
Start the app:
mvn spring-boot:run
Open http://localhost:8080
Demo data (categories, 12 products and 2 users) is inserted on first start.

Role	Email	Password
Admin	admin@shop.com	admin123
User	user@shop.com	user123
Project Structure
src/main/java/com/example/shop/
├── model/        # Hibernate entities
├── repository/   # Spring Data JPA repositories (search/filter query)
├── service/      # OrderService (transactional checkout)
├── controller/   # Spring MVC controllers
└── config/       # Interceptor, global model, data seeder
src/main/resources/
├── templates/    # Thymeleaf HTML pages
└── static/       # CSS and JavaScript
Notes
Payment is simulated (no real gateway). Card/UPI orders are marked PAID, COD is PENDING.
This demo uses session auth without CSRF protection. For production, add Spring Security (CSRF, roles) and a real payment gateway.
License
MIT
