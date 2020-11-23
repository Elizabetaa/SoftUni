package com.example.productsShop.services.Impl;

import com.example.productsShop.domain.dtos.Imports.ProductImportDataDto;
import com.example.productsShop.domain.dtos.Imports.ProductImportDataRootDto;
import com.example.productsShop.domain.dtos.exports.ProductExportDto;
import com.example.productsShop.domain.dtos.exports.ProductExportRootDto;
import com.example.productsShop.domain.entities.Category;
import com.example.productsShop.domain.entities.Product;
import com.example.productsShop.domain.entities.User;
import com.example.productsShop.repositories.CategoryRepository;
import com.example.productsShop.repositories.ProductRepository;
import com.example.productsShop.repositories.UserRepository;
import com.example.productsShop.services.ProductService;
import com.example.productsShop.utils.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    private final static String PRODUCT_IMPORT_DATA_PATH = "src/main/resources/xml/products.xml";
    private final static String PRODUCT_EXPORT_DATA_PATH = "src/main/resources/xml/exports/productInRange.xml";
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final XmlParser xmlParser;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, UserRepository userRepository, CategoryRepository categoryRepository, XmlParser xmlParser) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.xmlParser = xmlParser;
    }


    @Override
    public void seedDataProducts() throws JAXBException {
        ProductImportDataRootDto productImportDataRootDto = this.xmlParser.parseXml(ProductImportDataRootDto.class, PRODUCT_IMPORT_DATA_PATH);

        for (ProductImportDataDto productImportDataDto : productImportDataRootDto.getProducts()) {
            Product product = this.modelMapper.map(productImportDataDto, Product.class);
            User seller = this.userRepository.findById((long) getRandomUserId()).orElse(null);
            User buyer = this.userRepository.findById((long) getRandomUserId()).orElse(null);
            product.setBuyer(buyer);
            product.setSeller(seller);
            this.productRepository.saveAndFlush(product);
        }
    }

    @Override
    public void findByPriceBetweenAndBuyerIdIsNullOrderByPrice() throws JAXBException {
        List<ProductExportDto> dtos = new ArrayList<>();

        this.productRepository.findByPriceBetweenAndBuyerIdIsNullOrderByPrice()
                .forEach(p -> {
                    ProductExportDto product = this.modelMapper.map(p,ProductExportDto.class);
                    dtos.add(product);
                });
        ProductExportRootDto productExportRootDto = new ProductExportRootDto();
        productExportRootDto.setProducts(dtos);
        this.xmlParser.exportXml(productExportRootDto,ProductExportRootDto.class,PRODUCT_EXPORT_DATA_PATH);
    }

    @Override
    public void getProductsBySellerId(Long id) {

    }

    private Set<Category> getRandomCategory() {
        Random random = new Random();
        Set<Category> categories = new LinkedHashSet<>();
        for (int i = 0; i < 3; i++) {
            int bounds = random.nextInt((int) this.categoryRepository.count()) + 1;
            categories.add(this.categoryRepository.findById((long) bounds).get());
        }
        return categories;
    }

    private int getRandomUserId() {
        Random random = new Random();
        int bounds = random.nextInt((int) this.userRepository.count());
        return bounds;
    }
}
