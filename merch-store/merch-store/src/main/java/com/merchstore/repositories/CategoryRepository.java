package com.merchstore.repositories;

import com.merchstore.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndDeletedFalse(Long id);

    Optional<Category> findByName(String name);

    boolean existsByNameIgnoreCase(String name);

    List<Category> findAllByDeletedFalse();
}
