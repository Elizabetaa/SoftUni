package com.example.carDealer.services.impl;

import com.example.carDealer.domain.dtos.seedDataDtos.CustomerSeedDto;
import com.example.carDealer.domain.dtos.views.CustomerDto;
import com.example.carDealer.domain.dtos.views.CustomerExportDto;
import com.example.carDealer.domain.entities.Customer;
import com.example.carDealer.domain.entities.Part;
import com.example.carDealer.domain.entities.Sale;
import com.example.carDealer.domain.repositories.CustomerRepository;
import com.example.carDealer.services.CustomerService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final static String CUSTOMER_PATH = "src/main/resources/jsons/customers.json";
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, Gson gson) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedCustomers() throws IOException {
        String content = String.join("", Files
                .readAllLines(Paths.get(CUSTOMER_PATH)));
        CustomerSeedDto[] customerSeedDtos = this.gson.fromJson(content,CustomerSeedDto[].class);

        for (CustomerSeedDto customerSeedDto : customerSeedDtos) {
            Customer customer = this.modelMapper.map(customerSeedDto,Customer.class);
            this.customerRepository.saveAndFlush(customer);
        }


    }

    @Override
    public String orderedCustomers() {
        Set<Customer> allByOrderByYoungDriverAscBirthDateAsc = this.customerRepository.getAllByOrderByBirthDateAscYoungDriverAsc();

        List<CustomerExportDto> toExport = new ArrayList<>();
        for (Customer customer : allByOrderByYoungDriverAscBirthDateAsc) {
            CustomerExportDto map = this.modelMapper.map(customer, CustomerExportDto.class);
            toExport.add(map);
        }

        return this.gson.toJson(toExport);
    }

    @Override
    public String getAllWithBoughtCars() {
        Set<Customer> customers = this.customerRepository.getAllByBoughtCars();
        List<CustomerDto>customerDtos = new ArrayList<>();

        for (Customer customerDto : customers) {
            CustomerDto customer = this.modelMapper.map(customerDto,CustomerDto.class);
            customer.setBoughtCars(customerDto.getSales().size());

            double price = 0;
            for (Sale sale : customerDto.getSales()) {
                for (Part part : sale.getCar().getParts()) {
                    price += Double.parseDouble(String.valueOf(part.getPrice()));
                }
            }

            BigDecimal bd = BigDecimal.valueOf(price);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            customer.setSpentMoney(bd);
            customerDtos.add(customer);
        }

        return this.gson.toJson(customerDtos);
    }

}
