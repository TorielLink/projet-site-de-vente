package clemclo.projet_site_vente.controllers;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.repository.UserRepository;
import clemclo.projet_site_vente.services.ItemService;
import clemclo.projet_site_vente.services.SaleService;
import clemclo.projet_site_vente.services.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
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
        // Vérifier si un des champs est vide
        if (user.getUsername() == null || user.getUsername().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getCity() == null || user.getCity().isEmpty()) {
            // Ajouter un message d'erreur et rediriger vers une page d'erreur
            model.addAttribute("errorMessage", "Tous les champs sont obligatoires !");
            return "error"; // Retourne la vue "error"
        }

        // Si les champs sont valides, enregistre l'utilisateur
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
        List<ItemEntity> notSoldItems = itemService.getNotSoldItems(user);
        List<ItemEntity> soldItems = itemService.getSoldItems(user);

        List<ItemEntity> searchResults = (keyword != null && !keyword.isEmpty())
                ? itemService.searchItems(keyword)
                : null;

        if (searchResults != null) {
            searchResults.sort((item1, item2) -> {
                boolean isOwnedByUser1 = item1.getOwner().getId().equals(user.getId());
                boolean isOwnedByUser2 = item2.getOwner().getId().equals(user.getId());

                if (isOwnedByUser1 != isOwnedByUser2) {
                    return isOwnedByUser1 ? -1 : 1;
                }

                boolean isSold1 = item1.isSold();
                boolean isSold2 = item2.isSold();

                if (isSold1 != isSold2) {
                    return isSold1 ? 1 : -1;
                }
                return 0;
            });
        }

        model.addAttribute("user", user);
        model.addAttribute("notSoldItems", notSoldItems);
        model.addAttribute("otherItems", otherItems);
        model.addAttribute("soldItems", soldItems);
        model.addAttribute("searchResults", searchResults);
        if (user.getRole().equals("ADMIN"))
            model.addAttribute("revenue", saleService.getTotalRevenue());
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
}
