package com.example.shop.config;

import com.example.shop.model.*;
import com.example.shop.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/** Inserts demo users, categories and products on first run. */
@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository users;
    private final CategoryRepository cats;
    private final ProductRepository products;
    private final BCryptPasswordEncoder enc;

    public DataSeeder(UserRepository u, CategoryRepository c, ProductRepository p, BCryptPasswordEncoder e) {
        this.users = u; this.cats = c; this.products = p; this.enc = e;
    }

    @Override
    public void run(String... args) {
        if (users.count() > 0) return;
        user("Admin", "admin@shop.com", "admin123", "ADMIN");
        user("Demo User", "user@shop.com", "user123", "USER");

        Category el = cats.save(new Category("Electronics"));
        Category bk = cats.save(new Category("Books"));
        Category cl = cats.save(new Category("Clothing"));
        Category hm = cats.save(new Category("Home"));

        product("Wireless Headphones", "Over-ear Bluetooth headphones with 30h battery", "79.99", 25, el);
        product("Smart Watch", "Fitness tracking, heart-rate and sleep monitoring", "129.00", 15, el);
        product("USB-C Fast Charger", "65W GaN charger for laptops and phones", "29.50", 60, el);
        product("Mechanical Keyboard", "RGB backlit keyboard with tactile switches", "89.90", 20, el);
        product("Clean Code", "A handbook of agile software craftsmanship", "34.00", 40, bk);
        product("Effective Java", "Best practices for the Java platform", "42.50", 30, bk);
        product("Atomic Habits", "Tiny changes, remarkable results", "18.99", 50, bk);
        product("Cotton T-Shirt", "Soft, breathable everyday crew-neck tee", "14.99", 100, cl);
        product("Denim Jacket", "Classic fit washed denim jacket", "59.00", 18, cl);
        product("Running Shoes", "Lightweight cushioned shoes for daily runs", "74.95", 35, cl);
        product("Ceramic Coffee Mug Set", "Set of 4 handmade 300ml mugs", "24.00", 45, hm);
        product("LED Desk Lamp", "Dimmable lamp with USB charging port", "32.75", 28, hm);
    }

    private void user(String name, String email, String pwd, String role) {
        User u = new User();
        u.setName(name); u.setEmail(email); u.setPasswordHash(enc.encode(pwd)); u.setRole(role);
        users.save(u);
    }

    private void product(String name, String desc, String price, int stock, Category c) {
        Product p = new Product();
        p.setName(name); p.setDescription(desc); p.setPrice(new BigDecimal(price)); p.setStock(stock); p.setCategory(c);
        products.save(p);
    }
}
