package com.example.productsShop.domain.dtos.exports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductExportRootDto {

    @XmlElement(name = "product")
    private List<ProductExportDto> products;

    public ProductExportRootDto() {
    }

    public List<ProductExportDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductExportDto> products) {
        this.products = products;
    }
}
