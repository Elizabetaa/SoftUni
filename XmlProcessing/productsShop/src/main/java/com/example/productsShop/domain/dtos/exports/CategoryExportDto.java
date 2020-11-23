package com.example.productsShop.domain.dtos.exports;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "category")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryExportDto {

    @XmlAttribute(name = "name")
    private String categoryName;
    @XmlElement
    private ProductInfoDto productInfoDto;

    public CategoryExportDto() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ProductInfoDto getProductInfoDto() {
        return productInfoDto;
    }

    public void setProductInfoDto(ProductInfoDto productInfoDto) {
        this.productInfoDto = productInfoDto;
    }
}
