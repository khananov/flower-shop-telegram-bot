package ru.khananov.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.khananov.models.entities.Product;
import ru.khananov.services.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("products", productService.findAll());

        return "index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.findById(id));

        return "show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("product") Product product) {
        return "create";
    }

    @PostMapping()
    public String create(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "create";

        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "edit";
    }

    @PatchMapping("{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("product") @Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "edit";

        productService.save(product);
        return "redirect:/products";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.removeById(id);
        return "redirect:/products";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "query", required = false) String query, Model model) {
        model.addAttribute("products", productService.findByNameStartingWith(query));
        return "search";
    }
}
