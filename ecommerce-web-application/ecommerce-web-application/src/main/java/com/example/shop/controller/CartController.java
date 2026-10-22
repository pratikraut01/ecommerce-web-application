package com.example.shop.controller;

import com.example.shop.model.CartItem;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartItemRepository cart;
    private final ProductRepository products;
    private final UserRepository users;

    public CartController(CartItemRepository cart, ProductRepository products, UserRepository users) {
        this.cart = cart; this.products = products; this.users = users;
    }

    @GetMapping
    public String view(HttpSession s, Model model) {
        List<CartItem> items = cart.findByUserIdOrderByIdAsc((Long) s.getAttribute("uid"));
        model.addAttribute("items", items);
        model.addAttribute("total", items.stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add));
        return "cart";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long productId, @RequestParam(defaultValue = "1") int qty,
                      HttpSession s, HttpServletRequest req, RedirectAttributes ra) {
        Long uid = (Long) s.getAttribute("uid");
        var product = products.findById(productId).filter(p -> p.isActive()).orElse(null);
        if (product == null || product.getStock() <= 0) {
            ra.addFlashAttribute("error", "Product is not available");
        } else {
            CartItem ci = cart.findByUserIdAndProductId(uid, productId).orElseGet(() -> {
                CartItem n = new CartItem();
                n.setUser(users.getReferenceById(uid));
                n.setProduct(product);
                return n;
            });
            ci.setQuantity(Math.min(ci.getQuantity() + Math.max(qty, 1), product.getStock()));
            cart.save(ci);
            ra.addFlashAttribute("success", product.getName() + " added to cart");
        }
        String back = req.getHeader("Referer");
        return "redirect:" + (back != null ? back : "/");
    }

    @PostMapping("/update")
    public String update(@RequestParam Long itemId, @RequestParam int qty, HttpSession s) {
        cart.findById(itemId).filter(i -> i.getUser().getId().equals(s.getAttribute("uid"))).ifPresent(i -> {
            if (qty <= 0) cart.delete(i);
            else { i.setQuantity(Math.min(qty, i.getProduct().getStock())); cart.save(i); }
        });
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long itemId, HttpSession s) {
        cart.findById(itemId).filter(i -> i.getUser().getId().equals(s.getAttribute("uid"))).ifPresent(cart::delete);
        return "redirect:/cart";
    }
}
