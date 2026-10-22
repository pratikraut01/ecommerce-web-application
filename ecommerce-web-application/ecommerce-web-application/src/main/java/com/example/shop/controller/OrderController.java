package com.example.shop.controller;

import com.example.shop.model.CartItem;
import com.example.shop.model.CustomerOrder;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orders;
    private final CartItemRepository cart;

    public OrderController(OrderService orderService, OrderRepository orders, CartItemRepository cart) {
        this.orderService = orderService; this.orders = orders; this.cart = cart;
    }

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession s, Model model) {
        List<CartItem> items = cart.findByUserIdOrderByIdAsc((Long) s.getAttribute("uid"));
        if (items.isEmpty()) return "redirect:/cart";
        model.addAttribute("items", items);
        model.addAttribute("total", items.stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam String address, @RequestParam String method,
                             HttpSession s, RedirectAttributes ra) {
        if (address.isBlank() || !List.of("COD", "CARD", "UPI").contains(method)) {
            ra.addFlashAttribute("error", "Please enter an address and choose a payment method");
            return "redirect:/checkout";
        }
        try {
            CustomerOrder o = orderService.checkout((Long) s.getAttribute("uid"), address.trim(), method);
            ra.addFlashAttribute("success", "Order #" + o.getId() + " placed successfully!");
            return "redirect:/orders/" + o.getId();
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/orders")
    public String myOrders(HttpSession s, Model model) {
        model.addAttribute("orders", orders.findByUserIdOrderByCreatedAtDesc((Long) s.getAttribute("uid")));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, HttpSession s, Model model) {
        CustomerOrder o = orders.findById(id).orElse(null);
        boolean admin = "ADMIN".equals(s.getAttribute("role"));
        if (o == null || (!admin && !o.getUser().getId().equals(s.getAttribute("uid")))) return "redirect:/orders";
        model.addAttribute("order", o);
        return "order";
    }
}
