package com.example.carDealer.services.impl;

import com.example.carDealer.domain.dtos.seedDataDtos.SupplierSeedDto;
import com.example.carDealer.domain.dtos.views.SuppliersIsNotImporterDto;
import com.example.carDealer.domain.entities.Supplier;
import com.example.carDealer.domain.repositories.SupplierRepository;
import com.example.carDealer.services.SupplierService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SupplierServiceImpl implements SupplierService {
    private final static String SUPPLIER_PATH = "src/main/resources/jsons/suppliers.json";
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository, ModelMapper modelMapper, Gson gson) {
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedSupplier() throws IOException {
        String content = String.join("",Files
                .readAllLines(Paths.get(SUPPLIER_PATH)));

        SupplierSeedDto[] supplierSeedDtos = this.gson.fromJson(content, SupplierSeedDto[].class);
        for (SupplierSeedDto supplierSeedDto : supplierSeedDtos) {
            this.supplierRepository.saveAndFlush(this.modelMapper.map(supplierSeedDto, Supplier.class));

        }
    }

    @Override
    public String getAllSuppliers() {
        Set<Supplier> allByImporter = this.supplierRepository.findAllByImporter(false);

        List<SuppliersIsNotImporterDto> suppliers = new ArrayList<>();
        for (Supplier supplier : allByImporter) {
            SuppliersIsNotImporterDto map = this.modelMapper.map(supplier,SuppliersIsNotImporterDto.class);
            map.setPartsCount(supplier.getParts().size());
            suppliers.add(map);
        }

        return this.gson.toJson(suppliers);
    }
}
