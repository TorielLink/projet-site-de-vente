package clemclo.projet_site_vente.controllers;

import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersRestController {

    private final UserService userService;

    public UsersRestController(UserService userService) {
        this.userService = userService;
    }

    // Obtenir tous les utilisateurs
    @GetMapping(produces = {"application/json"})
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    // Obtenir un utilisateur par ID
    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // Créer un nouvel utilisateur
    @PostMapping(consumes = {"application/json"}, produces = {"application/json"})
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userService.registerUser(user.getUsername(), user.getPassword(), user.getCity());
    }

    // Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

