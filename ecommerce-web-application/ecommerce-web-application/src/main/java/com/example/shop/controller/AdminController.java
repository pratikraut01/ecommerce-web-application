package com.example.shop.controller;

import com.example.shop.model.CustomerOrder;
import com.example.shop.model.Product;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductRepository products;
    private final CategoryRepository categories;
    private final OrderRepository orders;

    public AdminController(ProductRepository p, CategoryRepository c, OrderRepository o) {
        this.products = p; this.categories = c; this.orders = o;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("products", products.findByActiveTrueOrderByIdDesc());
        model.addAttribute("categories", categories.findAll());
        model.addAttribute("orders", orders.findAllByOrderByCreatedAtDesc());
        model.addAttribute("statuses", CustomerOrder.STATUSES);
        return "admin";
    }

    @PostMapping("/products")
    public String addProduct(@RequestParam String name, @RequestParam(defaultValue = "") String description,
                             @RequestParam BigDecimal price, @RequestParam int stock,
                             @RequestParam Long categoryId, @RequestParam(defaultValue = "") String imageUrl,
                             RedirectAttributes ra) {
        if (name.isBlank() || price.signum() < 0 || stock < 0) {
            ra.addFlashAttribute("error", "Invalid product data");
            return "redirect:/admin";
        }
        Product p = new Product();
        p.setName(name.trim()); p.setDescription(description); p.setPrice(price); p.setStock(stock);
        p.setImageUrl(imageUrl.isBlank() ? null : imageUrl.trim());
        p.setCategory(categories.getReferenceById(categoryId));
        products.save(p);
        ra.addFlashAttribute("success", "Product added");
        return "redirect:/admin";
    }

    /** Soft delete so past orders keep their product reference. */
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        products.findById(id).ifPresent(p -> { p.setActive(false); products.save(p); });
        ra.addFlashAttribute("success", "Product removed");
        return "redirect:/admin";
    }

    @PostMapping("/orders/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes ra) {
        if (CustomerOrder.STATUSES.contains(status)) {
            orders.findById(id).ifPresent(o -> { o.setStatus(status); orders.save(o); });
            ra.addFlashAttribute("success", "Order #" + id + " marked " + status);
        }
        return "redirect:/admin";
    }
}
