
package com.example.productsShop.domain.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    private int age;
    private Set<Product> sellingProducts;
//   private Set<Product> boughtProducts;
    private Set<User> friend;



    public User() {

    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    @Length(min = 3)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    @ManyToMany(targetEntity = User.class)
    @JoinTable(
            name = "users_friends",
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "friend_id",referencedColumnName = "id")}
    )
    public Set<User> getFriend() {
        return friend;
    }

    public void setFriend(Set<User> friend) {
        this.friend = friend;
    }

    @OneToMany(mappedBy = "seller",fetch = FetchType.EAGER)
    public Set<Product> getSellingProducts() {
        return sellingProducts;
    }

    public void setSellingProducts(Set<Product> sellingProducts) {
        this.sellingProducts = sellingProducts;
    }

//    @OneToMany(mappedBy = "buyer",  fetch = FetchType.EAGER)
//    public Set<Product> getBoughtProducts() {
//        return boughtProducts;
//    }
//
//    public void setBoughtProducts(Set<Product> boughtProducts) {
//        this.boughtProducts = boughtProducts;
//    }
}