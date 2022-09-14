package cat.itacademy.barcelonactiva.CompanyVallet.Ernest.s05.t02.S05T02CompanyValletErnest.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {

    @Pattern(regexp = "[1-6]", message = "The value has to be an integer between 1 and 6")
    private String firstThrow;

    @Pattern(regexp = "[1-6]", message = "The value has to be an integer between 1 and 6")
    private String secondThrow;
}
