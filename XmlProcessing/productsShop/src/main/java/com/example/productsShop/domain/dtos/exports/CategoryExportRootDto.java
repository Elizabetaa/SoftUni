package com.example.productsShop.domain.dtos.exports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryExportRootDto {

    @XmlElement(name = "category")
    private List<CategoryExportDto> categories;

    public CategoryExportRootDto() {
    }

    public List<CategoryExportDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryExportDto> categories) {
        this.categories = categories;
    }
}
