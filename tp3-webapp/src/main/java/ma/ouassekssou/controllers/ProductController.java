package ma.ouassekssou.controllers;

import jakarta.validation.Valid;
import ma.ouassekssou.entities.Product;
import ma.ouassekssou.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Étape 6 : afficher la liste des produits (+ étape 8 : recherche/pagination)
    @GetMapping({"/", "/products"})
    public String listProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            Model model) {

        Page<Product> productPage = productRepository.findByNameContainsIgnoreCase(keyword,
                PageRequest.of(page, size));

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("pages", new int[productPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "products/list";
    }

    // Étape 6 : supprimer un produit
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id,
                                 @RequestParam(defaultValue = "") String keyword,
                                 @RequestParam(defaultValue = "0") int page,
                                 RedirectAttributes redirectAttributes) {
        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Produit supprimé avec succès.");
        return "redirect:/products?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/form";
    }

    // Étape 8 : édition d'un produit existant
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable : " + id));
        model.addAttribute("product", product);
        return "products/form";
    }

    // Étape 6 : ajout avec validation du formulaire (+ étape 8 : mise à jour, car réutilisé pour l'édition)
    @PostMapping("/products/save")
    public String saveProduct(@Valid @ModelAttribute Product product,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "products/form";
        }
        boolean isUpdate = product.getId() != null;
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("message",
                isUpdate ? "Produit mis à jour avec succès." : "Produit ajouté avec succès.");
        return "redirect:/products";
    }
}
