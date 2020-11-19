package com.example.productsShop.domain.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product extends BaseEntity{

    private String name;
    private BigDecimal price;
    private User buyer;
    private User seller;
    private Set<Category> categories = new LinkedHashSet<>();


    public Product() {

    }

    public Product(String name, BigDecimal price, User buyer, User seller, Category category) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.seller = seller;
        this.categories.add(category);
    }

    @Length(min = 3)
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }



    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id",referencedColumnName = "id")
    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyerId) {
        this.buyer = buyerId;
    }

    @ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id",name = "seller_id")
    public User getSeller() {
        return seller;
    }

    public void setSeller(User sellerId) {
        this.seller = sellerId;
    }

    @ManyToMany
    @JoinTable(name="category_products",
            joinColumns = @JoinColumn(name="product_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="category_id",referencedColumnName = "id"))
    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
