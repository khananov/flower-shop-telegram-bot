package ru.khananov.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.khananov.models.entities.Order;
import ru.khananov.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("orders", orderService.findAll());

        return "order/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Order order = orderService.findById(id);

        model.addAttribute("order", order);
        model.addAttribute("user", order.getTelegramUser());
        model.addAttribute("products", order.getProductsForCart());

        return "order/show";
    }

    @PatchMapping("{id}")
    public String update(@PathVariable("id") Long id) {
        orderService.delivered(id);

        return "redirect:/orders/" + id;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query", required = false) String query, Model model) {
        model.addAttribute("orders", orderService.findByIdStartingWith(query));
        return "order/search";
    }
}
