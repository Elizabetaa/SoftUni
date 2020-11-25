package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xmls.OfferDto;
import softuni.exam.models.dtos.xmls.OffersRootDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;

@Service
public class OfferServiceImpl implements OfferService {
    private final static String OFFER_PATH = "src/main/resources/files/xml/offers.xml";
    private final XmlParser xmlParser;
    private final OfferRepository offerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;

    @Autowired
    public OfferServiceImpl(XmlParser xmlParser, OfferRepository offerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, CarRepository carRepository, SellerRepository sellerRepository) {
        this.xmlParser = xmlParser;
        this.offerRepository = offerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.carRepository = carRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count() > 0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(OFFER_PATH)));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();

        OffersRootDto offersRootDto = this.xmlParser.parseXml(OffersRootDto.class, OFFER_PATH);

        for (OfferDto offer : offersRootDto.getOffers()) {
            Offer offer1 = this.modelMapper.map(offer,Offer.class);
            if (validationUtil.isValid(offer)){
                Car car = this.carRepository.findById(offer.getCar().getId()).get();
                Seller seller = this.sellerRepository.findById(offer.getSeller().getId()).get();
                offer1.setCar(car);
                offer1.setSeller(seller);
                offer1.setPictures(new HashSet<>(car.getPictures()));
                Offer description = this.offerRepository.findByDescription(offer.getDescription());
                Offer addedOn = this.offerRepository.findByAddedOn(offer.getAddedOn());

                this.offerRepository.saveAndFlush(offer1);
                builder.append(String.format("Successfully import offer %s - %s",offer.getAddedOn(),offer.isHasGoldStatus()))
                        .append(System.lineSeparator());
            }else {
                builder.append("Invalid offer").append(System.lineSeparator());
            }

        }

        return builder.toString();
    }
}
