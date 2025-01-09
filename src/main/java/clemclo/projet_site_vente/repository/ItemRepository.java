package clemclo.projet_site_vente.repository;

import clemclo.projet_site_vente.models.ItemEntity;
import clemclo.projet_site_vente.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    List<ItemEntity> findByDescriptionContainingAndSoldFalse(String keyword);

    List<ItemEntity> findByOwner(UserEntity user);

    List<ItemEntity> findByOwnerNot(UserEntity user);
}
