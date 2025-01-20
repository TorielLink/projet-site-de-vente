package clemclo.projet_site_vente.services;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.UserEntity;
import clemclo.projet_site_vente.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemRepository = mock(ItemRepository.class);
        itemService = new ItemService(itemRepository);
    }

    @Test
    void addItem_ShouldSaveAndReturnNewItem() {
        // Arrange
        String description = "A great item";
        double price = 99.99;
        UserEntity owner = new UserEntity();
        owner.setUsername("owner");

        ItemEntity savedItem = new ItemEntity();
        savedItem.setId(1L);
        savedItem.setDescription(description);
        savedItem.setPrice(price);
        savedItem.setOwner(owner);

        when(itemRepository.save(any(ItemEntity.class))).thenReturn(savedItem);

        // Act
        ItemEntity result = itemService.addItem(description, price, owner);

        // Assert
        assertNotNull(result);
        assertEquals(description, result.getDescription());
        assertEquals(price, result.getPrice());
        assertEquals(owner, result.getOwner());
        verify(itemRepository, times(1)).save(any(ItemEntity.class));
    }

    @Test
    void searchItems_ShouldReturnMatchingItems() {
        // Arrange
        String keyword = "item";
        ItemEntity item1 = new ItemEntity();
        item1.setDescription("item 1");
        ItemEntity item2 = new ItemEntity();
        item2.setDescription("another item");
        List<ItemEntity> items = List.of(item1, item2);

        when(itemRepository.findByDescriptionContainingAndSoldFalse(keyword)).thenReturn(items);

        // Act
        List<ItemEntity> result = itemService.searchItems(keyword);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(item1));
        assertTrue(result.contains(item2));
        verify(itemRepository, times(1)).findByDescriptionContainingAndSoldFalse(keyword);
    }

    @Test
    void markAsSold_ShouldMarkItemAsSold() {
        // Arrange
        Long itemId = 1L;
        ItemEntity item = new ItemEntity();
        item.setId(itemId);
        item.setSold(false);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(ItemEntity.class))).thenReturn(item);

        // Act
        itemService.markAsSold(itemId);

        // Assert
        assertTrue(item.isSold());
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void markAsSold_ShouldThrowException_WhenItemNotFound() {
        // Arrange
        Long itemId = 1L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> itemService.markAsSold(itemId));
        assertEquals("Objet non trouvé", exception.getMessage());
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, never()).save(any(ItemEntity.class));
    }

    @Test
    void getItemById_ShouldReturnItem_WhenItemExists() {
        // Arrange
        Long itemId = 1L;
        ItemEntity item = new ItemEntity();
        item.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Act
        ItemEntity result = itemService.getItemById(itemId);

        // Assert
        assertNotNull(result);
        assertEquals(item, result);
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void getItemById_ShouldReturnNull_WhenItemDoesNotExist() {
        // Arrange
        Long itemId = 1L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act
        ItemEntity result = itemService.getItemById(itemId);

        // Assert
        assertNull(result);
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void deleteItem_ShouldDeleteItem_WhenItemExists() {
        // Arrange
        Long itemId = 1L;

        when(itemRepository.existsById(itemId)).thenReturn(true);

        // Act
        boolean result = itemService.deleteItem(itemId);

        // Assert
        assertTrue(result);
        verify(itemRepository, times(1)).existsById(itemId);
        verify(itemRepository, times(1)).deleteById(itemId);
    }

    @Test
    void deleteItem_ShouldReturnFalse_WhenItemDoesNotExist() {
        // Arrange
        Long itemId = 1L;

        when(itemRepository.existsById(itemId)).thenReturn(false);

        // Act
        boolean result = itemService.deleteItem(itemId);

        // Assert
        assertFalse(result);
        verify(itemRepository, times(1)).existsById(itemId);
        verify(itemRepository, never()).deleteById(anyLong());
    }
}
