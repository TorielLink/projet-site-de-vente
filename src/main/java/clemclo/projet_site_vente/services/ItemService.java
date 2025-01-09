package clemclo.projet_site_vente.services;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemEntity addItem(String description, double price, UserEntity owner) {
        ItemEntity item = new ItemEntity();
        item.setDescription(description);
        item.setPrice(price);
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    public List<ItemEntity> searchItems(String keyword) {
        return itemRepository.findByDescriptionContainingAndSoldFalse(keyword);
    }

    public void markAsSold(Long itemId) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Objet non trouvé"));
        item.setSold(true);
        itemRepository.save(item);
    }

    public ItemEntity getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public boolean deleteItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
