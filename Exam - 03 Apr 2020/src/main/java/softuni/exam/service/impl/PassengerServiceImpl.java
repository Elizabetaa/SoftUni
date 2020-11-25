package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.jsons.PassengerImportDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
public class PassengerServiceImpl implements PassengerService {
    private final static String PASSENGER_PATH = "src/main/resources/files/json/passengers.json";
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final PassengerRepository passengerRepository;

    public PassengerServiceImpl(TownRepository townRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, PassengerRepository passengerRepository) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.passengerRepository = passengerRepository;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(PASSENGER_PATH)));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder builder = new StringBuilder();
        PassengerImportDto[] passengerImportDtos = this.gson.fromJson(readPassengersFileContent(), PassengerImportDto[].class);
        for (PassengerImportDto passengerImportDto : passengerImportDtos) {
            if (validationUtil.isValid(passengerImportDto)){
                Passenger passenger = this.modelMapper.map(passengerImportDto,Passenger.class);
                Town town = this.townRepository.findByName(passengerImportDto.getTown());
                passenger.setTown(town);
                this.passengerRepository.saveAndFlush(passenger);

                builder.append(String.format("Successfully imported Passenger %s - %s",passenger.getLastName(),passenger.getEmail()))
                        .append(System.lineSeparator());
            }else{
                builder.append("Invalid Passenger")
                        .append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder builder = new StringBuilder();

        Set<Passenger> passengers = this.passengerRepository.findAllOrderByTicketCountAndEmail();
        for (Passenger passenger : passengers) {
            builder.append(String.format("Passenger %s  %s%n" +
                    "\tEmail - %s%n" +
                    "\tPhone - %s%n" +
                    "\tNumber of tickets - %d%n",
                    passenger.getFirstName(),passenger.getLastName(),
                    passenger.getEmail(),
                    passenger.getPhoneNumber(),
                    passenger.getTickets().size()));
        }

        return builder.toString();
    }
}
