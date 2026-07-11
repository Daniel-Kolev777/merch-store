package repositories;

import models.Category;
import models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrue();

    List<Product> findByCategoryAndActiveTrue(Category category);

    List<Product> findByNameContainingIgnoreCase(String name);
}
