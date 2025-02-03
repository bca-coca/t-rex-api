package trex.com.web.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import trex.com.web.sales.model.ItemModel;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Long> {
}
