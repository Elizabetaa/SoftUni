package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xmls.SellerDto;
import softuni.exam.models.dtos.xmls.SellersImportRootDto;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private final static String SELLER_PATH = "src/main/resources/files/xml/sellers.xml";
    private final SellerRepository sellerRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.sellerRepository = sellerRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(SELLER_PATH)));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();
        SellersImportRootDto sellersImportRootDto = this.xmlParser
                .parseXml(SellersImportRootDto.class, SELLER_PATH);

        for (SellerDto sellerDto : sellersImportRootDto.getSellerDto()) {
            Seller seller = this.modelMapper.map(sellerDto, Seller.class);
            Optional<Seller> check = this.sellerRepository.findByEmail(sellerDto.getEmail());
            if (validationUtil.isValid(sellerDto) && check.isEmpty() && seller.getRating() != null) {
                this.sellerRepository.saveAndFlush(seller);
                builder.append(String.format("Successfully import seller %s - %s", sellerDto.getLastName(), sellerDto.getEmail()))
                        .append(System.lineSeparator());
            } else {
                builder.append("Invalid seller").append(System.lineSeparator());
            }
        }


        return builder.toString();
    }
}
