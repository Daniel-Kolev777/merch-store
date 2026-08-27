package com.merchstore.repositories;

import com.merchstore.models.CartItem;
import com.merchstore.models.enums.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartIdAndProductIdAndSize(
            Long cartId,
            Long productId,
            Size size
    );
}