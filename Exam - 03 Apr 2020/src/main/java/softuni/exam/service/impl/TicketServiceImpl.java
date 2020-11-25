package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xmls.TicketImportDto;
import softuni.exam.models.dtos.xmls.TicketImportRootDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {
    private final static String TICKET_PATH = "src/main/resources/files/xml/tickets.xml";
    private final PlaneRepository planeRepository;
    private final TownRepository townRepository;
    private final PassengerRepository passengerRepository;
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;

    @Autowired
    public TicketServiceImpl(PlaneRepository planeRepository, TownRepository townRepository, PassengerRepository passengerRepository, TicketRepository ticketRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil) {
        this.planeRepository = planeRepository;
        this.townRepository = townRepository;
        this.passengerRepository = passengerRepository;
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return  String.join("", Files.readAllLines(Path.of(TICKET_PATH)));
    }

    @Override
    public String importTickets() throws IOException, JAXBException {
        StringBuilder builder = new StringBuilder();
        TicketImportRootDto ticketImportRootDto = this.xmlParser.parseXml(TicketImportRootDto.class, TICKET_PATH);

        for (TicketImportDto ticket : ticketImportRootDto.getTickets()) {
            Ticket ticket1 = this.modelMapper.map(ticket,Ticket.class);
            String tt = ticket.getFromTown().getName();
            Town fromTown = this.townRepository.findByName(tt);
            Town toTown = this.townRepository.findByName(ticket.getToTown().getName());
            Passenger passenger = this.passengerRepository.findByEmail(ticket.getPassengerEmail().getEmail());
            Plane plane = this.planeRepository.findByRegisterNumber(ticket.getPlaneRegisterNumber().getRegisterNumber());

            if (validationUtil.isValid(ticket) && validationUtil.isValid(ticket.getFromTown().getName()) && validationUtil.isValid(ticket.getToTown().getName()) &&
            validationUtil.isValid(ticket.getPassengerEmail().getEmail()) && validationUtil.isValid(ticket.getPlaneRegisterNumber().getRegisterNumber())){
                ticket1.setFromTownId(fromTown);
                ticket1.setToTownId(toTown);
                ticket1.setPassenger(passenger);
                ticket1.setPlane(plane);

                this.ticketRepository.saveAndFlush(ticket1);

                builder.append(String.format("Successfully imported Ticket %s - %s",ticket1.getFromTownId().getName(),ticket1.getToTownId().getName()))
                        .append(System.lineSeparator());

            }else {
                builder.append("Invalid Ticket")
                        .append(System.lineSeparator());
            }
        }

        return builder.toString();
    }
}
