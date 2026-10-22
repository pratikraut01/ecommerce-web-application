-- Reference schema (3NF). Hibernate creates these tables automatically (ddl-auto=update);
-- this file documents the design and can be used to create the DB manually.
CREATE DATABASE IF NOT EXISTS shop_db;
USE shop_db;

CREATE TABLE users (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(255) NOT NULL,
  email         VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
  created_at    DATETIME
);

CREATE TABLE categories (
  id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE products (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(255)  NOT NULL,
  description VARCHAR(1000),
  price       DECIMAL(10,2) NOT NULL,
  stock       INT           NOT NULL,
  image_url   VARCHAR(255),
  active      BIT           NOT NULL DEFAULT 1,
  category_id BIGINT        NOT NULL,
  created_at  DATETIME,
  INDEX idx_product_name (name),
  FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE cart_items (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id    BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  quantity   INT    NOT NULL,
  UNIQUE (user_id, product_id),
  FOREIGN KEY (user_id)    REFERENCES users(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE orders (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id          BIGINT        NOT NULL,
  status           VARCHAR(20)   NOT NULL,
  total_amount     DECIMAL(10,2) NOT NULL,
  shipping_address VARCHAR(500)  NOT NULL,
  created_at       DATETIME,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_items (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id   BIGINT        NOT NULL,
  product_id BIGINT        NOT NULL,
  quantity   INT           NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,   -- price snapshot at purchase time
  FOREIGN KEY (order_id)   REFERENCES orders(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE payments (
  id              BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id        BIGINT        NOT NULL UNIQUE,
  method          VARCHAR(20)   NOT NULL,
  status          VARCHAR(20)   NOT NULL,
  amount          DECIMAL(10,2) NOT NULL,
  transaction_ref VARCHAR(255)  UNIQUE,
  paid_at         DATETIME,
  FOREIGN KEY (order_id) REFERENCES orders(id)
);
