package com.example.productsShop.domain.dtos.exports;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserRootDto {
    @XmlAttribute(name = "count")
    private int usersSize;
    @XmlElement(name = "user")
    private List<UserDto> userDto;

    public UserRootDto() {
    }

    public int getUsersSize() {
        return usersSize;
    }

    public void setUsersSize(int usersSize) {
        this.usersSize = usersSize;
    }

    public List<UserDto> getUserDto() {
        return userDto;
    }

    public void setUserDto(List<UserDto> userDto) {
        this.userDto = userDto;
    }
}
