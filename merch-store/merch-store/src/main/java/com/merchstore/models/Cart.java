package com.merchstore.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(
            name = "user_id",
            unique = true
    )
    private User user;

    @Column(
            name = "cart_token",
            unique = true
    )
    private String cartToken;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getCartToken() {
        return cartToken;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCartToken(String cartToken) {
        this.cartToken = cartToken;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}