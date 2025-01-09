package clemclo.projet_site_vente.controllers;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.services.ItemService;
import clemclo.projet_site_vente.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemsRestController {

    private final ItemService itemService;
    private final UserService userService;

    public ItemsRestController(ItemService itemService, UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    // Obtenir tous les objets (items)
    @GetMapping(produces = {"application/json"})
    public List<ItemEntity> getAllItems() {
        return itemService.searchItems("");
    }

    // Obtenir un objet par ID
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<ItemEntity> getItemById(@PathVariable Long id) {
        ItemEntity item = itemService.getItemById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(item);
    }

    // Ajouter un nouvel objet
    @PostMapping(name = "/item/add", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<ItemEntity> addItem(@RequestBody ItemEntity item, @RequestParam Long ownerID) {
        UserEntity owner = userService.getUserById(ownerID);
        if (owner == null)
            return ResponseEntity.badRequest().build(); // Retourne HTTP 400 si l'utilisateur n'existe pas
        ItemEntity createdItem = itemService.addItem(item.getDescription(), item.getPrice(), owner);
        return ResponseEntity.ok(createdItem);
    }

    // Supprimer un objet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemService.deleteItem(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
