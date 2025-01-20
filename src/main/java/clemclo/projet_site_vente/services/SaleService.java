package clemclo.projet_site_vente.services;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.SaleEntity;
import clemclo.projet_site_vente.repository.ItemRepository;
import clemclo.projet_site_vente.repository.SaleRepository;
import org.springframework.stereotype.Service;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ItemRepository itemRepository;

    public SaleService(SaleRepository saleRepository, ItemRepository itemRepository) {
        this.saleRepository = saleRepository;
        this.itemRepository = itemRepository;
    }

    public boolean recordSale(Long itemId) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Objet non trouvé"));
        if (item.isSold()) {
            throw new RuntimeException("Objet déjà vendu !");
        }

        item.setSold(true);
        itemRepository.save(item);

        SaleEntity sale = new SaleEntity();
        sale.setItem(item);
        sale.setCommission(item.getPrice() * 0.1); // 10% commission
        saleRepository.save(sale);
        return true;
    }

    public double getTotalRevenue() {
        return saleRepository.findAll().stream()
                .mapToDouble(SaleEntity::getCommission)
                .sum();
    }
}

