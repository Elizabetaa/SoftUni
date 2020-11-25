package softuni.exam.models.dtos.jsons;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.time.LocalDate;

public class CarImportDto {
    @Expose
    private String model;
    @Expose
    private String make;
    @Expose
    private int kilometers;
    @Expose
    private LocalDate registeredOn;

    public CarImportDto() {
    }

    @Length(min = 2,max = 20,message = "Invalid length of model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    @Length(min = 2,max = 20,message = "Invalid length of make")
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    @Min(value = 0,message = "Kilometers must be positive")
    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }
}
