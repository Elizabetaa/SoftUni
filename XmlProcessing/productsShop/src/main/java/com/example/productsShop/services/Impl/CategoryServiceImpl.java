package com.example.productsShop.services.Impl;

import com.example.productsShop.domain.dtos.Imports.CategoriesImportRootDto;
import com.example.productsShop.domain.dtos.Imports.CategoryImportDto;
import com.example.productsShop.domain.dtos.exports.CategoryExportDto;
import com.example.productsShop.domain.dtos.exports.CategoryExportRootDto;
import com.example.productsShop.domain.dtos.exports.ProductInfoDto;
import com.example.productsShop.domain.entities.Category;
import com.example.productsShop.domain.entities.Product;
import com.example.productsShop.repositories.CategoryRepository;
import com.example.productsShop.repositories.ProductRepository;
import com.example.productsShop.services.CategoryService;
import com.example.productsShop.utils.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final static String CATEGORY_DATA_PATH = "src/main/resources/xml/categories.xml";
    private final static String CATEGORY_EXPORT_PATH = "src/main/resources/xml/exports/categoryByProductsCount.fxml";
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;


    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, XmlParser xmlParser, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;

        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
    }


    @Override
    public void seedCategoriesData() throws JAXBException {
        CategoriesImportRootDto categoriesImportRootDto = this.xmlParser.parseXml(CategoriesImportRootDto.class, CATEGORY_DATA_PATH);

        for (CategoryImportDto category : categoriesImportRootDto.getCategories()) {
            Category map = this.modelMapper.map(category, Category.class);
            map.setProducts(getRandomProducts());
            this.categoryRepository.saveAndFlush(map);
        }
    }

    private Set<Product> getRandomProducts() {
        Set<Product> products = new LinkedHashSet<>();

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int bound = random.nextInt((int) this.productRepository.count()) + 1;
            Product product = this.productRepository.findById((long) bound).get();
            products.add(product);
        }
        return products;
    }


    @Override
    public void getAllCategoriesByProductsCount() throws JAXBException {
        List<CategoryExportDto> dtos = new ArrayList<>();
        List<Category> allCategoriesByProductsCount = this.categoryRepository.getAllCategoriesByProductsCount();
        System.out.println();

        for (Category c : allCategoriesByProductsCount){
            CategoryExportDto categoryExportDto = this.modelMapper.map(c, CategoryExportDto.class);
            ProductInfoDto productInfoDto = new ProductInfoDto();
            productInfoDto.setProductsSize(c.getProducts().size());
            double price = c.getProducts().stream().mapToDouble(p ->
                    Double.parseDouble(String.valueOf(p.getPrice()))).sum();

            BigDecimal total = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
            BigDecimal avg = new BigDecimal(price / c.getProducts().size())
                    .setScale(2, RoundingMode.HALF_UP);


            productInfoDto.setTotalRevenue(total);
            productInfoDto.setAveragePrice(avg);

            categoryExportDto.setProductInfoDto(productInfoDto);
            dtos.add(categoryExportDto);
        }
        CategoryExportRootDto categoryExportRootDto = new CategoryExportRootDto();
        categoryExportRootDto.setCategories(dtos);

        this.xmlParser.exportXml(categoryExportRootDto, CategoryExportRootDto.class, CATEGORY_EXPORT_PATH);

    }
}
