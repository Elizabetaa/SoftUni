package com.example.carDealer.services.impl;

import com.example.carDealer.domain.dtos.views.SalesWithDiscountDto;
import com.example.carDealer.domain.entities.Car;
import com.example.carDealer.domain.entities.Customer;
import com.example.carDealer.domain.entities.Part;
import com.example.carDealer.domain.entities.Sale;
import com.example.carDealer.domain.repositories.CarRepository;
import com.example.carDealer.domain.repositories.CustomerRepository;
import com.example.carDealer.domain.repositories.SaleRepository;
import com.example.carDealer.services.SaleService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, CarRepository carRepository,
                           CustomerRepository customerRepository, ModelMapper modelMapper, Gson gson) {
        this.saleRepository = saleRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;

        this.gson = gson;
    }

    @Override
    public void seedSale() {
        for (int i = 0; i < 5; i++) {
            Sale sale = new Sale();
            sale.setCar(getRandomCar());
            sale.setCustomer(getRandomCustomer());
            sale.setDiscount(getRandomDiscount());
            this.saleRepository.saveAndFlush(sale);
        }
    }

    @Override
    public String getSalesWithAndWithoutDiscount() {
        List<Sale> sales = this.saleRepository.findAll();
        List<SalesWithDiscountDto> salesWithDiscountDtos = new ArrayList<>();

        for (Sale sale : sales) {
            SalesWithDiscountDto map = this.modelMapper.map(sale, SalesWithDiscountDto.class);
            map.setName(sale.getCustomer().getName());
            map.setDiscount(sale.getDiscount());

            double price = 0;
            for (Part part : sale.getCar().getParts()) {
                price += Double.parseDouble(String.valueOf(part.getPrice()));
            }
            BigDecimal bd = BigDecimal.valueOf(price);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            map.setPrice(bd);

           if (sale.getDiscount() > 0) {
                price = price * ((Double.parseDouble(String.valueOf(sale.getDiscount())) / 100));
                map.setPriceWithDiscount(BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP));
            }else {
                map.setPriceWithDiscount(bd);
            }
            salesWithDiscountDtos.add(map);

        }

        return this.gson.toJson(salesWithDiscountDtos);
    }

    private int getRandomDiscount() {
        Random random = new Random();
        return 10 * random.nextInt(6);
    }

    private Customer getRandomCustomer() {
        Random random = new Random();
        long index = (long) random.nextInt((int) this.customerRepository.count()) + 1;
        return this.customerRepository.findById(index).get();
    }

    private Car getRandomCar() {
        Random random = new Random();
        long index = (long) random.nextInt((int) this.carRepository.count()) + 1;
        return this.carRepository.findById(index).get();
    }


}
