package com.example.shop.controller;

import com.example.shop.model.Product;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;

@Controller
public class ShopController {
    private final ProductRepository products;
    private final CategoryRepository categories;

    public ShopController(ProductRepository products, CategoryRepository categories) {
        this.products = products;
        this.categories = categories;
    }

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "") String q,
                       @RequestParam(defaultValue = "0") Long cat,
                       @RequestParam(required = false) BigDecimal min,
                       @RequestParam(required = false) BigDecimal max,
                       @RequestParam(defaultValue = "new") String sort,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        Sort s = switch (sort) {
            case "priceAsc" -> Sort.by("price").ascending();
            case "priceDesc" -> Sort.by("price").descending();
            case "name" -> Sort.by("name").ascending();
            default -> Sort.by("id").descending();
        };
        Page<Product> result = products.search(q.trim(), cat,
                min == null ? BigDecimal.ZERO : min,
                max == null ? new BigDecimal("99999999") : max,
                PageRequest.of(Math.max(page, 0), 8, s));

        model.addAttribute("page", result);
        model.addAttribute("categories", categories.findAll());
        model.addAttribute("q", q);
        model.addAttribute("cat", cat);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("sort", sort);
        return "products";
    }
}
