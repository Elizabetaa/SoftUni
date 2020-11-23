package com.example.productsShop.domain.dtos.exports;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDto {
    @XmlAttribute(name = "first-name")
    private String firstName;
    @XmlAttribute(name = "last-name")
    private String lastName;
    @XmlAttribute
    private int age;
    @XmlElement (name = "sold-product")
    private ProductSoldRootDto productSoldRootDto;

    public UserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ProductSoldRootDto getProductSoldRootDto() {
        return productSoldRootDto;
    }

    public void setProductSoldRootDto(ProductSoldRootDto productSoldRootDto) {
        this.productSoldRootDto = productSoldRootDto;
    }
}
