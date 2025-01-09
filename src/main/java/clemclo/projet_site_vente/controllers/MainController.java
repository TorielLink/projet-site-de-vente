package clemclo.projet_site_vente.controllers;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.repository.UserRepository;
import clemclo.projet_site_vente.services.ItemService;
import clemclo.projet_site_vente.services.SaleService;
import clemclo.projet_site_vente.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    private final UserService userService;
    private final ItemService itemService;
    private final SaleService saleService;
    private final UserRepository userRepository;

    public MainController(UserService userService, ItemService itemService, SaleService saleService, UserRepository userRepository) {
        this.userService = userService;
        this.itemService = itemService;
        this.saleService = saleService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String homePage() {
        return "index"; // Page d'accueil
    }

    // Page d'inscription utilisateur
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register"; // Page d'inscription
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserEntity user, Model model) {
        userService.registerUser(user.getUsername(), user.getPassword(), user.getCity());
        model.addAttribute("successMessage", "Utilisateur enregistré avec succès !");
        return "register"; // Retourne la vue "register" avec un message de succès
    }

    // Page de connexion
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Page de connexion
    }


    // Tableau de bord après la connexion
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<ItemEntity> items = itemService.searchItems(""); // Affiche tous les objets non vendus
        model.addAttribute("items", items);
        return "dashboard"; // Page principale après connexion
    }

    // Method to add a new item
    @PostMapping("/items/add")
    public String addItem(@RequestParam("description") String description,
                          @RequestParam("price") double price,
                          @RequestParam("ownerId") Long ownerId,
                          Model model) {

        // Save the item to the database using ItemService
        ItemEntity item = itemService.addItem(description, price, userRepository.getReferenceById(ownerId));

        // Optionally, you can add a success message or refresh the items list
        model.addAttribute("item", item);

        return "dashboard";  // Redirect to the dashboard after adding the item
    }
}
