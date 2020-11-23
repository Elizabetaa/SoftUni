package com.example.productsShop.domain.dtos.Imports;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserImportRootDto {

    @XmlElement(name = "user")
    private List<UserImportDto> userImportDto;

    public UserImportRootDto() {
    }

    public List<UserImportDto> getUserImportDto() {
        return userImportDto;
    }

    public void setUserImportDto(List<UserImportDto> userImportDto) {
        this.userImportDto = userImportDto;
    }
}
