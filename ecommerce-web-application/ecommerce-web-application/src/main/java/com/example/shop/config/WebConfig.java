package com.example.shop.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    /** Protects cart, checkout, orders (login) and admin pages (ADMIN role). */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
                HttpSession s = req.getSession(false);
                if (s == null || s.getAttribute("uid") == null) { res.sendRedirect("/login"); return false; }
                if (req.getRequestURI().startsWith("/admin") && !"ADMIN".equals(s.getAttribute("role"))) {
                    res.sendRedirect("/"); return false;
                }
                return true;
            }
        }).addPathPatterns("/cart/**", "/checkout/**", "/orders/**", "/admin/**");
    }
}
