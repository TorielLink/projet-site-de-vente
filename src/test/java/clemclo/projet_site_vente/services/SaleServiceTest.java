package clemclo.projet_site_vente.services;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.SaleEntity;
import clemclo.projet_site_vente.repository.ItemRepository;
import clemclo.projet_site_vente.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SaleServiceTest {
    @Mock
    private SaleRepository saleRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private SaleService saleService;

    @BeforeEach
    void setUp() {
        saleRepository = mock(SaleRepository.class);
        itemRepository = mock(ItemRepository.class);
        saleService = new SaleService(saleRepository, itemRepository);
    }

    @Test
    void recordSale_ShouldRecordSale_WhenItemIsAvailable() {
        // Arrange
        Long itemId = 1L;
        ItemEntity item = new ItemEntity();
        item.setId(itemId);
        item.setPrice(100.0);
        item.setSold(false);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(ItemEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(saleRepository.save(any(SaleEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        boolean result = saleService.recordSale(itemId);

        // Assert
        assertTrue(result);
        assertTrue(item.isSold());

        // Capture the SaleEntity saved
        ArgumentCaptor<SaleEntity> saleCaptor = ArgumentCaptor.forClass(SaleEntity.class);
        verify(saleRepository).save(saleCaptor.capture());

        SaleEntity savedSale = saleCaptor.getValue();
        assertEquals(item, savedSale.getItem());
        assertEquals(10.0, savedSale.getCommission()); // 10% of 100.0

        // Verify interactions
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(item);
        verify(saleRepository, times(1)).save(any(SaleEntity.class));
    }

    @Test
    void recordSale_ShouldThrowException_WhenItemIsAlreadySold() {
        // Arrange
        Long itemId = 2L;
        ItemEntity item = new ItemEntity();
        item.setId(itemId);
        item.setSold(true);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> saleService.recordSale(itemId));
        assertEquals("Objet déjà vendu !", exception.getMessage());

        // Verify no interactions with save methods
        verify(itemRepository, never()).save(any(ItemEntity.class));
        verify(saleRepository, never()).save(any(SaleEntity.class));
    }

    @Test
    void recordSale_ShouldThrowException_WhenItemNotFound() {
        // Arrange
        Long itemId = 3L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> saleService.recordSale(itemId));
        assertEquals("Objet non trouvé", exception.getMessage());

        // Verify no interactions with save methods
        verify(itemRepository, never()).save(any(ItemEntity.class));
        verify(saleRepository, never()).save(any(SaleEntity.class));
    }

    @Test
    void getTotalRevenue_ShouldReturnSumOfCommissions() {
        // Arrange
        SaleEntity sale1 = new SaleEntity();
        sale1.setCommission(10.0);

        SaleEntity sale2 = new SaleEntity();
        sale2.setCommission(20.0);

        List<SaleEntity> sales = Arrays.asList(sale1, sale2);
        when(saleRepository.findAll()).thenReturn(sales);

        // Act
        double totalRevenue = saleService.getTotalRevenue();

        // Assert
        assertEquals(30.0, totalRevenue);

        // Verify interaction
        verify(saleRepository, times(1)).findAll();
    }

    @Test
    void getTotalRevenue_ShouldReturnZero_WhenNoSales() {
        // Arrange
        when(saleRepository.findAll()).thenReturn(List.of());

        // Act
        double totalRevenue = saleService.getTotalRevenue();

        // Assert
        assertEquals(0.0, totalRevenue);

        // Verify interaction
        verify(saleRepository, times(1)).findAll();
    }
}
