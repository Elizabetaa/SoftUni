package com.example.productsShop.domain.dtos.exports;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "sold-products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductSoldRootDto {
    @XmlAttribute(name = "count")
    private int productsSize;
    @XmlElement(name = "product")
    private List<ProductDto> products;

    public ProductSoldRootDto() {
    }

    public int getProductsSize() {
        return productsSize;
    }

    public void setProductsSize(int productsSize) {
        this.productsSize = productsSize;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
