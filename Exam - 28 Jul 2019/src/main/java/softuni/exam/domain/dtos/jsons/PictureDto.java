package softuni.exam.domain.dtos.jsons;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;

public class PictureDto {

    @Expose
    private String url;

    public PictureDto() {
    }

    @NotNull
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
