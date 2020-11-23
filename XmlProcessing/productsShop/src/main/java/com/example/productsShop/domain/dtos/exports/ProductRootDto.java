package com.example.productsShop.domain.dtos.exports;

import com.example.productsShop.domain.entities.Product;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sold-product")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductRootDto {

    @XmlElement(name = "product")
    private List<ProductSoldDto> products;

    public ProductRootDto() {
    }

    public List<ProductSoldDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSoldDto> products) {
        this.products = products;
    }
}
