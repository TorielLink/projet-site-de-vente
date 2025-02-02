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
        System.out.println(item);
        return itemRepository.save(item);
    }

    public List<ItemEntity> searchItems(String keyword) {
        return itemRepository.findByDescriptionContainingAndSoldFalse(keyword);
    }

    public List<ItemEntity> getAllItems() {
        return itemRepository.findAll();
    }

    public List<ItemEntity> getAllItemsByUser(UserEntity user) {
        return itemRepository.findByOwnerOrderBySold(user);
    }

    public List<ItemEntity> getOtherUsersItems(UserEntity user) {
        return itemRepository.findByOwnerNot(user);
    }

    public void markAsSold(Long itemId) {
        ItemEntity item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Objet non trouvé"));
        item.setSold(true);
        itemRepository.save(item);
    }

    public ItemEntity getItemById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    public List<ItemEntity> getSoldItems(UserEntity user) {
        return itemRepository.findByOwnerAndSoldTrueOrderByPriceDesc(user);
    }

    public List<ItemEntity> getNotSoldItems(UserEntity user) {
        return itemRepository.findByOwnerAndSoldFalseOrderByPriceDesc(user);
    }

    public boolean deleteItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
