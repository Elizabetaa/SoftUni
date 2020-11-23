package com.example.productsShop.domain.dtos.Imports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductImportDataRootDto {

    @XmlElement(name = "product")
    private List<ProductImportDataDto> products;

    public ProductImportDataRootDto() {
    }

    public List<ProductImportDataDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductImportDataDto> products) {
        this.products = products;
    }
}
