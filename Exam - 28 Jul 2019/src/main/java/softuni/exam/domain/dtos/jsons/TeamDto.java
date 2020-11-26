package softuni.exam.domain.dtos.jsons;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class TeamDto {

    @Expose
    private String name;
    @Expose
    private PictureDto picture;


    public TeamDto() {
    }

    @NotNull
    @Length(min = 3, max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public PictureDto getPicture() {
        return picture;
    }

    public void setPicture(PictureDto picture) {
        this.picture = picture;
    }
}
