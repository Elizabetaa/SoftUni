package com.example.carDealer.services.impl;

import com.example.carDealer.domain.dtos.seedDataDtos.PartSeedDataDto;
import com.example.carDealer.domain.entities.Part;
import com.example.carDealer.domain.entities.Supplier;
import com.example.carDealer.domain.repositories.PartRepository;
import com.example.carDealer.domain.repositories.SupplierRepository;
import com.example.carDealer.services.PartService;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

@Service
public class PartServiceImpl implements PartService {
    private final static String PART_PATH = "src/main/resources/jsons/parts.json";
    private final PartRepository partRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final SupplierRepository supplierRepository;

    @Autowired
    public PartServiceImpl(PartRepository partRepository, ModelMapper modelMapper, Gson gson, SupplierRepository supplierRepository) {
        this.partRepository = partRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void seedPart() throws Exception {
        String content = String.join("", Files
                .readAllLines(Paths.get(PART_PATH)));

        PartSeedDataDto[] partSeedDataDtos = this.gson.fromJson(content, PartSeedDataDto[].class);
        for (PartSeedDataDto partSeedDataDto : partSeedDataDtos) {
            Part part = this.modelMapper.map(partSeedDataDto, Part.class);
            part.setSupplier(getRandomSupplier());
            this.partRepository.saveAndFlush(part);
        }

    }

    private Supplier getRandomSupplier() throws Exception {
        Random random = new Random();
        long index = (long)random.nextInt((int) this.supplierRepository.count()) + 1;

        Optional<Supplier> supplier = this.supplierRepository.findById(index);
        if (supplier.isPresent()){
            return supplier.get();
        }else{
            throw new Exception("Supplier don't exist");
        }
    }
}
