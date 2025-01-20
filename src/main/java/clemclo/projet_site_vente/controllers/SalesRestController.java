package clemclo.projet_site_vente.controllers;

import clemclo.projet_site_vente.services.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {

    private final SaleService saleService;

    public SalesRestController(SaleService saleService) {
        this.saleService = saleService;
    }

    // Enregistrer une vente
    @PostMapping("/{itemId}")
    public ResponseEntity<Void> recordSale(@PathVariable Long itemId) {
        if (saleService.recordSale(itemId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    // Obtenir le chiffre d'affaires total
    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenue() {
        Double revenue = saleService.getTotalRevenue();
        return ResponseEntity.ok(revenue);
    }
}

