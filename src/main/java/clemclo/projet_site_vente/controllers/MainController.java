package clemclo.projet_site_vente.controllers;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.repository.UserRepository;
import clemclo.projet_site_vente.services.ItemService;
import clemclo.projet_site_vente.services.SaleService;
import clemclo.projet_site_vente.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            String username = authentication.getName();
            return userService.getUserByUsername(username);
        }
        return null; // No authenticated user
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        UserEntity user = getAuthenticatedUser();
        if (user == null) {
            return "redirect:/login";
        }
        List<ItemEntity> otherItems = itemService.getOtherUsersItems(user);
        List<ItemEntity> userItems = itemService.getAllItemsByUser(user);

        List<ItemEntity> searchResults = (keyword != null && !keyword.isEmpty())
                ? itemService.searchItems(keyword)
                : null;

        model.addAttribute("userId", user.getId());
        model.addAttribute("userRole", user.getRole());
        model.addAttribute("userItems", userItems);
        model.addAttribute("otherItems", otherItems);
        model.addAttribute("searchResults", searchResults);
        if (user.getRole().equals("ADMIN"))
            model.addAttribute("revenue", saleService.getTotalRevenue());
        System.out.println("\n\n\n--------------------------------------------------");
        System.out.println(model.getAttribute("revenue"));
        System.out.println("--------------------------------------------------\n\n\n");
        return "dashboard";
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
        model.addAttribute("items", itemService.getAllItems());


        return "redirect:/dashboard";
    }

    // Method to mark an item as sold
    @PostMapping("/items/sell")
    public String recordSale(@RequestParam("itemId") Long itemId,
                             Model model) {

        // Save the item to the database using ItemService
        boolean success = saleService.recordSale(itemId);

        // Optionally, you can add a success message or refresh the items list
        model.addAttribute("items", itemService.getAllItems());

        return "redirect:/dashboard";
    }

    @GetMapping("/protected/admin/revenue")
    public String revenue(Model model) {
        model.addAttribute("revenue", saleService.getTotalRevenue());

        return "revenue";
    }
}
