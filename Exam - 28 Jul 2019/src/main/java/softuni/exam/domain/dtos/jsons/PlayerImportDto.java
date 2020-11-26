package softuni.exam.domain.dtos.jsons;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import softuni.exam.domain.entities.Position;

import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.math.BigDecimal;

public class PlayerImportDto {

    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private int number;
    @Expose
    private BigDecimal salary;
    @Expose
    private Position position;
    @Expose
    private PictureDto picture;
    @Expose
    private TeamDto team;

    public PlayerImportDto() {
    }

    @NotNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull
    @Length(min = 3, max = 15)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    @NotNull
    @Min(1)
    @Max(99)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @NotNull
    @DecimalMin("0")
    public BigDecimal getSalary() {
        return salary;
    }


    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @NotNull
    @Enumerated
    public Position getPosition() {
        return position;
    }


    public void setPosition(Position position) {
        this.position = position;
    }

    @NotNull
    public PictureDto getPicture() {
        return picture;
    }

    public void setPicture(PictureDto picture) {
        this.picture = picture;
    }

    @NotNull
    public TeamDto getTeam() {
        return team;
    }

    public void setTeam(TeamDto team) {
        this.team = team;
    }
}
