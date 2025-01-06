package clemclo.projet_site_vente.api.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestSimple {

    // Méthode accessible publiquement
    @GetMapping("/public")
    public String accessPublic() {
        return "page publique accessible à tous";
    }

    // Méthode pour les utilisateurs
    @GetMapping("/protected/users")
    public String accessUtilisateurs() {
        return "page privée des utilisateurs";
    }

    // Méthode pour les administrateurs
    @GetMapping("/protected/admin")
    public String accessAdministrateurs() {
        return "page privée des administrateurs";
    }
}