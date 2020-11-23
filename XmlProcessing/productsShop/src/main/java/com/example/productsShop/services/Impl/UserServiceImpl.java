package com.example.productsShop.services.Impl;

import com.example.productsShop.domain.dtos.Imports.UserImportDto;
import com.example.productsShop.domain.dtos.Imports.UserImportRootDto;
import com.example.productsShop.domain.dtos.exports.*;
import com.example.productsShop.domain.entities.Product;
import com.example.productsShop.domain.entities.User;
import com.example.productsShop.repositories.UserRepository;
import com.example.productsShop.services.UserService;
import com.example.productsShop.utils.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final static String USER_IMPORT_DATA_PATH = "src/main/resources/xml/users.xml";
    private final static String USER_EXPORT_DATA_PATH = "src/main/resources/xml/exports/usersWithSoldProducts.xml";
    private final static String USER_EXPORT_PATH = "src/main/resources/xml/exports/usersWithSellingProductsOrdered.xml";
    private final UserRepository userRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, XmlParser xmlParser, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedUsersData() throws JAXBException {
        UserImportRootDto userImportRootDto = this.xmlParser
                .parseXml(UserImportRootDto.class,USER_IMPORT_DATA_PATH);

        for (UserImportDto userImportDto : userImportRootDto.getUserImportDto()) {
            User map = this.modelMapper.map(userImportDto,User.class);
            this.userRepository.saveAndFlush(map);
        }
    }

    @Override
    public void getAllUsersWithSales() throws JAXBException {
        List<UserExportDto> dtos = new ArrayList<>();
        this.userRepository.getAllUsersWithSales().forEach(u -> {
            UserExportDto map = this.modelMapper.map(u,UserExportDto.class);
            List<ProductSoldDto> productSoldDtos = new ArrayList<>();
            u.getSellingProducts().forEach(p -> {
                ProductSoldDto map1 = this.modelMapper.map(p, ProductSoldDto.class);
                productSoldDtos.add(map1);
            });
            ProductRootDto productRootDto = new ProductRootDto();
            productRootDto.setProducts(productSoldDtos);
            map.setProduct(productRootDto);
            dtos.add(map);
        });
        UsersExportRootDto root = new UsersExportRootDto();
        root.setUsers(dtos);
        this.xmlParser.exportXml(root,UsersExportRootDto.class,USER_EXPORT_DATA_PATH);
    }

    @Override
    public void getAllUsersWithSalesOrdered() throws JAXBException {

        List<UserDto> userDtos = new ArrayList<>();

        this.userRepository.getAllUsersWithSalesOrdered().forEach(u -> {
            UserDto userDto = this.modelMapper.map(u,UserDto.class);
            List<ProductDto> productDtos =new ArrayList<>();
            for (Product sellingProduct : u.getSellingProducts()) {
                productDtos.add(this.modelMapper.map(sellingProduct,ProductDto.class));
            }
            ProductSoldRootDto productRootDto = new ProductSoldRootDto();
            productRootDto.setProducts(productDtos);
            productRootDto.setProductsSize(productDtos.size());
            userDto.setProductSoldRootDto(productRootDto);
            userDtos.add(userDto);
        });
        UserRootDto userRootDto = new UserRootDto();
        userRootDto.setUserDto(userDtos);
        userRootDto.setUsersSize(userDtos.size());

        this.xmlParser.exportXml(userRootDto,UserRootDto.class,USER_EXPORT_PATH);
    }
}
