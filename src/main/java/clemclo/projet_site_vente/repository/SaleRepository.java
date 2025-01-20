package clemclo.projet_site_vente.repository;

import clemclo.projet_site_vente.models.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
}
