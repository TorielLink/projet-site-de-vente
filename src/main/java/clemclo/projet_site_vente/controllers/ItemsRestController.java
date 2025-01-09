package clemclo.projet_site_vente.controllers;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.services.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemsRestController {

    private final ItemService itemService;

    public ItemsRestController(ItemService itemService) {
        this.itemService = itemService;
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
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<ItemEntity> addItem(@RequestBody ItemEntity item, @RequestParam UserEntity ownerUsername) {
        ItemEntity createdItem = itemService.addItem(item.getDescription(), item.getPrice(), ownerUsername);
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
